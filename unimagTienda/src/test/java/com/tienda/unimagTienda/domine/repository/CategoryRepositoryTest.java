package com.tienda.unimagTienda.domine.repository;

import com.tienda.unimagTienda.domine.entity.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class CategoryRepositoryTest extends AbstractRepositoryIT {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("Debe buscar una categoria por nombre")
    void shouldFindByName() {
        // Given
        Category category = Category.builder()
                .name("Ropa")
                .description("Prendas de vestir")
                .build();
        categoryRepository.save(category);

        // When
        Optional<Category> found = categoryRepository.findByName("Ropa");

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Ropa");
    }

    @Test
    @DisplayName("Debe retornar vacío al buscar una categoría inexistente")
    void shouldReturnEmptyWhenNotFound() {
        // When
        Optional<Category> found = categoryRepository.findByName("NoExiste");

        // Then
        assertThat(found).isEmpty();
    }
}
