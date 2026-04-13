package com.tienda.unimagTienda.domine.service;

import com.tienda.unimagTienda.api.dto.CategoryDto.*;
import com.tienda.unimagTienda.domine.entity.Category;
import com.tienda.unimagTienda.domine.repository.CategoryRepository;
import com.tienda.unimagTienda.domine.service.mapper.CategoryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCategory_shouldReturnCategoryResponse_whenNameIsUnique() {
        // Given
        CreateCategoryRequest request = new CreateCategoryRequest("Electronics", "Electronic devices");
        Category categoryEntity = Category.builder().name("Electronics").description("Electronic devices").build();
        Category savedCategoryEntity = Category.builder().id(1L).name("Electronics").description("Electronic devices").build();
        CategoryResponse expectedResponse = new CategoryResponse(1L, "Electronics", "Electronic devices");

        when(categoryRepository.findByName(request.name())).thenReturn(Optional.empty());
        when(categoryMapper.toEntity(request)).thenReturn(categoryEntity);
        when(categoryRepository.save(categoryEntity)).thenReturn(savedCategoryEntity);
        when(categoryMapper.toResponse(savedCategoryEntity)).thenReturn(expectedResponse);

        // When
        CategoryResponse actualResponse = categoryService.create(request);

        // Then
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.id(), actualResponse.id());
        assertEquals(expectedResponse.name(), actualResponse.name());
        verify(categoryRepository, times(1)).findByName(request.name());
        verify(categoryMapper, times(1)).toEntity(request);
        verify(categoryRepository, times(1)).save(categoryEntity);
        verify(categoryMapper, times(1)).toResponse(savedCategoryEntity);
    }

    @Test
    void createCategory_shouldThrowRuntimeException_whenNameAlreadyExists() {
        // Given
        CreateCategoryRequest request = new CreateCategoryRequest("Electronics", "Description");
        when(categoryRepository.findByName(request.name())).thenReturn(Optional.of(new Category()));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> categoryService.create(request));
        assertEquals("Category with name " + request.name() + " already exists.", exception.getMessage());
        verify(categoryRepository, times(1)).findByName(request.name());
        verify(categoryMapper, never()).toEntity(any());
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void getCategory_shouldReturnCategoryResponse_whenCategoryExists() {
        // Given
        Long id = 1L;
        Category categoryEntity = Category.builder().id(id).name("Electronics").build();
        CategoryResponse expectedResponse = new CategoryResponse(id, "Electronics", null);

        when(categoryRepository.findById(id)).thenReturn(Optional.of(categoryEntity));
        when(categoryMapper.toResponse(categoryEntity)).thenReturn(expectedResponse);

        // When
        CategoryResponse actualResponse = categoryService.get(id);

        // Then
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.id(), actualResponse.id());
        verify(categoryRepository, times(1)).findById(id);
        verify(categoryMapper, times(1)).toResponse(categoryEntity);
    }

    @Test
    void getCategory_shouldThrowRuntimeException_whenCategoryDoesNotExist() {
        // Given
        Long id = 1L;
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> categoryService.get(id));
        assertEquals("Category not found with id: " + id, exception.getMessage());
        verify(categoryRepository, times(1)).findById(id);
        verify(categoryMapper, never()).toResponse(any());
    }

    @Test
    void updateCategory_shouldReturnUpdatedCategoryResponse_whenNameIsUnique() {
        // Given
        Long id = 1L;
        CreateCategoryRequest request = new CreateCategoryRequest("New Name", "New Description");
        Category existingCategory = Category.builder().id(id).name("Old Name").description("Old Description").build();
        Category savedCategory = Category.builder().id(id).name("New Name").description("New Description").build();
        CategoryResponse expectedResponse = new CategoryResponse(id, "New Name", "New Description");

        when(categoryRepository.findById(id)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.findByName(request.name())).thenReturn(Optional.empty());
        when(categoryRepository.save(existingCategory)).thenReturn(savedCategory);
        when(categoryMapper.toResponse(savedCategory)).thenReturn(expectedResponse);

        // When
        CategoryResponse actualResponse = categoryService.update(id, request);

        // Then
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.name(), actualResponse.name());
        verify(categoryRepository, times(1)).findById(id);
        verify(categoryRepository, times(1)).findByName(request.name());
        verify(categoryRepository, times(1)).save(existingCategory);
        verify(categoryMapper, times(1)).toResponse(savedCategory);
    }

    @Test
    void updateCategory_shouldThrowRuntimeException_whenNewNameAlreadyExists() {
        // Given
        Long id = 1L;
        CreateCategoryRequest request = new CreateCategoryRequest("Existing Name", "Description");
        Category existingCategory = Category.builder().id(id).name("Old Name").build();
        Category anotherCategory = Category.builder().id(2L).name("Existing Name").build();

        when(categoryRepository.findById(id)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.findByName(request.name())).thenReturn(Optional.of(anotherCategory));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> categoryService.update(id, request));
        assertEquals("Category with name " + request.name() + " already exists.", exception.getMessage());
        verify(categoryRepository, times(1)).findById(id);
        verify(categoryRepository, times(1)).findByName(request.name());
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void getAllCategories_shouldReturnListOfCategoryResponses() {
        // Given
        Category category1 = Category.builder().id(1L).name("C1").build();
        Category category2 = Category.builder().id(2L).name("C2").build();
        List<Category> categoryEntities = Arrays.asList(category1, category2);

        CategoryResponse response1 = new CategoryResponse(1L, "C1", null);
        CategoryResponse response2 = new CategoryResponse(2L, "C2", null);

        when(categoryRepository.findAll()).thenReturn(categoryEntities);
        when(categoryMapper.toResponse(category1)).thenReturn(response1);
        when(categoryMapper.toResponse(category2)).thenReturn(response2);

        // When
        List<CategoryResponse> actualResponses = categoryService.getAll();

        // Then
        assertEquals(2, actualResponses.size());
        verify(categoryRepository, times(1)).findAll();
        verify(categoryMapper, times(2)).toResponse(any(Category.class));
    }

    @Test
    void deleteCategory_shouldDelete_whenCategoryExists() {
        // Given
        Long id = 1L;
        when(categoryRepository.existsById(id)).thenReturn(true);
        doNothing().when(categoryRepository).deleteById(id);

        // When
        categoryService.delete(id);

        // Then
        verify(categoryRepository, times(1)).existsById(id);
        verify(categoryRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteCategory_shouldThrow_whenCategoryDoesNotExist() {
        // Given
        Long id = 1L;
        when(categoryRepository.existsById(id)).thenReturn(false);

        // When & Then
        assertThrows(RuntimeException.class, () -> categoryService.delete(id));
        verify(categoryRepository, times(1)).existsById(id);
        verify(categoryRepository, never()).deleteById(anyLong());
    }
}
