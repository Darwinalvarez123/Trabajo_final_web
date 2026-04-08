package com.tienda.unimagTienda.api.dto;

import java.io.Serializable;

public class CategoryDto {
    public record CategoryCreateRequest(
            String name,
            String description) implements Serializable {}

    public record CategoryResponse(
            Long id,
            String name,
            String description) implements Serializable {}
}
