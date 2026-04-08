package com.tienda.unimagTienda.api.dto;

import com.tienda.unimagTienda.domine.enums.OrderStatus;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderDto {
    public record OrderCreateRequest(
            Long customerId,
            Long addressId,
            List<OrderItemDto.OrderItemCreateRequest> items) implements Serializable {}

    public record OrderResponse(
            Long id,
            Long customerId,
            Long addressId,
            OrderStatus status,
            BigDecimal total,
            LocalDateTime createdAt,
            List<OrderItemDto.OrderItemResponse> items) implements Serializable {}
}
