package com.tienda.unimagTienda.domine.service;

import com.tienda.unimagTienda.api.dto.CustomerDto.*;
import java.util.List;

public interface CustomerService {
    CustomerResponse create(CreateCustomerRequest req);
    CustomerResponse get(Long id);
    CustomerResponse update(Long id, UpdateCustomerRequest req);
    List<CustomerResponse> getAll();
    void delete(Long id);
}
