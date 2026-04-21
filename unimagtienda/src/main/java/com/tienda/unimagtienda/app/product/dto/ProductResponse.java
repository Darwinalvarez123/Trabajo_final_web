package com.tienda.unimagtienda.app.product.dto;

import com.tienda.unimagtienda.app.category.dto.CategoryResponse;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductResponse(

        Long id,
        String sku,
        String name,
        String description,
        BigDecimal price,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        CategoryResponse category

) implements Serializable {}