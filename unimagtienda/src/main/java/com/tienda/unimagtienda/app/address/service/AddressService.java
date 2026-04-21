package com.tienda.unimagtienda.app.address.service;

import com.tienda.unimagtienda.app.address.dto.AddressResponse;
import com.tienda.unimagtienda.app.address.dto.CreateAddressRequest;
import com.tienda.unimagtienda.app.address.dto.UpdateAddressRequest;
import com.tienda.unimagtienda.app.customer.dto.CreateCustomerRequest;
import com.tienda.unimagtienda.app.customer.dto.CustomerResponse;
import com.tienda.unimagtienda.app.customer.dto.UpdateCustomerRequest;

import java.util.List;

public interface AddressService {
    AddressResponse create(CreateAddressRequest req);

    AddressResponse getById(Long id);

    List<AddressResponse> getAll();

    AddressResponse update(Long id, UpdateAddressRequest req);

    void delete(Long id);
}
