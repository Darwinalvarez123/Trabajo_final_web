package com.tienda.unimagTienda.domine.service.mapper;

import com.tienda.unimagTienda.api.dto.OrderItemDto.*;
import com.tienda.unimagTienda.domine.entity.OrderItem;
import com.tienda.unimagTienda.domine.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "unitPrice", ignore = true)
    @Mapping(target = "subtotal", ignore = true)
    @Mapping(target = "product", source = "productId")
    OrderItem toEntity(OrderItemCreateRequest req);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    OrderItemResponse toResponse(OrderItem entity);

    default Product map(Long productId) {
        if (productId == null) {
            return null;
        }
        Product product = new Product();
        product.setId(productId);
        return product;
    }
}
