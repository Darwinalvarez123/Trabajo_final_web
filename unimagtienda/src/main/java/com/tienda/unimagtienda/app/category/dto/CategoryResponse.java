package com.tienda.unimagtienda.app.category.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public record CategoryResponse(

        Long id,
        String name,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt

) implements Serializable {}