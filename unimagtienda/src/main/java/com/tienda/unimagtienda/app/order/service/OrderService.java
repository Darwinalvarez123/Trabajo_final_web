package com.tienda.unimagtienda.app.order.service;

import com.tienda.unimagtienda.app.order.dto.CreateOrderRequest;
import com.tienda.unimagtienda.app.order.dto.OrderResponse;
import com.tienda.unimagtienda.app.orderStatusHistory.dto.OrderStatusHistoryResponse;

import java.util.List;

public interface OrderService {

    OrderResponse createOrder(CreateOrderRequest request);

    OrderResponse getById(Long id);

    List<OrderResponse> getAll();

    void payOrder(Long id);

    void shipOrder(Long id);

    void deliverOrder(Long id);

    void cancelOrder(Long id);
    List<OrderStatusHistoryResponse> getOrderHistory(Long orderId);
}