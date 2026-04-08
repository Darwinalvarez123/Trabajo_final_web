package com.tienda.unimagTienda.domine.repository;

import com.tienda.unimagTienda.domine.entity.OrderItem;
import com.tienda.unimagTienda.domine.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrderId(Long orderId);

    @Query("""
        SELECT oi.product.id, oi.product.name, SUM(oi.quantity) as totalSold
        FROM OrderItem oi
        WHERE oi.order.status != 'CANCELLED'
        GROUP BY oi.product.id, oi.product.name
        ORDER BY totalSold DESC
    """)
    List<Object[]> findBestSellingProducts();

    @Query("""
        SELECT oi.product.category.id, oi.product.category.name, SUM(oi.quantity) as volume
        FROM OrderItem oi
        WHERE oi.order.status != 'CANCELLED'
        GROUP BY oi.product.category.id, oi.product.category.name
        ORDER BY volume DESC
    """)
    List<Object[]> findTopCategoriesByVolume();
}
