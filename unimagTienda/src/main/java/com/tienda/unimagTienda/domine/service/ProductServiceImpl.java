package com.tienda.unimagTienda.domine.service;

import com.tienda.unimagTienda.api.dto.ProductDto.*;
import com.tienda.unimagTienda.domine.entity.Category;
import com.tienda.unimagTienda.domine.entity.Product;
import com.tienda.unimagTienda.domine.repository.CategoryRepository;
import com.tienda.unimagTienda.domine.repository.ProductRepository;
import com.tienda.unimagTienda.domine.service.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductResponse create(ProductCreateRequest req) {
        // Check if SKU already exists
        if (productRepository.findBySku(req.sku()).isPresent()) {
            throw new RuntimeException("Product with SKU " + req.sku() + " already exists.");
        }
        
        // Check if Category exists
        Category category = categoryRepository.findById(req.categoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + req.categoryId()));

        Product product = productMapper.toEntity(req);
        product.setCategory(category);
        product = productRepository.save(product);
        return productMapper.toResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse get(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        return productMapper.toResponse(product);
    }

    @Override
    @Transactional
    public ProductResponse update(Long id, ProductCreateRequest req) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        // Validate SKU if changed
        if (!product.getSku().equals(req.sku()) && productRepository.findBySku(req.sku()).isPresent()) {
            throw new RuntimeException("Product with SKU " + req.sku() + " already exists.");
        }
        
        // Validate and update category if changed
        if (!product.getCategory().getId().equals(req.categoryId())) {
            Category category = categoryRepository.findById(req.categoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found with id: " + req.categoryId()));
            product.setCategory(category);
        }

        product.setSku(req.sku());
        product.setName(req.name());
        product.setDescription(req.description());
        product.setPrice(req.price());
        
        product = productRepository.save(product);
        return productMapper.toResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAll() {
        return productRepository.findAll().stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        // Soft delete (logical delete) by setting active to false
        product.setActive(false);
        productRepository.save(product);
    }
}
