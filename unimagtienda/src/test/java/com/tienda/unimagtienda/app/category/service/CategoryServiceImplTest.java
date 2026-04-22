package com.tienda.unimagtienda.app.category.service;

import com.tienda.unimagtienda.app.category.dto.CreateCategoryRequest;
import com.tienda.unimagtienda.app.category.dto.UpdateCategoryRequest;
import com.tienda.unimagtienda.app.category.entity.Category;
import com.tienda.unimagtienda.app.category.mapper.CategoryMapper;
import com.tienda.unimagtienda.app.category.repository.CategoryRepository;
import com.tienda.unimagtienda.exception.ConflictException;
import com.tienda.unimagtienda.exception.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository repository;

    @Spy
    private CategoryMapper mapper = Mappers.getMapper(CategoryMapper.class);

    @InjectMocks
    private CategoryServiceImpl service;

    @Test
    @DisplayName("create: guarda correctamente")
    void shouldCreateCategory() {
        var req = new CreateCategoryRequest("Tech", "Desc");

        when(repository.existsByName("Tech")).thenReturn(false);
        when(repository.save(any(Category.class))).thenAnswer(inv -> {
            Category c = inv.getArgument(0);
            c.setId(1L);
            return c;
        });

        var res = service.create(req);

        assertThat(res.id()).isEqualTo(1L);
        assertThat(res.name()).isEqualTo("Tech");

        verify(repository).save(any(Category.class));
    }

    @Test
    @DisplayName("create: lanza error si ya existe")
    void shouldThrowIfCategoryExists() {
        var req = new CreateCategoryRequest("Tech", "Desc");

        when(repository.existsByName("Tech")).thenReturn(true);

        assertThatThrownBy(() -> service.create(req))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    @DisplayName("getById: retorna categoría")
    void shouldGetById() {
        var entity = Category.builder()
                .id(1L)
                .name("Tech")
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        var res = service.getById(1L);

        assertThat(res.id()).isEqualTo(1L);
        assertThat(res.name()).isEqualTo("Tech");
    }

    @Test
    @DisplayName("getById: lanza error si no existe")
    void shouldThrowIfNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("update: actualiza correctamente")
    void shouldUpdateCategory() {
        var entity = Category.builder()
                .id(1L)
                .name("Old")
                .description("Old desc")
                .build();

        var req = new UpdateCategoryRequest("New", "New desc");

        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.existsByName("New")).thenReturn(false);
        when(repository.save(any(Category.class))).thenReturn(entity);

        var res = service.update(1L, req);

        assertThat(res.name()).isEqualTo("New");
        assertThat(res.description()).isEqualTo("New desc");

        verify(repository).save(entity);
    }

    @Test
    @DisplayName("update: lanza error si nombre ya existe")
    void shouldThrowIfNameExistsOnUpdate() {
        var entity = Category.builder()
                .id(1L)
                .name("Old")
                .build();

        var req = new UpdateCategoryRequest("New", null);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.existsByName("New")).thenReturn(true);

        assertThatThrownBy(() -> service.update(1L, req))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    @DisplayName("delete: elimina correctamente")
    void shouldDeleteCategory() {
        var entity = Category.builder().id(1L).build();

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        service.delete(1L);

        verify(repository).delete(entity);
    }

    @Test
    @DisplayName("delete: lanza error si no existe")
    void shouldThrowOnDeleteIfNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}