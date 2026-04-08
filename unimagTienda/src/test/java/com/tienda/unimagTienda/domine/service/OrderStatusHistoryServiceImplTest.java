package com.tienda.unimagTienda.domine.service;

import com.tienda.unimagTienda.api.dto.OrderStatusHistoryDto.*;
import com.tienda.unimagTienda.domine.entity.Order;
import com.tienda.unimagTienda.domine.entity.OrderStatusHistory;
import com.tienda.unimagTienda.domine.enums.OrderStatus;
import com.tienda.unimagTienda.domine.repository.OrderRepository;
import com.tienda.unimagTienda.domine.repository.OrderStatusHistoryRepository;
import com.tienda.unimagTienda.domine.service.mapper.OrderStatusHistoryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderStatusHistoryServiceImplTest {

    @Mock
    private OrderStatusHistoryRepository orderStatusHistoryRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderStatusHistoryMapper orderStatusHistoryMapper;

    @InjectMocks
    private OrderStatusHistoryServiceImpl orderStatusHistoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getHistoryByOrderId_shouldReturnListOfResponses() {
        // Given
        Long orderId = 1L;
        OrderStatusHistory h1 = OrderStatusHistory.builder().id(1L).status(OrderStatus.CREATED).build();
        OrderStatusHistory h2 = OrderStatusHistory.builder().id(2L).status(OrderStatus.PAID).build();
        List<OrderStatusHistory> historyEntities = Arrays.asList(h1, h2);

        OrderStatusHistoryResponse r1 = new OrderStatusHistoryResponse(1L, orderId, OrderStatus.CREATED, null, null);
        OrderStatusHistoryResponse r2 = new OrderStatusHistoryResponse(2L, orderId, OrderStatus.PAID, null, null);

        when(orderStatusHistoryRepository.findByOrderIdOrderByCreatedAtAsc(orderId)).thenReturn(historyEntities);
        when(orderStatusHistoryMapper.toResponse(h1)).thenReturn(r1);
        when(orderStatusHistoryMapper.toResponse(h2)).thenReturn(r2);

        // When
        List<OrderStatusHistoryResponse> actualResponses = orderStatusHistoryService.getHistoryByOrderId(orderId);

        // Then
        assertEquals(2, actualResponses.size());
        verify(orderStatusHistoryRepository, times(1)).findByOrderIdOrderByCreatedAtAsc(orderId);
        verify(orderStatusHistoryMapper, times(2)).toResponse(any(OrderStatusHistory.class));
    }

    @Test
    void addHistory_shouldSaveNewHistory_whenOrderExists() {
        // Given
        Long orderId = 1L;
        String comment = "Test comment";
        Order order = Order.builder().id(orderId).status(OrderStatus.CREATED).build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // When
        orderStatusHistoryService.addHistory(orderId, comment);

        // Then
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderStatusHistoryRepository, times(1)).save(any(OrderStatusHistory.class));
    }

    @Test
    void addHistory_shouldThrow_whenOrderDoesNotExist() {
        // Given
        Long orderId = 1L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> orderStatusHistoryService.addHistory(orderId, "Comment"));
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderStatusHistoryRepository, never()).save(any());
    }
}
