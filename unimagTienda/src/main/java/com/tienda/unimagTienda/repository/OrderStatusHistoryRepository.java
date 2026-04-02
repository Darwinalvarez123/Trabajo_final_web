package com.tienda.unimagTienda.repository;

import com.tienda.unimagTienda.entity.OrderStatusHistory;
import com.tienda.unimagTienda.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderStatusHistoryRepository extends JpaRepository<OrderStatusHistory, Long> {
    List<OrderStatusHistory> findByOrderId(Long orderId);
    List<OrderStatusHistory> findByStatus(OrderStatus status);
}