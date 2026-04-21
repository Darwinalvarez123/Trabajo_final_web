package com.tienda.unimagtienda.app.orderItem.mapper;

import com.tienda.unimagtienda.app.orderItem.dto.OrderItemResponse;
import com.tienda.unimagtienda.app.orderItem.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(source = "product.id", target = "productId")
    OrderItemResponse toResponse(OrderItem entity);
}