package com.tienda.unimagTienda.domine.service;

import com.tienda.unimagTienda.api.dto.CustomerDto.*;
import com.tienda.unimagTienda.domine.entity.Customer;
import com.tienda.unimagTienda.domine.enums.CustomerStatus;
import com.tienda.unimagTienda.domine.repository.CustomerRepository;
import com.tienda.unimagTienda.domine.service.mapper.CustomerMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCustomer_shouldReturnCustomerResponse() {
        // Given
        CreateCustomerRequest request = new CreateCustomerRequest("John", "Doe", "john.doe@example.com", "1234567890", CustomerStatus.ACTIVE);
        Customer customerEntity = Customer.builder()
                .firstName("John").lastName("Doe").email("john.doe@example.com").phone("1234567890").status(CustomerStatus.ACTIVE)
                .build();
        Customer savedCustomerEntity = Customer.builder()
                .id(1L).firstName("John").lastName("Doe").email("john.doe@example.com").phone("1234567890").status(CustomerStatus.ACTIVE)
                .build();
        CustomerResponse expectedResponse = new CustomerResponse(1L, "John", "Doe", "john.doe@example.com", "1234567890", CustomerStatus.ACTIVE);

        when(customerMapper.toEntity(request)).thenReturn(customerEntity);
        when(customerRepository.save(customerEntity)).thenReturn(savedCustomerEntity);
        when(customerMapper.toResponse(savedCustomerEntity)).thenReturn(expectedResponse);

        // When
        CustomerResponse actualResponse = customerService.create(request);

        // Then
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.id(), actualResponse.id());
        assertEquals(expectedResponse.email(), actualResponse.email());
        verify(customerMapper, times(1)).toEntity(request);
        verify(customerRepository, times(1)).save(customerEntity);
        verify(customerMapper, times(1)).toResponse(savedCustomerEntity);
    }

    @Test
    void getCustomer_shouldReturnCustomerResponse_whenCustomerExists() {
        // Given
        Long customerId = 1L;
        Customer customerEntity = Customer.builder()
                .id(customerId).firstName("John").lastName("Doe").email("john.doe@example.com").phone("1234567890").status(CustomerStatus.ACTIVE)
                .build();
        CustomerResponse expectedResponse = new CustomerResponse(customerId, "John", "Doe", "john.doe@example.com", "1234567890", CustomerStatus.ACTIVE);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customerEntity));
        when(customerMapper.toResponse(customerEntity)).thenReturn(expectedResponse);

        // When
        CustomerResponse actualResponse = customerService.get(customerId);

        // Then
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.id(), actualResponse.id());
        verify(customerRepository, times(1)).findById(customerId);
        verify(customerMapper, times(1)).toResponse(customerEntity);
    }

    @Test
    void getCustomer_shouldThrowRuntimeException_whenCustomerDoesNotExist() {
        // Given
        Long customerId = 1L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> customerService.get(customerId));
        assertEquals("Customer not found with id: " + customerId, exception.getMessage());
        verify(customerRepository, times(1)).findById(customerId);
        verify(customerMapper, never()).toResponse(any());
    }

    @Test
    void updateCustomer_shouldReturnUpdatedCustomerResponse_whenCustomerExists() {
        // Given
        Long customerId = 1L;
        UpdateCustomerRequest request = new UpdateCustomerRequest("Jane", "Smith", "jane.smith@example.com", "0987654321", CustomerStatus.INACTIVE);
        Customer existingCustomer = Customer.builder()
                .id(customerId).firstName("John").lastName("Doe").email("john.doe@example.com").phone("1234567890").status(CustomerStatus.ACTIVE)
                .build();
        Customer updatedCustomer = Customer.builder()
                .id(customerId).firstName("Jane").lastName("Smith").email("jane.smith@example.com").phone("0987654321").status(CustomerStatus.INACTIVE)
                .build();
        CustomerResponse expectedResponse = new CustomerResponse(customerId, "Jane", "Smith", "jane.smith@example.com", "0987654321", CustomerStatus.INACTIVE);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(updatedCustomer);
        when(customerMapper.toResponse(updatedCustomer)).thenReturn(expectedResponse);

        // When
        CustomerResponse actualResponse = customerService.update(customerId, request);

        // Then
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.id(), actualResponse.id());
        assertEquals(expectedResponse.email(), actualResponse.email());
        assertEquals(expectedResponse.firstName(), actualResponse.firstName());
        assertEquals(expectedResponse.status(), actualResponse.status());
        verify(customerRepository, times(1)).findById(customerId);
        verify(customerRepository, times(1)).save(existingCustomer); // Verify that the existing customer object was saved
        verify(customerMapper, times(1)).toResponse(updatedCustomer);
    }

    @Test
    void updateCustomer_shouldThrowRuntimeException_whenCustomerDoesNotExist() {
        // Given
        Long customerId = 1L;
        UpdateCustomerRequest request = new UpdateCustomerRequest("Jane", "Smith", "jane.smith@example.com", "0987654321", CustomerStatus.INACTIVE);
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> customerService.update(customerId, request));
        assertEquals("Customer not found with id: " + customerId, exception.getMessage());
        verify(customerRepository, times(1)).findById(customerId);
        verify(customerRepository, never()).save(any(Customer.class));
        verify(customerMapper, never()).toResponse(any());
    }

    @Test
    void getAllCustomers_shouldReturnListOfCustomerResponses() {
        // Given
        Customer customer1 = Customer.builder().id(1L).firstName("John").email("john@example.com").status(CustomerStatus.ACTIVE).build();
        Customer customer2 = Customer.builder().id(2L).firstName("Jane").email("jane@example.com").status(CustomerStatus.INACTIVE).build();
        List<Customer> customerEntities = Arrays.asList(customer1, customer2);

        CustomerResponse response1 = new CustomerResponse(1L, "John", null, "john@example.com", null, CustomerStatus.ACTIVE);
        CustomerResponse response2 = new CustomerResponse(2L, "Jane", null, "jane@example.com", null, CustomerStatus.INACTIVE);
        List<CustomerResponse> expectedResponses = Arrays.asList(response1, response2);

        when(customerRepository.findAll()).thenReturn(customerEntities);
        when(customerMapper.toResponse(customer1)).thenReturn(response1);
        when(customerMapper.toResponse(customer2)).thenReturn(response2);

        // When
        List<CustomerResponse> actualResponses = customerService.getAll();

        // Then
        assertNotNull(actualResponses);
        assertEquals(2, actualResponses.size());
        assertEquals(expectedResponses.get(0).id(), actualResponses.get(0).id());
        assertEquals(expectedResponses.get(1).id(), actualResponses.get(1).id());
        verify(customerRepository, times(1)).findAll();
        verify(customerMapper, times(2)).toResponse(any(Customer.class));
    }

    @Test
    void deleteCustomer_shouldDeleteCustomer_whenCustomerExists() {
        // Given
        Long customerId = 1L;
        when(customerRepository.existsById(customerId)).thenReturn(true);
        doNothing().when(customerRepository).deleteById(customerId);

        // When
        customerService.delete(customerId);

        // Then
        verify(customerRepository, times(1)).existsById(customerId);
        verify(customerRepository, times(1)).deleteById(customerId);
    }

    @Test
    void deleteCustomer_shouldThrowRuntimeException_whenCustomerDoesNotExist() {
        // Given
        Long customerId = 1L;
        when(customerRepository.existsById(customerId)).thenReturn(false);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> customerService.delete(customerId));
        assertEquals("Customer not found with id: " + customerId, exception.getMessage());
        verify(customerRepository, times(1)).existsById(customerId);
        verify(customerRepository, never()).deleteById(anyLong());
    }
}
