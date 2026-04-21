package com.tienda.unimagtienda.app.address.mapper;

import com.tienda.unimagtienda.app.address.dto.AddressResponse;
import com.tienda.unimagtienda.app.address.dto.CreateAddressRequest;
import com.tienda.unimagtienda.app.address.dto.UpdateAddressRequest;
import com.tienda.unimagtienda.app.address.entity.Address;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    Address toEntity(CreateAddressRequest req);

    @Mapping(source = "customer.id", target = "customerId")
    AddressResponse toResponse(Address entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    void update(@MappingTarget Address address, UpdateAddressRequest req);
}