package com.tienda.unimagTienda.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

public class ProductDto {
    public record CreateProductRequest(
            @NotBlank(message = "SKU is mandatory") String sku,
            @NotBlank(message = "Name is mandatory") String name,
            String description,
            @NotNull(message = "Price is mandatory") @DecimalMin(value = "0.01", message = "Price must be greater than zero") BigDecimal price,
            @NotNull(message = "Category ID is mandatory") Long categoryId) implements Serializable {}

    public record UpdateProductRequest(
            @NotBlank(message = "SKU is mandatory") String sku,
            @NotBlank(message = "Name is mandatory") String name,
            String description,
            @NotNull(message = "Price is mandatory") @DecimalMin(value = "0.01", message = "Price must be greater than zero") BigDecimal price,
            @NotNull(message = "Category ID is mandatory") Long categoryId) implements Serializable {}

    public record ProductResponse(
            Long id,
            String sku,
            String name,
            String description,
            BigDecimal price,
            Boolean active,
            Long categoryId) implements Serializable {}
}
