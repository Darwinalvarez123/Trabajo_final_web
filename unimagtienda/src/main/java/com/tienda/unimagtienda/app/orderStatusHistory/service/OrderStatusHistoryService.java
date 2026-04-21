package com.tienda.unimagtienda.app.orderStatusHistory.service;

import com.tienda.unimagtienda.app.order.entity.Order;
import com.tienda.unimagtienda.app.order.enums.OrderStatus;
import com.tienda.unimagtienda.app.orderStatusHistory.dto.OrderStatusHistoryResponse;

import java.util.List;

public interface OrderStatusHistoryService {
    void register(Order order, OrderStatus status, String comment);
    List<OrderStatusHistoryResponse> getByOrderId(Long orderId);
}