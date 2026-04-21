package com.tienda.unimagtienda.app.inventory.mapper;

import com.tienda.unimagtienda.app.inventory.dto.InventoryResponse;
import com.tienda.unimagtienda.app.inventory.dto.UpdateInventoryRequest;
import com.tienda.unimagtienda.app.inventory.entity.Inventory;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface InventoryMapper {

    @Mapping(source = "product.id", target = "productId")
    InventoryResponse toResponse(Inventory inventory);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    void update(@MappingTarget Inventory inventory, UpdateInventoryRequest req);
}