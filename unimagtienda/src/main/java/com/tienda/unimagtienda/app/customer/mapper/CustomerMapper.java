package com.tienda.unimagtienda.app.customer.mapper;


import com.tienda.unimagtienda.app.customer.dto.CreateCustomerRequest;
import com.tienda.unimagtienda.app.customer.dto.CustomerResponse;
import com.tienda.unimagtienda.app.customer.dto.UpdateCustomerRequest;
import com.tienda.unimagtienda.app.customer.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
@Mapper(componentModel = "spring")
public interface CustomerMapper {
    @Mapping(target = "id", ignore = true)
    Customer toEntity(CreateCustomerRequest req);

    CustomerResponse toResponse(Customer entity);

    @Mapping(target = "id", ignore = true)
    void update(@MappingTarget Customer customer, UpdateCustomerRequest req);
}
