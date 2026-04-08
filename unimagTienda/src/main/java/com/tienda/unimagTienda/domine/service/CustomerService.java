package com.tienda.unimagTienda.domine.service;

import com.tienda.unimagTienda.api.dto.CustomerDto.*;
import java.util.List;

public interface CustomerService {
    CustomerResponse create(CustomerCreateRequest req);
    CustomerResponse get(Long id);
    CustomerResponse update(Long id, CustomerCreateRequest req);
    List<CustomerResponse> getAll();
    void delete(Long id);
}
