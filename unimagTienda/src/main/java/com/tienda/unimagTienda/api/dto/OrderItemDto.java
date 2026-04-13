package com.tienda.unimagTienda.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

public class OrderItemDto {
    public record CreateOrderItemRequest(
            @NotNull(message = "Product ID is mandatory") Long productId,
            @NotNull(message = "Quantity is mandatory") @Min(value = 1, message = "Quantity must be at least 1") Integer quantity) implements Serializable {}

    public record OrderItemResponse(
            Long id,
            Long productId,
            String productName,
            Integer quantity,
            BigDecimal unitPrice,
            BigDecimal subtotal) implements Serializable {}
}
