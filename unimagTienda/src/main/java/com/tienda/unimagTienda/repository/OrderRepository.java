package com.tienda.unimagTienda.repository;

import com.tienda.unimagTienda.entity.Customer;
import com.tienda.unimagTienda.entity.Order;
import com.tienda.unimagTienda.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomer(Customer customer);

    @Query("""
        SELECT o FROM Order o WHERE
        (:customerId IS NULL OR o.customer.id = :customerId) AND
        (:status IS NULL OR o.status = :status) AND
        (:startDate IS NULL OR o.createdAt >= :startDate) AND
        (:endDate IS NULL OR o.createdAt <= :endDate) AND
        (:minTotal IS NULL OR o.total >= :minTotal) AND
        (:maxTotal IS NULL OR o.total <= :maxTotal)
        ORDER BY o.createdAt DESC
    """)
    List<Order> findOrdersWithFilters(
            @Param("customerId") Long customerId,
            @Param("status") OrderStatus status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("minTotal") BigDecimal minTotal,
            @Param("maxTotal") BigDecimal maxTotal
    );

    @Query("""
        SELECT DISTINCT o FROM Order o
        JOIN FETCH o.items i
        JOIN FETCH i.product
        WHERE o.id = :id
    """)
    Optional<Order> findByIdWithItems(@Param("id") Long id);

    List<Order> findByCustomerId(Long customerId);
    List<Order> findByStatus(OrderStatus status);
    List<Order> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}