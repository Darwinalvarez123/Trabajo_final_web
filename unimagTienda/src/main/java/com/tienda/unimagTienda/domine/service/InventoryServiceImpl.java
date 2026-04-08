package com.tienda.unimagTienda.domine.service;

import com.tienda.unimagTienda.api.dto.InventoryDto.*;
import com.tienda.unimagTienda.domine.entity.Inventory;
import com.tienda.unimagTienda.domine.entity.Product;
import com.tienda.unimagTienda.domine.repository.InventoryRepository;
import com.tienda.unimagTienda.domine.repository.ProductRepository;
import com.tienda.unimagTienda.domine.service.mapper.InventoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;
    private final InventoryMapper inventoryMapper;

    @Override
    @Transactional
    public InventoryResponse create(InventoryRequest req) {
        // Validate if product exists
        Product product = productRepository.findById(req.productId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + req.productId()));

        // Check if inventory already exists for this product
        if (inventoryRepository.findByProductId(req.productId()).isPresent()) {
            throw new RuntimeException("Inventory already exists for product id: " + req.productId());
        }

        Inventory inventory = inventoryMapper.toEntity(req);
        inventory.setProduct(product);
        inventory = inventoryRepository.save(inventory);
        return inventoryMapper.toResponse(inventory);
    }

    @Override
    @Transactional
    public InventoryResponse update(Long id, InventoryRequest req) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found with id: " + id));

        // Update product reference if changed
        if (!inventory.getProduct().getId().equals(req.productId())) {
            Product product = productRepository.findById(req.productId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + req.productId()));
            inventory.setProduct(product);
        }

        inventoryMapper.updateEntity(req, inventory);
        inventory = inventoryRepository.save(inventory);
        return inventoryMapper.toResponse(inventory);
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryResponse getByProductId(Long productId) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Inventory not found for product id: " + productId));
        return inventoryMapper.toResponse(inventory);
    }

    @Override
    @Transactional
    public InventoryResponse updateStock(UpdateStockRequest req) {
        Inventory inventory = inventoryRepository.findByProductId(req.productId())
                .orElseThrow(() -> new RuntimeException("Inventory not found for product id: " + req.productId()));
        
        inventory.setAvailableStock(inventory.getAvailableStock() + req.quantity());
        if (inventory.getAvailableStock() < 0) {
            throw new RuntimeException("Not enough stock for product id: " + req.productId());
        }
        
        inventory = inventoryRepository.save(inventory);
        return inventoryMapper.toResponse(inventory);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryResponse> getLowStock() {
        return inventoryRepository.findInventoriesWithLowStock().stream()
                .map(inventoryMapper::toResponse)
                .collect(Collectors.toList());
    }
}
