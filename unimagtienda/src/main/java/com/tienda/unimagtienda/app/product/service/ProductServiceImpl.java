package com.tienda.unimagtienda.app.product.service;

import com.tienda.unimagtienda.app.category.entity.Category;
import com.tienda.unimagtienda.app.category.repository.CategoryRepository;
import com.tienda.unimagtienda.app.inventory.entity.Inventory;
import com.tienda.unimagtienda.app.inventory.repository.InventoryRepository;
import com.tienda.unimagtienda.app.product.dto.CreateProductRequest;
import com.tienda.unimagtienda.app.product.dto.ProductResponse;
import com.tienda.unimagtienda.app.product.dto.UpdateProductRequest;
import com.tienda.unimagtienda.app.product.entity.Product;
import com.tienda.unimagtienda.app.product.mapper.ProductMapper;
import com.tienda.unimagtienda.app.product.repository.ProductRepository;
import com.tienda.unimagtienda.exception.ConflictException;
import com.tienda.unimagtienda.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final InventoryRepository inventoryRepository;

    @Override
    public ProductResponse create(CreateProductRequest req) {

        if (productRepository.existsBySku(req.sku())) {
            throw new ConflictException("SKU already exists");
        }

        if (req.price().doubleValue() <= 0) {
            throw new ConflictException("Price must be greater than 0");
        }

        Category category = categoryRepository.findById(req.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Product product = productMapper.toEntity(req);
        product.setCategory(category);
        product.setActive(true);

        Product savedProduct = productRepository.save(product);


        Inventory inventory = Inventory.builder()
                .product(savedProduct)
                .availableStock(0)
                .minStock(0)
                .build();

        inventoryRepository.save(inventory);

        return productMapper.toResponse(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (!product.getActive()) {
            throw new ResourceNotFoundException("Product not found");
        }

        return productMapper.toResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAll() {
        return productRepository.findByActiveTrue()
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllAdmin() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getByIdAdmin(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        return productMapper.toResponse(product);
    }

    @Override
    public ProductResponse update(Long id, UpdateProductRequest req) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (!product.getActive()) {
            throw new ResourceNotFoundException("Product not found");
        }

        if (req.sku() != null &&
                !req.sku().equals(product.getSku()) &&
                productRepository.existsBySku(req.sku())) {
            throw new ConflictException("SKU already exists");
        }

        if (req.categoryId() != null) {
            Category category = categoryRepository.findById(req.categoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            product.setCategory(category);
        }

        productMapper.update(product, req);

        return productMapper.toResponse(productRepository.save(product));
    }

    @Override
    public void delete(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        product.setActive(false);

        productRepository.save(product);
    }
}