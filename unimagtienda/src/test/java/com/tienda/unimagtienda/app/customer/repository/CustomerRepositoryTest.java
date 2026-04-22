package com.tienda.unimagtienda.app.customer.repository;

import com.tienda.unimagtienda.app.customer.entity.Customer;
import com.tienda.unimagtienda.app.customer.enums.CustomerStatus;
import com.tienda.unimagtienda.shared.AbstractRepositoryIT;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerRepositoryTest extends AbstractRepositoryIT {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    @DisplayName("Customer: guarda y valida existencia por email")
    void shouldSaveAndCheckExistsByEmail() {
        // Given
        String email = "test@example.com";
        Customer customer = Customer.builder()
                .firstName("John")
                .lastName("Doe")
                .email(email)
                .status(CustomerStatus.ACTIVE)
                .build();

        customerRepository.save(customer);

        // When
        boolean exists = customerRepository.existsByEmail(email);

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Customer: encuentra por estado")
    void shouldFindByStatus() {
        // Given
        customerRepository.save(Customer.builder()
                .firstName("Active")
                .lastName("User")
                .email("active@example.com")
                .status(CustomerStatus.ACTIVE)
                .build());

        customerRepository.save(Customer.builder()
                .firstName("Inactive")
                .lastName("User")
                .email("inactive@example.com")
                .status(CustomerStatus.INACTIVE)
                .build());

        // When
        List<Customer> activeCustomers = customerRepository.findByStatus(CustomerStatus.ACTIVE);

        // Then
        assertThat(activeCustomers).isNotEmpty();
        assertThat(activeCustomers).allMatch(c -> c.getStatus() == CustomerStatus.ACTIVE);
    }
}
