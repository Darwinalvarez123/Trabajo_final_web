package com.tienda.unimagtienda.app.order.mapper;

import com.tienda.unimagtienda.app.order.dto.CreateOrderRequest;
import com.tienda.unimagtienda.app.order.dto.OrderResponse;
import com.tienda.unimagtienda.app.order.entity.Order;
import com.tienda.unimagtienda.app.orderItem.mapper.OrderItemMapper;
import org.mapstruct.*;
@Mapper(
        componentModel = "spring",
        uses = {OrderItemMapper.class}
)

public interface OrderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "shippingAddress", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "total", ignore = true)
    @Mapping(target = "items", source = "items")
    Order toEntity(CreateOrderRequest req);

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "shippingAddress.id", target = "addressId")
    OrderResponse toResponse(Order entity);
}