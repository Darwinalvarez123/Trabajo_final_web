package com.tienda.unimagTienda.domine.service;

import com.tienda.unimagTienda.api.dto.InventoryDto.UpdateStockRequest;
import com.tienda.unimagTienda.api.dto.OrderDto.*;
import com.tienda.unimagTienda.api.dto.OrderItemDto.*;
import com.tienda.unimagTienda.domine.entity.*;
import com.tienda.unimagTienda.domine.enums.CustomerStatus;
import com.tienda.unimagTienda.domine.enums.OrderStatus;
import com.tienda.unimagTienda.domine.repository.*;
import com.tienda.unimagTienda.domine.service.mapper.OrderMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private InventoryService inventoryService;

    @Mock
    private OrderStatusHistoryService orderStatusHistoryService;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createOrder_shouldReturnOrderResponse_whenSuccessful() {
        // Given
        Long customerId = 1L;
        Long addressId = 1L;
        Long productId = 1L;
        Integer quantity = 2;
        BigDecimal price = new BigDecimal("100.00");
        
        OrderItemCreateRequest itemReq = new OrderItemCreateRequest(productId, quantity);
        OrderCreateRequest request = new OrderCreateRequest(customerId, addressId, Arrays.asList(itemReq));

        Customer customer = Customer.builder().id(customerId).status(CustomerStatus.ACTIVE).build();
        Address address = Address.builder().id(addressId).customer(customer).build();
        Product product = Product.builder().id(productId).name("P1").price(price).active(true).build();
        
        Order savedOrder = Order.builder().id(1L).customer(customer).shippingAddress(address).status(OrderStatus.CREATED).total(new BigDecimal("200.00")).items(new ArrayList<>()).build();
        OrderResponse expectedResponse = new OrderResponse(1L, customerId, addressId, OrderStatus.CREATED, new BigDecimal("200.00"), null, null);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        when(orderMapper.toResponse(savedOrder)).thenReturn(expectedResponse);

        // When
        OrderResponse actualResponse = orderService.create(request);

        // Then
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.id(), actualResponse.id());
        assertEquals(new BigDecimal("200.00"), actualResponse.total());
        verify(customerRepository, times(1)).findById(customerId);
        verify(addressRepository, times(1)).findById(addressId);
        verify(productRepository, times(1)).findById(productId);
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderStatusHistoryService, times(1)).addHistory(anyLong(), anyString());
        verify(orderMapper, times(1)).toResponse(savedOrder);
    }

    @Test
    void createOrder_shouldThrow_whenCustomerDoesNotExist() {
        // Given
        Long customerId = 1L;
        OrderCreateRequest request = new OrderCreateRequest(customerId, 1L, null);
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> orderService.create(request));
        verify(customerRepository, times(1)).findById(customerId);
        verify(orderRepository, never()).save(any());
    }

    @Test
    void createOrder_shouldThrow_whenProductDoesNotExist() {
        // Given
        Long customerId = 1L;
        Long addressId = 1L;
        Long productId = 1L;
        OrderItemCreateRequest itemReq = new OrderItemCreateRequest(productId, 2);
        OrderCreateRequest request = new OrderCreateRequest(customerId, addressId, Arrays.asList(itemReq));

        Customer customer = Customer.builder().id(customerId).status(CustomerStatus.ACTIVE).build();
        Address address = Address.builder().id(addressId).customer(customer).build();

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> orderService.create(request));
        verify(productRepository, times(1)).findById(productId);
        verify(orderRepository, never()).save(any());
    }

    @Test
    void getOrder_shouldReturnResponse_whenExists() {
        // Given
        Long orderId = 1L;
        Order orderEntity = Order.builder().id(orderId).build();
        OrderResponse expectedResponse = new OrderResponse(orderId, 1L, 1L, OrderStatus.CREATED, null, null, null);

        when(orderRepository.findByIdWithItems(orderId)).thenReturn(Optional.of(orderEntity));
        when(orderMapper.toResponse(orderEntity)).thenReturn(expectedResponse);

        // When
        OrderResponse actualResponse = orderService.get(orderId);

        // Then
        assertEquals(orderId, actualResponse.id());
        verify(orderRepository, times(1)).findByIdWithItems(orderId);
        verify(orderMapper, times(1)).toResponse(orderEntity);
    }

    @Test
    void getOrder_shouldThrow_whenDoesNotExist() {
        // Given
        Long orderId = 1L;
        when(orderRepository.findByIdWithItems(orderId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> orderService.get(orderId));
        verify(orderRepository, times(1)).findByIdWithItems(orderId);
    }

    @Test
    void getAllByCustomerId_shouldReturnListOfResponses() {
        // Given
        Long customerId = 1L;
        Order o1 = Order.builder().id(1L).build();
        List<Order> orders = Arrays.asList(o1);
        OrderResponse r1 = new OrderResponse(1L, customerId, 1L, null, null, null, null);

        when(orderRepository.findByCustomerId(customerId)).thenReturn(orders);
        when(orderMapper.toResponse(o1)).thenReturn(r1);

        // When
        List<OrderResponse> responses = orderService.getAllByCustomerId(customerId);

        // Then
        assertEquals(1, responses.size());
        verify(orderRepository, times(1)).findByCustomerId(customerId);
    }

    @Test
    void cancelOrder_shouldUpdateStatusAndReturnStock_whenOrderCanBeCancelled() {
        // Given
        Long orderId = 1L;
        Long productId = 1L;
        Product product = Product.builder().id(productId).build();
        OrderItem item = OrderItem.builder().product(product).quantity(5).build();
        Order order = Order.builder().id(orderId).status(OrderStatus.PAID).items(new ArrayList<>(Arrays.asList(item))).build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // When
        orderService.cancel(orderId);

        // Then
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
        verify(orderRepository, times(1)).findById(orderId);
        verify(inventoryService, times(1)).updateStock(any(UpdateStockRequest.class));
        verify(orderRepository, times(1)).save(order);
        verify(orderStatusHistoryService, times(1)).addHistory(eq(orderId), anyString());
    }

    @Test
    void cancelOrder_shouldThrow_whenOrderCannotBeCancelled() {
        // Given
        Long orderId = 1L;
        Order order = Order.builder().id(orderId).status(OrderStatus.DELIVERED).build();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // When & Then
        assertThrows(RuntimeException.class, () -> orderService.cancel(orderId));
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, never()).save(any());
    }
}
