package com.tienda.unimagtienda.app.orderStatusHistory.repository;

import com.tienda.unimagtienda.app.address.entity.Address;
import com.tienda.unimagtienda.app.address.repository.AddressRepository;
import com.tienda.unimagtienda.app.customer.entity.Customer;
import com.tienda.unimagtienda.app.customer.repository.CustomerRepository;
import com.tienda.unimagtienda.app.order.entity.Order;
import com.tienda.unimagtienda.app.order.enums.OrderStatus;
import com.tienda.unimagtienda.app.order.repository.OrderRepository;
import com.tienda.unimagtienda.app.orderStatusHistory.entity.OrderStatusHistory;
import com.tienda.unimagtienda.shared.AbstractRepositoryIT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderStatusHistoryRepositoryTest extends AbstractRepositoryIT {

    @Autowired
    private OrderStatusHistoryRepository statusHistoryRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AddressRepository addressRepository;

    private Order order;

    @BeforeEach
    void setUp() {
        Customer customer = customerRepository.save(Customer.builder()
                .firstName("John")
                .lastName("Doe")
                .email("history.test@example.com")
                .build());

        Address address = addressRepository.save(Address.builder()
                .street("Main St")
                .city("City")
                .state("State")
                .postalCode("12345")
                .country("Country")
                .customer(customer)
                .build());

        order = orderRepository.save(Order.builder()
                .customer(customer)
                .shippingAddress(address)
                .status(OrderStatus.CREATED)
                .total(new BigDecimal("100.00"))
                .build());
    }

    @Test
    @DisplayName("OrderStatusHistory: guarda y busca por orderId")
    void shouldSaveAndFindByOrderId() {
        // Given
        OrderStatusHistory history = OrderStatusHistory.builder()
                .order(order)
                .status(OrderStatus.CREATED)
                .comment("Order created")
                .build();

        statusHistoryRepository.save(history);

        // When
        List<OrderStatusHistory> results = statusHistoryRepository.findByOrderId(order.getId());

        // Then
        assertThat(results).isNotEmpty();
        assertThat(results.get(0).getOrder().getId()).isEqualTo(order.getId());
    }
}
