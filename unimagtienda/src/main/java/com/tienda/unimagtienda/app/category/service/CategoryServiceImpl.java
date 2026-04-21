package com.tienda.unimagtienda.app.category.service;

import com.tienda.unimagtienda.app.category.dto.CategoryResponse;
import com.tienda.unimagtienda.app.category.dto.CreateCategoryRequest;
import com.tienda.unimagtienda.app.category.dto.UpdateCategoryRequest;
import com.tienda.unimagtienda.app.category.entity.Category;
import com.tienda.unimagtienda.app.category.mapper.CategoryMapper;
import com.tienda.unimagtienda.app.category.repository.CategoryRepository;
import com.tienda.unimagtienda.exception.ConflictException;
import com.tienda.unimagtienda.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    @Override
    public CategoryResponse create(CreateCategoryRequest req) {


        if (repository.existsByName(req.name())) {
            throw new ConflictException("Category already exists");
        }

        Category category = mapper.toEntity(req);
        Category saved = repository.save(category);

        return mapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getById(Long id) {

        Category category = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found: " + id));

        return mapper.toResponse(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAll() {

        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public CategoryResponse update(Long id, UpdateCategoryRequest req) {

        Category category = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found: " + id));

       
        if (req.name() != null &&
                !req.name().equals(category.getName()) &&
                repository.existsByName(req.name())) {

            throw new ConflictException("Category name already exists");
        }

        mapper.update(category, req);

        Category updated = repository.save(category);

        return mapper.toResponse(updated);
    }

    @Override
    public void delete(Long id) {

        Category category = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found: " + id));

        repository.delete(category);
    }
}