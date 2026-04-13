package com.tienda.unimagTienda.domine.service;

import com.tienda.unimagTienda.api.dto.AddressDto.*;
import java.util.List;

public interface AddressService {
    AddressResponse create(CreateAddressRequest req);
    AddressResponse get(Long id);
    AddressResponse update(Long id, CreateAddressRequest req);
    List<AddressResponse> getAllByCustomerId(Long customerId);
    void delete(Long id);
}
