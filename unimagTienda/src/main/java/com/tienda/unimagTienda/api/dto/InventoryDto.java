package com.tienda.unimagTienda.api.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class InventoryDto {
    public record InventoryRequest(
            Long productId,
            Integer availableStock,
            Integer minStock) implements Serializable {}

    public record InventoryResponse(
            Long id,
            Long productId,
            String productName,
            Integer availableStock,
            Integer minStock,
            LocalDateTime updatedAt) implements Serializable {}
    
    public record UpdateStockRequest(
            Long productId,
            Integer quantity) implements Serializable {}
}
