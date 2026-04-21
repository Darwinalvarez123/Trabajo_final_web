package com.tienda.unimagtienda.app.inventory.dto;

import java.io.Serializable;

public record InventoryResponse(

        Long id,
        Long productId,
        Integer availableStock,
        Integer minStock

) implements Serializable {}