package com.tienda.unimagtienda.app.orderStatusHistory.repository;

import com.tienda.unimagtienda.app.orderStatusHistory.entity.OrderStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderStatusHistoryRepository extends JpaRepository<OrderStatusHistory, Long> {
    List<OrderStatusHistory> findByOrderId(Long orderId);
    List<OrderStatusHistory> findByOrderIdOrderByCreatedAtAsc(Long orderId);

}
