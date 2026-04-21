package com.tienda.unimagtienda.app.inventory.controller;

import com.tienda.unimagtienda.app.inventory.dto.InventoryResponse;
import com.tienda.unimagtienda.app.inventory.dto.UpdateInventoryRequest;
import com.tienda.unimagtienda.app.inventory.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/{productId}/inventory")
    public InventoryResponse getInventory(@PathVariable Long productId) {
        return inventoryService.getByProductId(productId);
    }

    @PutMapping("/{productId}/inventory")
    public InventoryResponse updateInventory(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateInventoryRequest req
    ) {
        return inventoryService.updateByProductId(productId, req);
    }
}