package com.tienda.unimagtienda.app.category.dto;


import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public record CreateCategoryRequest(

        @NotBlank(message = "Name is required")
        String name,

        String description

) implements Serializable {}