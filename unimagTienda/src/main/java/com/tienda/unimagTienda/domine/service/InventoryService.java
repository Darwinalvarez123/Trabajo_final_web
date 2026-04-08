package com.tienda.unimagTienda.domine.service;

import com.tienda.unimagTienda.api.dto.InventoryDto.*;
import java.util.List;

public interface InventoryService {
    InventoryResponse create(InventoryRequest req);
    InventoryResponse update(Long id, InventoryRequest req);
    InventoryResponse getByProductId(Long productId);
    InventoryResponse updateStock(UpdateStockRequest req);
    List<InventoryResponse> getLowStock();
}
