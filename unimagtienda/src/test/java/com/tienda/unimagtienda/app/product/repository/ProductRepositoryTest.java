package com.tienda.unimagtienda.app.product.repository;

import com.tienda.unimagtienda.app.category.entity.Category;
import com.tienda.unimagtienda.app.category.repository.CategoryRepository;
import com.tienda.unimagtienda.app.product.entity.Product;
import com.tienda.unimagtienda.shared.AbstractRepositoryIT;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ProductRepositoryTest extends AbstractRepositoryIT {

    @Autowired ProductRepository productRepository;
    @Autowired CategoryRepository categoryRepository;

    @Test
    @DisplayName("Product: guarda, encuentra por SKU y por categoría")
    void shouldSaveAndFindProduct() {

        // Given
        var category = categoryRepository.save(
                Category.builder()
                        .name("Electronics")
                        .description("Electronic items")
                        .build()
        );

        var product = Product.builder()
                .sku("SKU-001")
                .name("Laptop")
                .description("Gaming laptop")
                .price(new BigDecimal("2500.00"))
                .active(true)
                .category(category)
                .build();

        productRepository.save(product);

        // When
        Optional<Product> bySku =
                productRepository.findBySku("SKU-001");

        List<Product> byCategory =
                productRepository.findByCategoryId(category.getId());

        List<Product> activeProducts =
                productRepository.findByActiveTrue();

        // Then
        assertThat(bySku).isPresent();
        assertThat(bySku.get().getName()).isEqualTo("Laptop");

        assertThat(byCategory).isNotEmpty();
        assertThat(byCategory.getFirst().getSku()).isEqualTo("SKU-001");

        assertThat(activeProducts).isNotEmpty();
        assertThat(activeProducts.getFirst().getActive()).isTrue();
    }

    @Test
    @DisplayName("Product: valida existencia por SKU")
    void shouldCheckExistsBySku() {

        // Given
        var category = categoryRepository.save(
                Category.builder()
                        .name("Books")
                        .build()
        );

        productRepository.save(
                Product.builder()
                        .sku("BOOK-001")
                        .name("Clean Code")
                        .price(new BigDecimal("50.00"))
                        .active(true)
                        .category(category)
                        .build()
        );

        // When
        boolean exists =
                productRepository.existsBySku("BOOK-001");

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Product: encuentra por id")
    void shouldFindById() {

        // Given
        var category = categoryRepository.save(
                Category.builder()
                        .name("Hardware")
                        .build()
        );

        var product = productRepository.save(
                Product.builder()
                        .sku("HW-001")
                        .name("Mouse")
                        .price(new BigDecimal("20.00"))
                        .active(true)
                        .category(category)
                        .build()
        );

        // When
        var found = productRepository.findById(product.getId());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Mouse");
        assertThat(found.get().getCategory().getName())
                .isEqualTo("Hardware");
    }
}