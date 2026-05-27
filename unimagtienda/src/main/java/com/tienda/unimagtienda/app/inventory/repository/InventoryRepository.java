package com.tienda.unimagtienda.app.inventory.repository;

import com.tienda.unimagtienda.app.inventory.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByProductId(Long productId);
    @Query("""
           SELECT COUNT(i)
           FROM Inventory i
           WHERE i.availableStock <= i.minStock
           """)
    long countLowStock();

}


