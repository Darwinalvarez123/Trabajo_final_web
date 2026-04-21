package com.tienda.unimagtienda.app.orderStatusHistory.mapper;

import com.tienda.unimagtienda.app.orderStatusHistory.dto.OrderStatusHistoryResponse;
import com.tienda.unimagtienda.app.orderStatusHistory.entity.OrderStatusHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderStatusHistoryMapper {

    @Mapping(target = "status", expression = "java(entity.getStatus().name())")
    OrderStatusHistoryResponse toResponse(OrderStatusHistory entity);
}