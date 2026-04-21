package com.tienda.unimagtienda.app.inventory.service;

import com.tienda.unimagtienda.app.inventory.dto.InventoryResponse;
import com.tienda.unimagtienda.app.inventory.dto.UpdateInventoryRequest;

public interface InventoryService {

    InventoryResponse updateByProductId(Long productId, UpdateInventoryRequest req);

    InventoryResponse getByProductId(Long productId);
}