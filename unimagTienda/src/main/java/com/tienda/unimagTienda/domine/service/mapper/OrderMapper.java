package com.tienda.unimagTienda.domine.service.mapper;

import com.tienda.unimagTienda.api.dto.OrderDto.*;
import com.tienda.unimagTienda.domine.entity.Address;
import com.tienda.unimagTienda.domine.entity.Customer;
import com.tienda.unimagTienda.domine.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class})
public interface OrderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "total", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "statusHistory", ignore = true)
    @Mapping(target = "customer", source = "customerId")
    @Mapping(target = "shippingAddress", source = "addressId")
    Order toEntity(OrderCreateRequest req);

    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "addressId", source = "shippingAddress.id")
    OrderResponse toResponse(Order entity);

    default Customer mapCustomer(Long customerId) {
        if (customerId == null) {
            return null;
        }
        Customer customer = new Customer();
        customer.setId(customerId);
        return customer;
    }

    default Address mapAddress(Long addressId) {
        if (addressId == null) {
            return null;
        }
        Address address = new Address();
        address.setId(addressId);
        return address;
    }
}
