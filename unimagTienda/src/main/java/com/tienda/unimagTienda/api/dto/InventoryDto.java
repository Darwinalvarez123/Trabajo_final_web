package com.tienda.unimagTienda.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

public class InventoryDto {
    public record InventoryRequest(
            @NotNull(message = "Product ID is mandatory") Long productId,
            @NotNull(message = "Available stock is mandatory") @Min(value = 0, message = "Stock cannot be negative") Integer availableStock,
            @NotNull(message = "Min stock is mandatory") @Min(value = 0, message = "Min stock cannot be negative") Integer minStock) implements Serializable {}

    public record UpdateInventoryRequest(
            @NotNull(message = "Product ID is mandatory") Long productId,
            @NotNull(message = "Available stock is mandatory") @Min(value = 0, message = "Stock cannot be negative") Integer availableStock,
            @NotNull(message = "Min stock is mandatory") @Min(value = 0, message = "Min stock cannot be negative") Integer minStock) implements Serializable {}

    public record InventoryResponse(
            Long id,
            Long productId,
            String productName,
            Integer availableStock,
            Integer minStock,
            LocalDateTime updatedAt) implements Serializable {}
    
    public record UpdateStockRequest(
            @NotNull(message = "Product ID is mandatory") Long productId,
            @NotNull(message = "Quantity is mandatory") Integer quantity) implements Serializable {}
}
