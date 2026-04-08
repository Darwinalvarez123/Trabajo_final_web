package com.tienda.unimagTienda.domine.repository;

import com.tienda.unimagTienda.domine.entity.Category;
import com.tienda.unimagTienda.domine.entity.Inventory;
import com.tienda.unimagTienda.domine.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class InventoryRepositoryTest extends AbstractRepositoryIT {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("Debe encontrar inventario por ID de producto")
    void shouldFindByProductId() {
        Category category = categoryRepository.save(Category.builder().name("Electro").build());

        Product product = productRepository.save(Product.builder()
                .name("Laptop")
                .sku("LAP-001")
                .price(BigDecimal.valueOf(1000))
                .active(true)
                .category(category)
                .build());

        Inventory inventory = inventoryRepository.save(Inventory.builder()
                .product(product)
                .availableStock(10)
                .minStock(5)
                .build());

        Optional<Inventory> found = inventoryRepository.findByProductId(product.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getAvailableStock()).isEqualTo(10);
    }

    @Test
    @DisplayName("Debe encontrar inventarios con bajo stock")
    void shouldFindInventoriesWithLowStock() {
        Category category = categoryRepository.save(Category.builder().name("Accesorios").build());

        Product p1 = productRepository.save(Product.builder().name("Mouse").sku("MOU-001").price(BigDecimal.valueOf(20)).active(true).category(category).build());
        Product p2 = productRepository.save(Product.builder().name("Teclado").sku("TEC-001").price(BigDecimal.valueOf(50)).active(true).category(category).build());

        inventoryRepository.save(Inventory.builder().product(p1).availableStock(2).minStock(5).build()); // Bajo stock
        inventoryRepository.save(Inventory.builder().product(p2).availableStock(10).minStock(5).build()); // Stock normal

        List<Inventory> lowStockList = inventoryRepository.findInventoriesWithLowStock();

        assertThat(lowStockList).hasSize(1);
        assertThat(lowStockList.get(0).getProduct().getName()).isEqualTo("Mouse");
    }
}
