package com.tienda.unimagtienda.app.product.service;

import com.tienda.unimagtienda.app.product.dto.CreateProductRequest;
import com.tienda.unimagtienda.app.product.dto.ProductResponse;
import com.tienda.unimagtienda.app.product.dto.UpdateProductRequest;

import java.util.List;

public interface ProductService {

    ProductResponse create(CreateProductRequest req);

    ProductResponse getById(Long id);

    List<ProductResponse> getAll();


    List<ProductResponse> getAllAdmin();

    ProductResponse getByIdAdmin(Long id);

    ProductResponse update(Long id, UpdateProductRequest req);

    void delete(Long id);
}