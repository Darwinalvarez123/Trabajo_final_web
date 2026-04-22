package com.tienda.unimagtienda.app.customer.service;

import com.tienda.unimagtienda.app.customer.dto.CreateCustomerRequest;
import com.tienda.unimagtienda.app.customer.dto.UpdateCustomerRequest;
import com.tienda.unimagtienda.app.customer.entity.Customer;
import com.tienda.unimagtienda.app.customer.enums.CustomerStatus;
import com.tienda.unimagtienda.app.customer.mapper.CustomerMapper;
import com.tienda.unimagtienda.app.customer.repository.CustomerRepository;
import com.tienda.unimagtienda.exception.ConflictException;
import com.tienda.unimagtienda.exception.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository repository;

    @Spy
    private CustomerMapper mapper = Mappers.getMapper(CustomerMapper.class);

    @InjectMocks
    private CustomerServiceImpl service;


    @Test
    @DisplayName("create: guarda correctamente")
    void shouldCreateCustomer() {
        var req = new CreateCustomerRequest("John", "Doe", "john@test.com");

        when(repository.existsByEmail("john@test.com")).thenReturn(false);
        when(repository.save(any(Customer.class))).thenAnswer(inv -> {
            Customer c = inv.getArgument(0);
            c.setId(1L);
            c.setStatus(CustomerStatus.ACTIVE);
            return c;
        });

        var res = service.create(req);

        assertThat(res.id()).isEqualTo(1L);
        assertThat(res.email()).isEqualTo("john@test.com");

        verify(repository).save(any(Customer.class));
    }

    @Test
    @DisplayName("create: error si email existe")
    void shouldThrowIfEmailExists() {
        var req = new CreateCustomerRequest("John", "Doe", "john@test.com");

        when(repository.existsByEmail("john@test.com")).thenReturn(true);

        assertThatThrownBy(() -> service.create(req))
                .isInstanceOf(ConflictException.class);
    }



    @Test
    @DisplayName("getById: retorna cliente activo")
    void shouldGetActiveCustomer() {
        var entity = Customer.builder()
                .id(1L)
                .email("test@test.com")
                .status(CustomerStatus.ACTIVE)
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        var res = service.getById(1L);

        assertThat(res.id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("getById: error si no existe")
    void shouldThrowIfNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("getById: error si está INACTIVE")
    void shouldThrowIfInactive() {
        var entity = Customer.builder()
                .id(1L)
                .status(CustomerStatus.INACTIVE)
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        assertThatThrownBy(() -> service.getById(1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // ---------- GET ALL ----------

    @Test
    @DisplayName("getAll: solo retorna activos")
    void shouldReturnOnlyActive() {
        var active = Customer.builder().id(1L).status(CustomerStatus.ACTIVE).build();
        var inactive = Customer.builder().id(2L).status(CustomerStatus.INACTIVE).build();

        when(repository.findAll()).thenReturn(List.of(active, inactive));

        var res = service.getAll();

        assertThat(res).hasSize(1);
        assertThat(res.get(0).id()).isEqualTo(1L);
    }



    @Test
    @DisplayName("update: actualiza correctamente")
    void shouldUpdateCustomer() {
        var entity = Customer.builder()
                .id(1L)
                .email("old@test.com")
                .status(CustomerStatus.ACTIVE)
                .build();

        var req = new UpdateCustomerRequest("New", "Name", "new@test.com");

        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.existsByEmail("new@test.com")).thenReturn(false);
        when(repository.save(any(Customer.class))).thenReturn(entity);

        var res = service.update(1L, req);

        assertThat(res.email()).isEqualTo("new@test.com");

        verify(repository).save(entity);
    }

    @Test
    @DisplayName("update: error si email ya existe")
    void shouldThrowIfEmailExistsOnUpdate() {
        var entity = Customer.builder()
                .id(1L)
                .email("old@test.com")
                .status(CustomerStatus.ACTIVE)
                .build();

        var req = new UpdateCustomerRequest(null, null, "new@test.com");

        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.existsByEmail("new@test.com")).thenReturn(true);

        assertThatThrownBy(() -> service.update(1L, req))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    @DisplayName("update: error si está INACTIVE")
    void shouldThrowIfInactiveOnUpdate() {
        var entity = Customer.builder()
                .id(1L)
                .status(CustomerStatus.INACTIVE)
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        var req = new UpdateCustomerRequest("A", "B", "c@test.com");

        assertThatThrownBy(() -> service.update(1L, req))
                .isInstanceOf(ResourceNotFoundException.class);
    }



    @Test
    @DisplayName("delete: marca como INACTIVE")
    void shouldSoftDelete() {
        var entity = Customer.builder()
                .id(1L)
                .status(CustomerStatus.ACTIVE)
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        service.delete(1L);

        assertThat(entity.getStatus()).isEqualTo(CustomerStatus.INACTIVE);
        verify(repository).save(entity);
    }
}