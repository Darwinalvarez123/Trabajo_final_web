package com.tienda.unimagTienda.domine.service;

import com.tienda.unimagTienda.api.dto.OrderStatusHistoryDto.*;
import com.tienda.unimagTienda.domine.entity.Order;
import com.tienda.unimagTienda.domine.entity.OrderStatusHistory;
import com.tienda.unimagTienda.domine.repository.OrderRepository;
import com.tienda.unimagTienda.domine.repository.OrderStatusHistoryRepository;
import com.tienda.unimagTienda.domine.service.mapper.OrderStatusHistoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderStatusHistoryServiceImpl implements OrderStatusHistoryService {

    private final OrderStatusHistoryRepository orderStatusHistoryRepository;
    private final OrderRepository orderRepository;
    private final OrderStatusHistoryMapper orderStatusHistoryMapper;

    @Override
    @Transactional(readOnly = true)
    public List<OrderStatusHistoryResponse> getHistoryByOrderId(Long orderId) {
        return orderStatusHistoryRepository.findByOrderIdOrderByCreatedAtAsc(orderId).stream()
                .map(orderStatusHistoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addHistory(Long orderId, String comment) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        OrderStatusHistory history = OrderStatusHistory.builder()
                .order(order)
                .status(order.getStatus())
                .comment(comment)
                .build();

        orderStatusHistoryRepository.save(history);
    }
}
