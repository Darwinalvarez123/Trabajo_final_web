package com.tienda.unimagtienda.app.category.service;

import com.tienda.unimagtienda.app.category.dto.CategoryResponse;
import com.tienda.unimagtienda.app.category.dto.CreateCategoryRequest;
import com.tienda.unimagtienda.app.category.dto.UpdateCategoryRequest;

import java.util.List;

public interface CategoryService {

    CategoryResponse create(CreateCategoryRequest req);

    CategoryResponse getById(Long id);

    List<CategoryResponse> getAll();

    CategoryResponse update(Long id, UpdateCategoryRequest req);

    void delete(Long id);
}