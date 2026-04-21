package com.tienda.unimagtienda.app.category.controller;

import com.tienda.unimagtienda.app.category.dto.CategoryResponse;
import com.tienda.unimagtienda.app.category.dto.CreateCategoryRequest;
import com.tienda.unimagtienda.app.category.dto.UpdateCategoryRequest;
import com.tienda.unimagtienda.app.category.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse create(@Valid @RequestBody CreateCategoryRequest req) {
        return service.create(req);
    }


    @GetMapping("/{id}")
    public CategoryResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }


    @GetMapping
    public List<CategoryResponse> getAll() {
        return service.getAll();
    }

    @PutMapping("/{id}")
    public CategoryResponse update(
            @PathVariable Long id,
            @RequestBody UpdateCategoryRequest req
    ) {
        return service.update(id, req);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}