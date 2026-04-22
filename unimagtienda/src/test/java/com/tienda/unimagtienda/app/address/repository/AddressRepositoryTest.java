package com.tienda.unimagtienda.app.address.repository;

import com.tienda.unimagtienda.app.address.entity.Address;
import com.tienda.unimagtienda.app.customer.entity.Customer;
import com.tienda.unimagtienda.app.customer.repository.CustomerRepository;
import com.tienda.unimagtienda.shared.AbstractRepositoryIT;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class AddressRepositoryTest extends AbstractRepositoryIT {

    @Autowired AddressRepository addressRepository;
    @Autowired CustomerRepository customerRepository;

    @Test
    @DisplayName("Address: guarda y consulta por customerId y por id")
    void shouldSaveAndFindAddress() {

        // Given
        var customer = Customer.builder()
                .firstName("Juan")
                .lastName("Perez")
                .email("juan@test.com")
                .build();

        customer = customerRepository.save(customer);

        var address = Address.builder()
                .street("Calle 123")
                .city("Santa Marta")
                .state("Magdalena")
                .postalCode("470001")
                .country("Colombia")
                .customer(customer)
                .build();

        address = addressRepository.save(address);

        // When
        List<Address> addressesByCustomer =
                addressRepository.findByCustomerId(customer.getId());

        var foundById =
                addressRepository.findById(address.getId());

        // Then
        assertThat(addressesByCustomer).isNotEmpty();
        assertThat(addressesByCustomer.getFirst().getStreet()).isEqualTo("Calle 123");
        assertThat(addressesByCustomer.getFirst().getCity()).isEqualTo("Santa Marta");
        assertThat(addressesByCustomer.getFirst().getCustomer().getId())
                .isEqualTo(customer.getId());

        assertThat(foundById).isPresent();
        assertThat(foundById.get().getStreet()).isEqualTo("Calle 123");
        assertThat(foundById.get().getCity()).isEqualTo("Santa Marta");
    }
}