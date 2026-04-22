package com.tienda.unimagtienda.app.order.service;

import com.tienda.unimagtienda.app.address.entity.Address;
import com.tienda.unimagtienda.app.address.repository.AddressRepository;
import com.tienda.unimagtienda.app.customer.entity.Customer;
import com.tienda.unimagtienda.app.customer.enums.CustomerStatus;
import com.tienda.unimagtienda.app.customer.repository.CustomerRepository;
import com.tienda.unimagtienda.app.inventory.entity.Inventory;
import com.tienda.unimagtienda.app.inventory.repository.InventoryRepository;
import com.tienda.unimagtienda.app.order.dto.CreateOrderRequest;
import com.tienda.unimagtienda.app.order.dto.OrderResponse;
import com.tienda.unimagtienda.app.order.entity.Order;
import com.tienda.unimagtienda.app.order.enums.OrderStatus;
import com.tienda.unimagtienda.app.order.mapper.OrderMapper;
import com.tienda.unimagtienda.app.order.repository.OrderRepository;
import com.tienda.unimagtienda.app.orderItem.dto.CreateOrderItemRequest;
import com.tienda.unimagtienda.app.orderItem.entity.OrderItem;
import com.tienda.unimagtienda.app.orderItem.mapper.OrderItemMapper;
import com.tienda.unimagtienda.app.orderStatusHistory.mapper.OrderStatusHistoryMapper;
import com.tienda.unimagtienda.app.orderStatusHistory.repository.OrderStatusHistoryRepository;
import com.tienda.unimagtienda.app.orderStatusHistory.service.OrderStatusHistoryService;
import com.tienda.unimagtienda.app.product.entity.Product;
import com.tienda.unimagtienda.app.product.repository.ProductRepository;
import com.tienda.unimagtienda.exception.BusinessException;
import com.tienda.unimagtienda.exception.ValidationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock private OrderRepository orderRepository;
    @Mock private CustomerRepository customerRepository;
    @Mock private AddressRepository addressRepository;
    @Mock private ProductRepository productRepository;
    @Mock private InventoryRepository inventoryRepository;
    @Mock private OrderStatusHistoryService historyService;
    @Mock private OrderStatusHistoryRepository orderStatusHistoryRepository;

    @Spy private OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);
    @Spy private OrderItemMapper orderItemMapper = Mappers.getMapper(OrderItemMapper.class);
    @Spy private OrderStatusHistoryMapper orderStatusHistoryMapper = Mappers.getMapper(OrderStatusHistoryMapper.class);

    @InjectMocks
    private OrderServiceImpl service;


    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(orderMapper, "orderItemMapper", orderItemMapper);
    }

    @Test
    @DisplayName("OrderService: crea orden correctamente")
    void shouldCreateOrderSuccessfully() {

        Customer customer = Customer.builder().id(1L).status(CustomerStatus.ACTIVE).build();
        Address address = Address.builder().id(2L).customer(customer).build();
        Product product = Product.builder().id(3L).price(BigDecimal.valueOf(100)).active(true).build();

        CreateOrderItemRequest itemReq = new CreateOrderItemRequest(3L, 2);
        CreateOrderRequest req = new CreateOrderRequest(1L, 2L, List.of(itemReq));

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(addressRepository.findById(2L)).thenReturn(Optional.of(address));
        when(productRepository.findById(3L)).thenReturn(Optional.of(product));

        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> {
            Order o = inv.getArgument(0);
            o.setId(10L);
            return o;
        });

        OrderResponse res = service.createOrder(req);

        assertThat(res.id()).isEqualTo(10L);
        verify(orderRepository).save(any(Order.class));
        verify(historyService).register(any(), eq(OrderStatus.CREATED), anyString());
    }

    @Test
    @DisplayName("OrderService: falla si cliente está inactivo")
    void shouldFailIfCustomerInactive() {
        Customer customer = Customer.builder().id(1L).status(CustomerStatus.INACTIVE).build();
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        var req = new CreateOrderRequest(1L, 2L, List.of());

        assertThatThrownBy(() -> service.createOrder(req))
                .isInstanceOf(BusinessException.class);

        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("OrderService: falla si no hay items")
    void shouldFailIfNoItems() {
        Customer customer = Customer.builder().id(1L).status(CustomerStatus.ACTIVE).build();
        Address address = Address.builder().id(2L).customer(customer).build();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(addressRepository.findById(2L)).thenReturn(Optional.of(address));

        var req = new CreateOrderRequest(1L, 2L, List.of());

        assertThatThrownBy(() -> service.createOrder(req))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    @DisplayName("OrderService: paga orden correctamente")
    void shouldPayOrderSuccessfully() {
        Product product = Product.builder().id(1L).build();

        OrderItem item = OrderItem.builder().product(product).quantity(2).build();
        Order order = Order.builder().id(1L).status(OrderStatus.CREATED).items(List.of(item)).build();

        Inventory inv = Inventory.builder().availableStock(10).build();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(inventoryRepository.findByProductId(1L)).thenReturn(Optional.of(inv));

        service.payOrder(1L);

        assertThat(order.getStatus()).isEqualTo(OrderStatus.PAID);
        assertThat(inv.getAvailableStock()).isEqualTo(8);

        verify(inventoryRepository).save(inv);
        verify(historyService).register(any(), eq(OrderStatus.PAID), anyString());
    }

    @Test
    @DisplayName("OrderService: cancela orden y devuelve stock")
    void shouldCancelPaidOrderAndRestoreStock() {
        Product product = Product.builder().id(1L).build();

        OrderItem item = OrderItem.builder().product(product).quantity(3).build();
        Order order = Order.builder().id(1L).status(OrderStatus.PAID).items(List.of(item)).build();

        Inventory inv = Inventory.builder().availableStock(5).build();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(inventoryRepository.findByProductId(1L)).thenReturn(Optional.of(inv));

        service.cancelOrder(1L);

        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELLED);
        assertThat(inv.getAvailableStock()).isEqualTo(8);

        verify(inventoryRepository).save(inv);
        verify(historyService).register(any(), eq(OrderStatus.CANCELLED), anyString());
    }
}