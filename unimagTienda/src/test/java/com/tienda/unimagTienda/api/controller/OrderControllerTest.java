package com.tienda.unimagTienda.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tienda.unimagTienda.api.dto.OrderDto.CreateOrderRequest;
import com.tienda.unimagTienda.api.dto.OrderDto.OrderResponse;
import com.tienda.unimagTienda.api.dto.OrderItemDto.CreateOrderItemRequest;
import com.tienda.unimagTienda.api.exception.BusinessException;
import com.tienda.unimagTienda.domine.enums.OrderStatus;
import com.tienda.unimagTienda.domine.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/orders - Debe crear un pedido exitosamente")
    void createOrder_Success() throws Exception {
        CreateOrderItemRequest item = new CreateOrderItemRequest(1L, 2);
        CreateOrderRequest request = new CreateOrderRequest(1L, 1L, List.of(item));
        OrderResponse response = new OrderResponse(1L, 1L, 1L, OrderStatus.CREATED, new BigDecimal("100.00"), null, Collections.emptyList());

        when(orderService.create(any(CreateOrderRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.total").value(100.00));
    }

    @Test
    @DisplayName("PUT /api/orders/{id}/pay - Debe marcar pedido como pagado")
    void payOrder_Success() throws Exception {
        OrderResponse response = new OrderResponse(1L, 1L, 1L, OrderStatus.PAID, new BigDecimal("100.00"), null, Collections.emptyList());

        when(orderService.pay(1L)).thenReturn(response);

        mockMvc.perform(put("/api/orders/1/pay"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PAID"));
    }

    @Test
    @DisplayName("PUT /api/orders/{id}/pay - Debe fallar por stock insuficiente")
    void payOrder_NoStockError() throws Exception {
        when(orderService.pay(1L)).thenThrow(new BusinessException("Not enough stock for product id: 1"));

        mockMvc.perform(put("/api/orders/1/pay"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("BUSINESS_ERROR"))
                .andExpect(jsonPath("$.message").value("Not enough stock for product id: 1"));
    }

    @Test
    @DisplayName("PUT /api/orders/{id}/cancel - Debe cancelar el pedido")
    void cancelOrder_Success() throws Exception {
        OrderResponse response = new OrderResponse(1L, 1L, 1L, OrderStatus.CANCELLED, new BigDecimal("100.00"), null, Collections.emptyList());

        when(orderService.cancel(1L)).thenReturn(response);

        mockMvc.perform(put("/api/orders/1/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }
}
