package com.tienda.unimagtienda.app.inventory.service;

import com.tienda.unimagtienda.app.inventory.dto.InventoryResponse;
import com.tienda.unimagtienda.app.inventory.dto.UpdateInventoryRequest;
import com.tienda.unimagtienda.app.inventory.entity.Inventory;
import com.tienda.unimagtienda.app.inventory.mapper.InventoryMapper;
import com.tienda.unimagtienda.app.inventory.repository.InventoryRepository;
import com.tienda.unimagtienda.exception.ConflictException;
import com.tienda.unimagtienda.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;

    @Override
    @Transactional(readOnly = true)
    public InventoryResponse getByProductId(Long productId) {

        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Inventory not found for product " + productId)
                );

        return inventoryMapper.toResponse(inventory);
    }

    @Override
    public InventoryResponse updateByProductId(Long productId, UpdateInventoryRequest req) {

        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Inventory not found for product " + productId)
                );


        if (req.availableStock() != null && req.availableStock() < 0) {
            throw new ConflictException("Available stock cannot be negative");
        }

        if (req.minStock() != null && req.minStock() < 0) {
            throw new ConflictException("Min stock cannot be negative");
        }

        inventoryMapper.update(inventory, req);

        Inventory saved = inventoryRepository.save(inventory);

        return inventoryMapper.toResponse(saved);
    }
}