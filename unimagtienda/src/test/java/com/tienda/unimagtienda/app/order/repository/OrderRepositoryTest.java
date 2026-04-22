package com.tienda.unimagtienda.app.order.repository;

import com.tienda.unimagtienda.app.address.entity.Address;
import com.tienda.unimagtienda.app.address.repository.AddressRepository;
import com.tienda.unimagtienda.app.customer.entity.Customer;
import com.tienda.unimagtienda.app.customer.repository.CustomerRepository;
import com.tienda.unimagtienda.app.order.entity.Order;
import com.tienda.unimagtienda.app.order.enums.OrderStatus;
import com.tienda.unimagtienda.shared.AbstractRepositoryIT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderRepositoryTest extends AbstractRepositoryIT {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AddressRepository addressRepository;

    private Customer customer;
    private Address address;

    @BeforeEach
    void setUp() {
        customer = customerRepository.save(Customer.builder()
                .firstName("John")
                .lastName("Doe")
                .email("order.test@example.com")
                .build());

        address = addressRepository.save(Address.builder()
                .street("Main St")
                .city("City")
                .state("State")
                .postalCode("12345")
                .country("Country")
                .customer(customer)
                .build());
    }

    @Test
    @DisplayName("Order: guarda y busca por customerId")
    void shouldSaveAndFindByCustomerId() {
        // Given
        Order order = Order.builder()
                .customer(customer)
                .shippingAddress(address)
                .status(OrderStatus.CREATED)
                .total(new BigDecimal("100.00"))
                .build();

        orderRepository.save(order);

        // When
        List<Order> orders = orderRepository.findByCustomerId(customer.getId());

        // Then
        assertThat(orders).isNotEmpty();
        assertThat(orders.get(0).getCustomer().getId()).isEqualTo(customer.getId());
    }

    @Test
    @DisplayName("Order: busca por estado")
    void shouldFindByStatus() {
        // Given
        orderRepository.save(Order.builder()
                .customer(customer)
                .shippingAddress(address)
                .status(OrderStatus.CREATED)
                .total(new BigDecimal("100.00"))
                .build());

        // When
        List<Order> orders = orderRepository.findByStatus(OrderStatus.CREATED);

        // Then
        assertThat(orders).isNotEmpty();
        assertThat(orders.get(0).getStatus()).isEqualTo(OrderStatus.CREATED);
    }

    @Test
    @DisplayName("Order: busca por fecha entre rangos")
    void shouldFindByCreatedAtBetween() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        orderRepository.save(Order.builder()
                .customer(customer)
                .shippingAddress(address)
                .status(OrderStatus.CREATED)
                .total(new BigDecimal("100.00"))
                .build());

        // When
        List<Order> orders = orderRepository.findByCreatedAtBetween(now.minusDays(1), now.plusDays(1));

        // Then
        assertThat(orders).isNotEmpty();
    }
}
