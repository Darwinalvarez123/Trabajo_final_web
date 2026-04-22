package com.tienda.unimagtienda.app.address.service;

import com.tienda.unimagtienda.app.address.dto.AddressResponse;
import com.tienda.unimagtienda.app.address.dto.CreateAddressRequest;
import com.tienda.unimagtienda.app.address.dto.UpdateAddressRequest;
import com.tienda.unimagtienda.app.address.entity.Address;
import com.tienda.unimagtienda.app.address.mapper.AddressMapper;
import com.tienda.unimagtienda.app.address.repository.AddressRepository;
import com.tienda.unimagtienda.app.customer.entity.Customer;
import com.tienda.unimagtienda.app.customer.repository.CustomerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceImpTest {

    @Mock
    private AddressRepository repo;

    @Mock
    private CustomerRepository customerRepository;

    @Spy
    private AddressMapper mapper = Mappers.getMapper(AddressMapper.class);

    @InjectMocks
    private AddressServiceImp service;

    @Test
    @DisplayName("AddressService: crea y retorna respuesta")
    void shouldCreateAndReturnResponse() {
        // Given
        var req = new CreateAddressRequest(
                "Calle 123", "Santa Marta", "Magdalena", "470001", "Colombia", 1L
        );
        var customer = Customer.builder().id(1L).email("test@test.com").build();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(repo.save(any(Address.class))).thenAnswer(inv -> {
            Address a = inv.getArgument(0);
            a.setId(10L);
            return a;
        });

        // When
        AddressResponse res = service.create(req);

        // Then
        assertThat(res.id()).isEqualTo(10L);
        assertThat(res.street()).isEqualTo("Calle 123");
        assertThat(res.customerId()).isEqualTo(1L);
        verify(repo).save(any(Address.class));
    }

    @Test
    @DisplayName("AddressService: actualiza dirección")
    void shouldUpdateAddress() {
        // Given
        var customer = Customer.builder().id(1L).build();
        var entity = Address.builder()
                .id(5L)
                .street("Old Street")
                .city("Old City")
                .state("Old State")
                .postalCode("00000")
                .country("Old Country")
                .customer(customer)
                .build();

        var updateReq = new UpdateAddressRequest(
                "New Street",
                "New City",
                "New State",
                "11111",
                "New Country"
        );

        when(repo.findById(5L)).thenReturn(Optional.of(entity));
        when(repo.save(any(Address.class))).thenReturn(entity);

        // When
        AddressResponse res = service.update(5L, updateReq);

        // Then
        assertThat(res.street()).isEqualTo("New Street");
        assertThat(res.city()).isEqualTo("New City");
        assertThat(res.state()).isEqualTo("New State"); // ✅ cambiado
        verify(repo).save(entity);
    }

    @Test
    @DisplayName("AddressService: obtiene por ID")
    void shouldGetById() {
        // Given
        var customer = Customer.builder().id(1L).build();
        var entity = Address.builder()
                .id(1L)
                .street("Street")
                .customer(customer)
                .build();

        when(repo.findById(1L)).thenReturn(Optional.of(entity));

        // When
        AddressResponse res = service.getById(1L);

        // Then
        assertThat(res.id()).isEqualTo(1L);
        assertThat(res.street()).isEqualTo("Street");
    }
}