package com.tienda.unimagTienda.domine.repository;

import com.tienda.unimagTienda.domine.entity.Address;
import com.tienda.unimagTienda.domine.entity.Customer;
import com.tienda.unimagTienda.domine.enums.CustomerStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class AddressRepositoryTest extends AbstractRepositoryIT {

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @Test
    @DisplayName("Debe encontrar direcciones por ID de cliente")
    void shouldFindByCustomerId() {
        // Given
        Customer customer = customerRepository.save(Customer.builder()
                .firstName("Juan")
                .lastName("Perez")
                .email("juan@email.com")
                .status(CustomerStatus.ACTIVE)
                .build());

        addressRepository.save(Address.builder()
                .street("Calle 1")
                .city("Santa Marta")
                .state("Magdalena")
                .postalCode("470001")
                .country("Colombia")
                .customer(customer)
                .build());

        addressRepository.save(Address.builder()
                .street("Calle 2")
                .city("Santa Marta")
                .state("Magdalena")
                .postalCode("470002")
                .country("Colombia")
                .customer(customer)
                .build());

        // When
        List<Address> addresses = addressRepository.findByCustomerId(customer.getId());

        // Then
        assertThat(addresses).hasSize(2);
        assertThat(addresses).extracting(Address::getStreet).containsExactlyInAnyOrder("Calle 1", "Calle 2");
    }

    @Test
    @DisplayName("Debe encontrar dirección por ID y cliente")
    void shouldFindByIdAndCustomerId() {
        // Given
        Customer customer = customerRepository.save(Customer.builder()
                .firstName("Maria")
                .lastName("Lopez")
                .email("maria@email.com")
                .status(CustomerStatus.ACTIVE)
                .build());

        Address address = addressRepository.save(Address.builder()
                .street("Avenida Principal")
                .city("Bogota")
                .state("Cundinamarca")
                .postalCode("110111")
                .country("Colombia")
                .customer(customer)
                .build());

        // When
        Optional<Address> foundAddress = addressRepository.findByIdAndCustomerId(address.getId(), customer.getId());

        // Then
        assertThat(foundAddress).isPresent();
        assertThat(foundAddress.get().getStreet()).isEqualTo("Avenida Principal");
        assertThat(foundAddress.get().getCustomer().getFirstName()).isEqualTo("Maria");
    }

    @Test
    @DisplayName("Debe retornar vacío cuando no existe la dirección para ese cliente")
    void shouldReturnEmptyWhenNotFound() {
        // Given
        Customer customer = customerRepository.save(Customer.builder()
                .firstName("Carlos")
                .lastName("Gomez")
                .email("carlos@email.com")
                .status(CustomerStatus.ACTIVE)
                .build());

        // When
        Optional<Address> address = addressRepository.findByIdAndCustomerId(999L, customer.getId());

        // Then
        assertThat(address).isEmpty();
    }
}
