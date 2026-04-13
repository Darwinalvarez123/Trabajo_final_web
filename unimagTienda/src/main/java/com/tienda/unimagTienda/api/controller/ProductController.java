package com.tienda.unimagTienda.api.controller;

import com.tienda.unimagTienda.api.dto.InventoryDto.InventoryRequest;
import com.tienda.unimagTienda.api.dto.InventoryDto.InventoryResponse;
import com.tienda.unimagTienda.api.dto.ProductDto.*;
import com.tienda.unimagTienda.domine.service.InventoryService;
import com.tienda.unimagTienda.domine.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final InventoryService inventoryService;

    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody CreateProductRequest req) {
        return new ResponseEntity<>(productService.create(req), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(productService.get(id));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAll() {
        return ResponseEntity.ok(productService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(@PathVariable Long id, @Valid @RequestBody UpdateProductRequest req) {
        return ResponseEntity.ok(productService.update(id, req));
    }

    @PutMapping("/{id}/inventory")
    public ResponseEntity<InventoryResponse> updateInventory(@PathVariable Long id, @Valid @RequestBody InventoryRequest req) {
        InventoryResponse currentInventory = inventoryService.getByProductId(id);
        return ResponseEntity.ok(inventoryService.update(currentInventory.id(), req));
    }
}
