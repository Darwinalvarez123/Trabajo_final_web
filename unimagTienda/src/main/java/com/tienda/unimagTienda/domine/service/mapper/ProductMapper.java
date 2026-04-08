package com.tienda.unimagTienda.domine.service.mapper;

import com.tienda.unimagTienda.api.dto.ProductDto.*;
import com.tienda.unimagTienda.domine.entity.Category;
import com.tienda.unimagTienda.domine.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "inventory", ignore = true)
    @Mapping(target = "category", source = "categoryId")
    Product toEntity(ProductCreateRequest req);

    @Mapping(target = "categoryId", source = "category.id")
    ProductResponse toResponse(Product entity);

    default Category map(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        Category category = new Category();
        category.setId(categoryId);
        return category;
    }
}
