package com.tienda.unimagTienda.repository;

import com.tienda.unimagTienda.entity.OrderItem;
import com.tienda.unimagTienda.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // Métodos derivados básicos
    List<OrderItem> findByOrderId(Long orderId);
    List<OrderItem> findByProductId(Long productId);


    @Query("""
        SELECT oi FROM OrderItem oi
        JOIN FETCH oi.product p
        JOIN FETCH oi.order o
        WHERE o.status IN (:statuses)
        AND o.createdAt BETWEEN :startDate AND :endDate
    """)
    List<OrderItem> findItemsDetailed(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("statuses") List<OrderStatus> statuses
    );


    @Query("""
        SELECT oi FROM OrderItem oi
        JOIN FETCH oi.product p
        JOIN FETCH p.category c
        JOIN FETCH oi.order o
        WHERE o.status IN (:statuses)
    """)
    List<OrderItem> findItemsWithCategory(@Param("statuses") List<OrderStatus> statuses);

    @Query("""
        SELECT SUM(oi.subtotal) FROM OrderItem oi
        WHERE oi.product.id = :productId
        AND oi.order.status IN (
            com.tienda.unimagTienda.enums.OrderStatus.PAID,
            com.tienda.unimagTienda.enums.OrderStatus.SHIPPED,
            com.tienda.unimagTienda.enums.OrderStatus.DELIVERED
        )
    """)
    java.math.BigDecimal getTotalSalesByProductId(@Param("productId") Long productId);
}
