package com.tienda.unimagtienda.app.orderStatusHistory.service;

import com.tienda.unimagtienda.app.order.entity.Order;
import com.tienda.unimagtienda.app.order.enums.OrderStatus;
import com.tienda.unimagtienda.app.orderStatusHistory.dto.OrderStatusHistoryResponse;
import com.tienda.unimagtienda.app.orderStatusHistory.entity.OrderStatusHistory;
import com.tienda.unimagtienda.app.orderStatusHistory.mapper.OrderStatusHistoryMapper;
import com.tienda.unimagtienda.app.orderStatusHistory.repository.OrderStatusHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderStatusHistoryServiceImpl implements OrderStatusHistoryService {

    private final OrderStatusHistoryRepository repository;
    private final OrderStatusHistoryMapper mapper;

    @Override
    public void register(Order order, OrderStatus status, String comment) {
        OrderStatusHistory history = OrderStatusHistory.builder().order(order).status(status).comment(comment).build();

        repository.save(history);
    }
    @Override
    public List<OrderStatusHistoryResponse> getByOrderId(Long orderId) {

        return repository.findByOrderId(orderId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }
}