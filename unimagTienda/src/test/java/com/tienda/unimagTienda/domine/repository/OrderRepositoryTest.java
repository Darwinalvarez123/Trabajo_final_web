package com.tienda.unimagTienda.domine.repository;

import com.tienda.unimagTienda.domine.entity.Address;
import com.tienda.unimagTienda.domine.entity.Customer;
import com.tienda.unimagTienda.domine.entity.Order;
import com.tienda.unimagTienda.domine.enums.CustomerStatus;
import com.tienda.unimagTienda.domine.enums.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderRepositoryTest extends AbstractRepositoryIT {

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
    @DisplayName("Debe encontrar órdenes por cliente")
    void shouldFindByCustomer() {
        Customer customer = customerRepository.save(Customer.builder()
                .firstName("Elena")
                .lastName("Blanco")
                .email("elena@test.com")
                .status(CustomerStatus.ACTIVE)
                .build());

        Address address = createAndSaveAddress(customer);

        orderRepository.save(Order.builder()
                .customer(customer)
                .shippingAddress(address)
                .status(OrderStatus.PAID)
                .total(BigDecimal.valueOf(200))
                .build());

        List<Order> orders = orderRepository.findByCustomer(customer);

        assertThat(orders).hasSize(1);
        assertThat(orders.get(0).getCustomer().getEmail()).isEqualTo("elena@test.com");
    }

    @Test
    @DisplayName("Debe filtrar órdenes por estado y rango de total")
    void shouldFindOrdersWithFilters() {
        Customer customer = customerRepository.save(Customer.builder().firstName("X").lastName("Y").email("xy@test.com").status(CustomerStatus.ACTIVE).build());
        Address address = createAndSaveAddress(customer);

        orderRepository.save(Order.builder().customer(customer).shippingAddress(address).status(OrderStatus.PAID).total(BigDecimal.valueOf(150)).build());
        orderRepository.save(Order.builder().customer(customer).shippingAddress(address).status(OrderStatus.CREATED).total(BigDecimal.valueOf(50)).build());

        List<Order> filtered = orderRepository.findOrdersWithFilters(null, OrderStatus.PAID, null, null, BigDecimal.valueOf(100), BigDecimal.valueOf(200));

        assertThat(filtered).hasSize(1);
        assertThat(filtered.get(0).getStatus()).isEqualTo(OrderStatus.PAID);
    }

    @Test
    @DisplayName("Debe obtener clientes con mayor facturación")
    void shouldFindTopCustomers() {
        Customer c1 = customerRepository.save(Customer.builder().firstName("John").lastName("Doe").email("john@doe.com").status(CustomerStatus.ACTIVE).build());
        Customer c2 = customerRepository.save(Customer.builder().firstName("Jane").lastName("Doe").email("jane@doe.com").status(CustomerStatus.ACTIVE).build());
        
        Address a1 = createAndSaveAddress(c1);
        Address a2 = createAndSaveAddress(c2);

        orderRepository.save(Order.builder().customer(c1).shippingAddress(a1).status(OrderStatus.PAID).total(new BigDecimal("1000.00")).build());
        orderRepository.save(Order.builder().customer(c2).shippingAddress(a2).status(OrderStatus.PAID).total(new BigDecimal("500.00")).build());

        List<Object[]> results = orderRepository.findTopCustomers();

        assertThat(results).isNotEmpty();
        assertThat(results.get(0)[0]).isEqualTo("John");
        assertThat(new BigDecimal(results.get(0)[2].toString())).isEqualByComparingTo(new BigDecimal("1000.00"));
    }

    @Test
    @DisplayName("Debe obtener ingresos mensuales")
    void shouldFindMonthlyIncome() {
        Customer c = customerRepository.save(Customer.builder().firstName("A").lastName("B").email("a@b.com").status(CustomerStatus.ACTIVE).build());
        Address a = createAndSaveAddress(c);

        orderRepository.save(Order.builder().customer(c).shippingAddress(a).status(OrderStatus.PAID).total(new BigDecimal("100.00")).build());

        List<Object[]> results = orderRepository.findMonthlyIncome();

        assertThat(results).isNotEmpty();
        assertThat(new BigDecimal(results.get(0)[1].toString())).isEqualByComparingTo(new BigDecimal("100.00"));
    }
}
