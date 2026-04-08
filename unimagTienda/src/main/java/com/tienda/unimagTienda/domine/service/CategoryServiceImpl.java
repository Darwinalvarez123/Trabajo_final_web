package com.tienda.unimagTienda.domine.service;

import com.tienda.unimagTienda.api.dto.CategoryDto.*;
import com.tienda.unimagTienda.domine.entity.Category;
import com.tienda.unimagTienda.domine.repository.CategoryRepository;
import com.tienda.unimagTienda.domine.service.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryResponse create(CategoryCreateRequest req) {
        // Optional: Check if a category with the same name already exists
        if (categoryRepository.findByName(req.name()).isPresent()) {
            throw new RuntimeException("Category with name " + req.name() + " already exists.");
        }
        Category category = categoryMapper.toEntity(req);
        category = categoryRepository.save(category);
        return categoryMapper.toResponse(category);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse get(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        return categoryMapper.toResponse(category);
    }

    @Override
    @Transactional
    public CategoryResponse update(Long id, CategoryCreateRequest req) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        
        // Optional: Check for duplicate name if name is changed
        if (!category.getName().equals(req.name()) && categoryRepository.findByName(req.name()).isPresent()) {
            throw new RuntimeException("Category with name " + req.name() + " already exists.");
        }
        
        category.setName(req.name());
        category.setDescription(req.description());
        
        category = categoryRepository.save(category);
        return categoryMapper.toResponse(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAll() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Category not found with id: " + id);
        }
        // Optional: Check if category has associated products before deleting
        // If so, decide whether to disassociate, reassign, or prevent deletion.
        categoryRepository.deleteById(id);
    }
}
