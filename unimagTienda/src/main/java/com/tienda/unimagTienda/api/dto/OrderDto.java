package com.tienda.unimagTienda.api.dto;

import com.tienda.unimagTienda.domine.enums.OrderStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderDto {
    public record CreateOrderRequest(
            @NotNull(message = "Customer ID is mandatory") Long customerId,
            @NotNull(message = "Address ID is mandatory") Long addressId,
            @NotEmpty(message = "Order must have at least one item") 
            @Valid List<OrderItemDto.CreateOrderItemRequest> items) implements Serializable {}

    public record OrderResponse(
            Long id,
            Long customerId,
            Long addressId,
            OrderStatus status,
            BigDecimal total,
            LocalDateTime createdAt,
            List<OrderItemDto.OrderItemResponse> items) implements Serializable {}

    public record CancelOrderRequest(
            String reason) implements Serializable {}
}
