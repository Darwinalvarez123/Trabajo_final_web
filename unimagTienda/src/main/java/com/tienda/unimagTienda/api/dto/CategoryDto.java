package com.tienda.unimagTienda.api.dto;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

public class CategoryDto {
    public record CreateCategoryRequest(
            @NotBlank(message = "Category name is mandatory") String name,
            String description) implements Serializable {}

    public record CategoryResponse(
            Long id,
            String name,
            String description) implements Serializable {}
}
