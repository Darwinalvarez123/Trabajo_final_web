package com.tienda.unimagTienda.domine.service;

import com.tienda.unimagTienda.api.dto.CategoryDto.*;
import java.util.List;

public interface CategoryService {
    CategoryResponse create(CategoryCreateRequest req);
    CategoryResponse get(Long id);
    CategoryResponse update(Long id, CategoryCreateRequest req);
    List<CategoryResponse> getAll();
    void delete(Long id);
}
