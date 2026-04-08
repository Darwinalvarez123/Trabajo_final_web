package com.tienda.unimagTienda.api.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class OrderItemDto {
    public record OrderItemCreateRequest(
            Long productId,
            Integer quantity) implements Serializable {}

    public record OrderItemResponse(
            Long id,
            Long productId,
            String productName,
            Integer quantity,
            BigDecimal unitPrice,
            BigDecimal subtotal) implements Serializable {}
}
