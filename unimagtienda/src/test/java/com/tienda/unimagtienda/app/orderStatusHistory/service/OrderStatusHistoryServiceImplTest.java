package com.tienda.unimagtienda.app.orderStatusHistory.service;

import com.tienda.unimagtienda.app.order.entity.Order;
import com.tienda.unimagtienda.app.order.enums.OrderStatus;
import com.tienda.unimagtienda.app.orderStatusHistory.dto.OrderStatusHistoryResponse;
import com.tienda.unimagtienda.app.orderStatusHistory.entity.OrderStatusHistory;
import com.tienda.unimagtienda.app.orderStatusHistory.mapper.OrderStatusHistoryMapper;
import com.tienda.unimagtienda.app.orderStatusHistory.repository.OrderStatusHistoryRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mapstruct.factory.Mappers;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class OrderStatusHistoryServiceImplTest {

    @Mock
    private OrderStatusHistoryRepository repository;

    @Spy
    private OrderStatusHistoryMapper mapper = Mappers.getMapper(OrderStatusHistoryMapper.class);

    @InjectMocks
    private OrderStatusHistoryServiceImpl service;

    @Test
    @DisplayName("OrderStatusHistoryService: registra historial correctamente")
    void shouldRegisterHistory() {

        Order order = Order.builder().id(1L).build();

        service.register(order, OrderStatus.CREATED, "Order created");

        verify(repository).save(any(OrderStatusHistory.class));
    }

    @Test
    @DisplayName("OrderStatusHistoryService: obtiene historial por orderId")
    void shouldGetByOrderId() {

        Order order = Order.builder().id(1L).build();

        OrderStatusHistory history = OrderStatusHistory.builder()
                .id(1L)
                .order(order)
                .status(OrderStatus.CREATED)
                .comment("Order created")
                .build();

        when(repository.findByOrderId(1L)).thenReturn(List.of(history));

        List<OrderStatusHistoryResponse> result = service.getByOrderId(1L);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().status()).isEqualTo(OrderStatus.CREATED.name());

        verify(repository).findByOrderId(1L);
    }
}