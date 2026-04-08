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

public class ProductRepositoryTest extends AbstractRepositoryIT {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Test
    @DisplayName("Debe buscar producto por SKU")
    void shouldFindBySku() {
        Category category = categoryRepository.save(Category.builder().name("Electro").build());
        Product product = Product.builder()
                .name("Smartphone")
                .sku("SMART-001")
                .price(BigDecimal.valueOf(500))
                .active(true)
                .category(category)
                .build();
        productRepository.save(product);

        Optional<Product> found = productRepository.findBySku("SMART-001");

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Smartphone");
    }

    @Test
    @DisplayName("Debe encontrar productos activos por categoría")
    void shouldFindByCategoryAndActiveTrue() {
        Category cat = categoryRepository.save(Category.builder().name("Hogar").build());

        productRepository.save(Product.builder().name("Lámpara").category(cat).active(true).sku("LUM-1").price(BigDecimal.valueOf(30)).build());
        productRepository.save(Product.builder().name("Silla").category(cat).active(false).sku("CHAIR-1").price(BigDecimal.valueOf(50)).build());

        List<Product> products = productRepository.findByCategoryAndActiveTrue(cat);

        assertThat(products).hasSize(1);
        assertThat(products.get(0).getName()).isEqualTo("Lámpara");
    }

    @Test
    @DisplayName("Debe encontrar productos con bajo stock")
    void shouldFindProductsWithLowStock() {
        Category category = categoryRepository.save(Category.builder().name("Monitores").build());
        Product product = productRepository.save(Product.builder().name("Monitor").sku("MON-1").price(BigDecimal.valueOf(150)).active(true).category(category).build());
        inventoryRepository.save(Inventory.builder().product(product).availableStock(1).minStock(5).build());

        List<Product> lowStockProducts = productRepository.findProductsWithLowStock();

        assertThat(lowStockProducts).isNotEmpty();
        assertThat(lowStockProducts).extracting(Product::getName).contains("Monitor");
    }
}
