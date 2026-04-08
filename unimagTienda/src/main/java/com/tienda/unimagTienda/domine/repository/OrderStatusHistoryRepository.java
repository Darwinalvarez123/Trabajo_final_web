package com.tienda.unimagTienda.domine.repository;

import com.tienda.unimagTienda.domine.entity.OrderStatusHistory;
import com.tienda.unimagTienda.domine.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderStatusHistoryRepository extends JpaRepository<OrderStatusHistory, Long> {

    List<OrderStatusHistory> findByOrderIdOrderByCreatedAtAsc(Long orderId);

    List<OrderStatusHistory> findByStatus(OrderStatus status);
}
