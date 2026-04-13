package com.tienda.unimagTienda.domine.service.mapper;

import com.tienda.unimagTienda.api.dto.AddressDto.*;
import com.tienda.unimagTienda.domine.entity.Address;
import com.tienda.unimagTienda.domine.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", source = "customerId")
    Address toEntity(CreateAddressRequest req);

    @Mapping(target = "customerId", source = "customer.id")
    AddressResponse toResponse(Address entity);

    default Customer map(Long customerId) {
        if (customerId == null) {
            return null;
        }
        Customer customer = new Customer();
        customer.setId(customerId);
        return customer;
    }
}
