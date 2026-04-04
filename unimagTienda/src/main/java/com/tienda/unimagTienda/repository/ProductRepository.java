package com.tienda.unimagTienda.repository;

import com.tienda.unimagTienda.entity.Category;
import com.tienda.unimagTienda.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findBySku(String sku);

    List<Product> findByCategoryAndActiveTrue(Category category);

    List<Product> findByCategoryIdAndActiveTrue(Long categoryId);

    List<Product> findByPriceBetweenAndActiveTrue(BigDecimal min, BigDecimal max);

    List<Product> findByNameContainingIgnoreCaseAndActiveTrue(String name);

    List<Product> findByActiveTrue();

    @Query("""
        SELECT p FROM Product p
        WHERE p.inventory.availableStock < p.inventory.minStock
    """)
    List<Product> findProductsWithLowStock();
}