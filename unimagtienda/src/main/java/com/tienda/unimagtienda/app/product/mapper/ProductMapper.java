package com.tienda.unimagtienda.app.product.mapper;

import com.tienda.unimagtienda.app.product.dto.CreateProductRequest;
import com.tienda.unimagtienda.app.product.dto.ProductResponse;
import com.tienda.unimagtienda.app.product.dto.UpdateProductRequest;
import com.tienda.unimagtienda.app.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {com.tienda.unimagtienda.app.category.mapper.CategoryMapper.class})
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "active", ignore = true)
    Product toEntity(CreateProductRequest req);

    ProductResponse toResponse(Product entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "sku", ignore = true)
    void update(@MappingTarget Product product, UpdateProductRequest req);
}