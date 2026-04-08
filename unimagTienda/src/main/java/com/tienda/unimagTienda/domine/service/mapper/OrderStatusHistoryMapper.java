package com.tienda.unimagTienda.domine.service.mapper;

import com.tienda.unimagTienda.api.dto.OrderStatusHistoryDto.*;
import com.tienda.unimagTienda.domine.entity.OrderStatusHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderStatusHistoryMapper {

    @Mapping(target = "orderId", source = "order.id")
    OrderStatusHistoryResponse toResponse(OrderStatusHistory entity);
}
