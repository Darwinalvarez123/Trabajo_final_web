package com.tienda.unimagtienda.app.category.dto;

import java.io.Serializable;

public record UpdateCategoryRequest(

        String name,
        String description

) implements Serializable {}