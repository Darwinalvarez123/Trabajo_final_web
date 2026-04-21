package com.tienda.unimagtienda.app.customer.service;

import com.tienda.unimagtienda.app.customer.dto.CreateCustomerRequest;
import com.tienda.unimagtienda.app.customer.dto.CustomerResponse;
import com.tienda.unimagtienda.app.customer.dto.UpdateCustomerRequest;


import java.util.List;

public interface CustomerService {

    CustomerResponse create(CreateCustomerRequest req);

    CustomerResponse getById(Long id);

    List<CustomerResponse> getAll();

    CustomerResponse update(Long id, UpdateCustomerRequest req);

    void delete(Long id);
}
