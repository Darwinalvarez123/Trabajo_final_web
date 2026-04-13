package com.tienda.unimagTienda.domine.service.mapper;

import com.tienda.unimagTienda.api.dto.CategoryDto.*;
import com.tienda.unimagTienda.domine.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "products", ignore = true)
    Category toEntity(CreateCategoryRequest req);

    CategoryResponse toResponse(Category entity);
}
