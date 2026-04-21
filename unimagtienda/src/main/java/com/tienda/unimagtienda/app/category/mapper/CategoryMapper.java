package com.tienda.unimagtienda.app.category.mapper;

import com.tienda.unimagtienda.app.category.dto.CategoryResponse;
import com.tienda.unimagtienda.app.category.dto.CreateCategoryRequest;
import com.tienda.unimagtienda.app.category.dto.UpdateCategoryRequest;
import com.tienda.unimagtienda.app.category.entity.Category;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Category toEntity(CreateCategoryRequest req);

    CategoryResponse toResponse(Category entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void update(@MappingTarget Category category, UpdateCategoryRequest req);
}