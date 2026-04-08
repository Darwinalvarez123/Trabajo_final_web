package com.tienda.unimagTienda.domine.repository;

import com.tienda.unimagTienda.domine.entity.*;
import com.tienda.unimagTienda.domine.enums.CustomerStatus;
import com.tienda.unimagTienda.domine.enums.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderStatusHistoryRepositoryTest extends AbstractRepositoryIT {

    @Autowired
    private OrderStatusHistoryRepository orderStatusHistoryRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AddressRepository addressRepository;

    private Address createAndSaveAddress(Customer customer) {
        return addressRepository.save(Address.builder()
                .street("Main St")
                .city("Santa Marta")
                .state("Magdalena")
                .postalCode("470001")
                .country("Colombia")
                .customer(customer)
                .build());
    }

    @Test
    @DisplayName("Debe encontrar historial por ID de orden ordenado por fecha")
    void shouldFindByOrderIdOrderByCreatedAtAsc() {
        Customer customer = customerRepository.save(Customer.builder().firstName("H").lastName("S").email("hist@test.com").status(CustomerStatus.ACTIVE).build());
        Address address = createAndSaveAddress(customer);
        Order order = orderRepository.save(Order.builder().customer(customer).shippingAddress(address).status(OrderStatus.PENDING).total(BigDecimal.ZERO).build());

        orderStatusHistoryRepository.save(OrderStatusHistory.builder().order(order).status(OrderStatus.PENDING).build());
        orderStatusHistoryRepository.save(OrderStatusHistory.builder().order(order).status(OrderStatus.PAID).build());

        List<OrderStatusHistory> history = orderStatusHistoryRepository.findByOrderIdOrderByCreatedAtAsc(order.getId());

        assertThat(history).hasSize(2);
        assertThat(history.get(0).getStatus()).isEqualTo(OrderStatus.PENDING);
        assertThat(history.get(1).getStatus()).isEqualTo(OrderStatus.PAID);
    }

    @Test
    @DisplayName("Debe encontrar historial por estado")
    void shouldFindByStatus() {
        Customer customer = customerRepository.save(Customer.builder().firstName("H2").lastName("S2").email("hist2@test.com").status(CustomerStatus.ACTIVE).build());
        Address address = createAndSaveAddress(customer);
        Order order = orderRepository.save(Order.builder().customer(customer).shippingAddress(address).status(OrderStatus.SHIPPED).total(BigDecimal.ZERO).build());

        orderStatusHistoryRepository.save(OrderStatusHistory.builder().order(order).status(OrderStatus.SHIPPED).build());

        List<OrderStatusHistory> histories = orderStatusHistoryRepository.findByStatus(OrderStatus.SHIPPED);

        assertThat(histories).isNotEmpty();
        assertThat(histories.get(0).getStatus()).isEqualTo(OrderStatus.SHIPPED);
    }
}
