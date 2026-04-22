package com.tienda.unimagtienda.app.category.repository;

import com.tienda.unimagtienda.app.category.entity.Category;
import com.tienda.unimagtienda.shared.AbstractRepositoryIT;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryRepositoryTest extends AbstractRepositoryIT {

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    @DisplayName("Category: guarda, encuentra por nombre y valida existencia")
    void shouldSaveAndFindByName() {

        // Given
        var category = Category.builder()
                .name("Electronics")
                .description("Electronic products")
                .build();

        categoryRepository.save(category);

        // When
        Optional<Category> foundByName =
                categoryRepository.findByName("Electronics");

        boolean exists =
                categoryRepository.existsByName("Electronics");

        // Then
        assertThat(foundByName).isPresent();
        assertThat(foundByName.get().getName())
                .isEqualTo("Electronics");

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Category: encuentra por id")
    void shouldFindById() {

        // Given
        var category = categoryRepository.save(
                Category.builder()
                        .name("Books")
                        .description("Books category")
                        .build()
        );

        // When
        var found = categoryRepository.findById(category.getId());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Books");
        assertThat(found.get().getDescription()).isEqualTo("Books category");
    }
}