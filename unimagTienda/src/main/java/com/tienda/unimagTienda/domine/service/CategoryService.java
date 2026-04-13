package com.tienda.unimagTienda.domine.service;

import com.tienda.unimagTienda.api.dto.CategoryDto.*;
import java.util.List;

public interface CategoryService {
    CategoryResponse create(CreateCategoryRequest req);
    CategoryResponse get(Long id);
    CategoryResponse update(Long id, CreateCategoryRequest req);
    List<CategoryResponse> getAll();
    void delete(Long id);
}
