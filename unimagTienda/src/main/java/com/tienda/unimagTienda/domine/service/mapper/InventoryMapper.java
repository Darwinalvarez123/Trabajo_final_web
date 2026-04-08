package com.tienda.unimagTienda.domine.service.mapper;

import com.tienda.unimagTienda.api.dto.InventoryDto.*;
import com.tienda.unimagTienda.domine.entity.Inventory;
import com.tienda.unimagTienda.domine.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface InventoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "product", source = "productId")
    Inventory toEntity(InventoryRequest req);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    InventoryResponse toResponse(Inventory entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "product", ignore = true)
    void updateEntity(InventoryRequest req, @MappingTarget Inventory entity);

    default Product map(Long productId) {
        if (productId == null) {
            return null;
        }
        Product product = new Product();
        product.setId(productId);
        return product;
    }
}
