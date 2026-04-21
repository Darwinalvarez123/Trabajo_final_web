package com.tienda.unimagtienda.app.product.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public record UpdateProductRequest(

        String sku,
        String name,
        String description,
        BigDecimal price,
        Long categoryId

)implements Serializable {}