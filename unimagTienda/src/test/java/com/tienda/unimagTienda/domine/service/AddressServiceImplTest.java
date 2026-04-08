package com.tienda.unimagTienda.domine.service;

import com.tienda.unimagTienda.api.dto.AddressDto.*;
import com.tienda.unimagTienda.domine.entity.Address;
import com.tienda.unimagTienda.domine.entity.Customer;
import com.tienda.unimagTienda.domine.repository.AddressRepository;
import com.tienda.unimagTienda.domine.repository.CustomerRepository;
import com.tienda.unimagTienda.domine.service.mapper.AddressMapper;
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

class AddressServiceImplTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AddressMapper addressMapper;

    @InjectMocks
    private AddressServiceImpl addressService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createAddress_shouldReturnAddressResponse() {
        // Given
        Long customerId = 1L;
        AddressCreateRequest request = new AddressCreateRequest("123 Main St", "City", "State", "12345", "Country", customerId);
        Customer customer = Customer.builder().id(customerId).build();
        Address addressEntity = Address.builder()
                .street("123 Main St").city("City").state("State").postalCode("12345").country("Country").customer(customer)
                .build();
        Address savedAddressEntity = Address.builder()
                .id(1L).street("123 Main St").city("City").state("State").postalCode("12345").country("Country").customer(customer)
                .build();
        AddressResponse expectedResponse = new AddressResponse(1L, "123 Main St", "City", "State", "12345", "Country", customerId);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(addressMapper.toEntity(request)).thenReturn(addressEntity);
        when(addressRepository.save(addressEntity)).thenReturn(savedAddressEntity);
        when(addressMapper.toResponse(savedAddressEntity)).thenReturn(expectedResponse);

        // When
        AddressResponse actualResponse = addressService.create(request);

        // Then
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.id(), actualResponse.id());
        assertEquals(expectedResponse.street(), actualResponse.street());
        verify(customerRepository, times(1)).findById(customerId);
        verify(addressMapper, times(1)).toEntity(request);
        verify(addressRepository, times(1)).save(addressEntity);
        verify(addressMapper, times(1)).toResponse(savedAddressEntity);
    }

    @Test
    void createAddress_shouldThrowRuntimeException_whenCustomerDoesNotExist() {
        // Given
        Long customerId = 1L;
        AddressCreateRequest request = new AddressCreateRequest("123 Main St", "City", "State", "12345", "Country", customerId);

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> addressService.create(request));
        assertEquals("Customer not found with id: " + customerId, exception.getMessage());
        verify(customerRepository, times(1)).findById(customerId);
        verify(addressMapper, never()).toEntity(any());
        verify(addressRepository, never()).save(any());
    }

    @Test
    void getAddress_shouldReturnAddressResponse_whenAddressExists() {
        // Given
        Long addressId = 1L;
        Long customerId = 1L;
        Customer customer = Customer.builder().id(customerId).build();
        Address addressEntity = Address.builder()
                .id(addressId).street("123 Main St").city("City").state("State").postalCode("12345").country("Country").customer(customer)
                .build();
        AddressResponse expectedResponse = new AddressResponse(addressId, "123 Main St", "City", "State", "12345", "Country", customerId);

        when(addressRepository.findById(addressId)).thenReturn(Optional.of(addressEntity));
        when(addressMapper.toResponse(addressEntity)).thenReturn(expectedResponse);

        // When
        AddressResponse actualResponse = addressService.get(addressId);

        // Then
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.id(), actualResponse.id());
        verify(addressRepository, times(1)).findById(addressId);
        verify(addressMapper, times(1)).toResponse(addressEntity);
    }

    @Test
    void getAddress_shouldThrowRuntimeException_whenAddressDoesNotExist() {
        // Given
        Long addressId = 1L;
        when(addressRepository.findById(addressId)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> addressService.get(addressId));
        assertEquals("Address not found with id: " + addressId, exception.getMessage());
        verify(addressRepository, times(1)).findById(addressId);
        verify(addressMapper, never()).toResponse(any());
    }

    @Test
    void updateAddress_shouldReturnUpdatedAddressResponse_whenAddressAndCustomerExist() {
        // Given
        Long addressId = 1L;
        Long oldCustomerId = 1L;
        Long newCustomerId = 2L;
        AddressCreateRequest request = new AddressCreateRequest("456 New St", "New City", "New State", "67890", "New Country", newCustomerId);
        
        Customer oldCustomer = Customer.builder().id(oldCustomerId).build();
        Customer newCustomer = Customer.builder().id(newCustomerId).build();

        Address existingAddress = Address.builder()
                .id(addressId).street("123 Main St").city("City").state("State").postalCode("12345").country("Country").customer(oldCustomer)
                .build();
        Address updatedAddress = Address.builder()
                .id(addressId).street("456 New St").city("New City").state("New State").postalCode("67890").country("New Country").customer(newCustomer)
                .build();
        AddressResponse expectedResponse = new AddressResponse(addressId, "456 New St", "New City", "New State", "67890", "New Country", newCustomerId);

        when(addressRepository.findById(addressId)).thenReturn(Optional.of(existingAddress));
        when(customerRepository.findById(newCustomerId)).thenReturn(Optional.of(newCustomer));
        when(addressRepository.save(any(Address.class))).thenReturn(updatedAddress);
        when(addressMapper.toResponse(updatedAddress)).thenReturn(expectedResponse);

        // When
        AddressResponse actualResponse = addressService.update(addressId, request);

        // Then
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.id(), actualResponse.id());
        assertEquals(expectedResponse.street(), actualResponse.street());
        assertEquals(expectedResponse.customerId(), actualResponse.customerId());
        verify(addressRepository, times(1)).findById(addressId);
        verify(customerRepository, times(1)).findById(newCustomerId);
        verify(addressRepository, times(1)).save(existingAddress);
        verify(addressMapper, times(1)).toResponse(updatedAddress);
    }

    @Test
    void updateAddress_shouldThrowRuntimeException_whenAddressDoesNotExist() {
        // Given
        Long addressId = 1L;
        Long customerId = 1L;
        AddressCreateRequest request = new AddressCreateRequest("456 New St", "New City", "New State", "67890", "New Country", customerId);
        when(addressRepository.findById(addressId)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> addressService.update(addressId, request));
        assertEquals("Address not found with id: " + addressId, exception.getMessage());
        verify(addressRepository, times(1)).findById(addressId);
        verify(customerRepository, never()).findById(anyLong());
        verify(addressRepository, never()).save(any());
    }

    @Test
    void updateAddress_shouldThrowRuntimeException_whenNewCustomerDoesNotExist() {
        // Given
        Long addressId = 1L;
        Long oldCustomerId = 1L;
        Long newCustomerId = 2L;
        AddressCreateRequest request = new AddressCreateRequest("456 New St", "New City", "New State", "67890", "New Country", newCustomerId);
        
        Customer oldCustomer = Customer.builder().id(oldCustomerId).build();
        Address existingAddress = Address.builder()
                .id(addressId).street("123 Main St").city("City").state("State").postalCode("12345").country("Country").customer(oldCustomer)
                .build();

        when(addressRepository.findById(addressId)).thenReturn(Optional.of(existingAddress));
        when(customerRepository.findById(newCustomerId)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> addressService.update(addressId, request));
        assertEquals("Customer not found with id: " + newCustomerId, exception.getMessage());
        verify(addressRepository, times(1)).findById(addressId);
        verify(customerRepository, times(1)).findById(newCustomerId);
        verify(addressRepository, never()).save(any());
    }

    @Test
    void getAllByCustomerId_shouldReturnListOfAddressResponses() {
        // Given
        Long customerId = 1L;
        Customer customer = Customer.builder().id(customerId).build();
        Address address1 = Address.builder().id(1L).street("Street 1").customer(customer).build();
        Address address2 = Address.builder().id(2L).street("Street 2").customer(customer).build();
        List<Address> addressEntities = Arrays.asList(address1, address2);

        AddressResponse response1 = new AddressResponse(1L, "Street 1", null, null, null, null, customerId);
        AddressResponse response2 = new AddressResponse(2L, "Street 2", null, null, null, null, customerId);
        List<AddressResponse> expectedResponses = Arrays.asList(response1, response2);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(addressRepository.findByCustomerId(customerId)).thenReturn(addressEntities);
        when(addressMapper.toResponse(address1)).thenReturn(response1);
        when(addressMapper.toResponse(address2)).thenReturn(response2);

        // When
        List<AddressResponse> actualResponses = addressService.getAllByCustomerId(customerId);

        // Then
        assertNotNull(actualResponses);
        assertEquals(2, actualResponses.size());
        assertEquals(expectedResponses.get(0).id(), actualResponses.get(0).id());
        assertEquals(expectedResponses.get(1).id(), actualResponses.get(1).id());
        verify(customerRepository, times(1)).findById(customerId);
        verify(addressRepository, times(1)).findByCustomerId(customerId);
        verify(addressMapper, times(2)).toResponse(any(Address.class));
    }

    @Test
    void getAllByCustomerId_shouldThrowRuntimeException_whenCustomerDoesNotExist() {
        // Given
        Long customerId = 1L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> addressService.getAllByCustomerId(customerId));
        assertEquals("Customer not found with id: " + customerId, exception.getMessage());
        verify(customerRepository, times(1)).findById(customerId);
        verify(addressRepository, never()).findByCustomerId(anyLong());
    }

    @Test
    void deleteAddress_shouldDeleteAddress_whenAddressExists() {
        // Given
        Long addressId = 1L;
        when(addressRepository.existsById(addressId)).thenReturn(true);
        doNothing().when(addressRepository).deleteById(addressId);

        // When
        addressService.delete(addressId);

        // Then
        verify(addressRepository, times(1)).existsById(addressId);
        verify(addressRepository, times(1)).deleteById(addressId);
    }

    @Test
    void deleteAddress_shouldThrowRuntimeException_whenAddressDoesNotExist() {
        // Given
        Long addressId = 1L;
        when(addressRepository.existsById(addressId)).thenReturn(false);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> addressService.delete(addressId));
        assertEquals("Address not found with id: " + addressId, exception.getMessage());
        verify(addressRepository, times(1)).existsById(addressId);
        verify(addressRepository, never()).deleteById(anyLong());
    }
}
