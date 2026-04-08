package com.tienda.unimagTienda.api.dto;

import com.tienda.unimagTienda.domine.enums.OrderStatus;
import java.io.Serializable;
import java.time.LocalDateTime;

public class OrderStatusHistoryDto {
    public record OrderStatusHistoryResponse(
            Long id,
            Long orderId,
            OrderStatus status,
            String comment,
            LocalDateTime createdAt) implements Serializable {}
}
