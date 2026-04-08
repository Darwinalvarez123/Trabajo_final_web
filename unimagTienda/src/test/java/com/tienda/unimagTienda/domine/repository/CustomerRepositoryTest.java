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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomerRepositoryTest extends AbstractRepositoryIT {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Test
    @DisplayName("Debe buscar cliente por email")
    void shouldFindByEmail() {
        Customer customer = Customer.builder()
                .firstName("Juan")
                .lastName("Perez")
                .email("juan@test.com")
                .status(CustomerStatus.ACTIVE)
                .build();
        customerRepository.save(customer);

        Optional<Customer> found = customerRepository.findByEmail("juan@test.com");

        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("juan@test.com");
    }

    @Test
    @DisplayName("Debe buscar cliente por teléfono")
    void shouldFindByPhone() {
        Customer customer = Customer.builder()
                .firstName("Maria")
                .lastName("Gomez")
                .email("maria@test.com")
                .phone("123456789")
                .status(CustomerStatus.ACTIVE)
                .build();
        customerRepository.save(customer);

        Optional<Customer> found = customerRepository.findByPhone("123456789");

        assertThat(found).isPresent();
        assertThat(found.get().getPhone()).isEqualTo("123456789");
    }

    @Test
    @DisplayName("Debe encontrar clientes con órdenes válidas")
    void shouldFindCustomersWithValidOrders() {
        Customer customer = customerRepository.save(Customer.builder()
                .firstName("Carlos")
                .lastName("Ruiz")
                .email("carlos@test.com")
                .status(CustomerStatus.ACTIVE)
                .build());

        Address address = addressRepository.save(Address.builder()
                .street("Calle Test")
                .city("Santa Marta")
                .state("Magdalena")
                .postalCode("470001")
                .country("Colombia")
                .customer(customer)
                .build());

        orderRepository.save(Order.builder()
                .customer(customer)
                .shippingAddress(address)
                .status(OrderStatus.PAID)
                .total(BigDecimal.valueOf(100))
                .build());

        List<Customer> customers = customerRepository.findCustomersWithValidOrders();

        assertThat(customers).isNotEmpty();
        assertThat(customers).extracting(Customer::getEmail).contains("carlos@test.com");
    }
}
