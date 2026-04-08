package com.tienda.unimagTienda.api.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProductDto {
    public record ProductCreateRequest(
            String sku,
            String name,
            String description,
            BigDecimal price,
            Long categoryId) implements Serializable {}

    public record ProductResponse(
            Long id,
            String sku,
            String name,
            String description,
            BigDecimal price,
            Boolean active,
            Long categoryId) implements Serializable {}
}
