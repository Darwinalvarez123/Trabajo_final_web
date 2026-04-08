package com.tienda.unimagTienda.domine.service.mapper;

import com.tienda.unimagTienda.api.dto.CustomerDto.*;
import com.tienda.unimagTienda.domine.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Customer toEntity(CustomerCreateRequest req);

    CustomerResponse toResponse(Customer entity);
}
