package com.tienda.unimagtienda.app.inventory.repository;

import com.tienda.unimagtienda.app.category.entity.Category;
import com.tienda.unimagtienda.app.category.repository.CategoryRepository;
import com.tienda.unimagtienda.app.inventory.entity.Inventory;
import com.tienda.unimagtienda.app.product.entity.Product;
import com.tienda.unimagtienda.app.product.repository.ProductRepository;
import com.tienda.unimagtienda.shared.AbstractRepositoryIT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;


import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest

class InventoryRepositoryTest extends AbstractRepositoryIT {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Product product;

    @BeforeEach
    void setUp() {

        Category category = Category.builder()
                .name("Tech")
                .description("Technology products")
                .build();

        category = categoryRepository.save(category);

        product = Product.builder()
                .sku("INV-001")
                .name("Laptop")
                .price(BigDecimal.valueOf(2000))
                .active(true)
                .category(category)
                .build();

        product = productRepository.save(product);
    }

    @Test
    void shouldSaveInventory() {

        Inventory inventory = Inventory.builder()
                .product(product)
                .availableStock(10)
                .minStock(2)
                .build();

        Inventory saved = inventoryRepository.save(inventory);

        assertNotNull(saved.getId());
        assertEquals(10, saved.getAvailableStock());
    }

    @Test
    void shouldFindByProductId() {

        Inventory inventory = Inventory.builder()
                .product(product)
                .availableStock(5)
                .minStock(1)
                .build();

        inventoryRepository.save(inventory);

        Optional<Inventory> result = inventoryRepository.findByProductId(product.getId());

        assertTrue(result.isPresent());
        assertEquals(5, result.get().getAvailableStock());
    }
}