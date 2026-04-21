package com.tienda.unimagtienda.app.order.controller;

import com.tienda.unimagtienda.app.order.dto.CreateOrderRequest;
import com.tienda.unimagtienda.app.order.dto.OrderResponse;
import com.tienda.unimagtienda.app.order.service.OrderService;
import com.tienda.unimagtienda.app.orderStatusHistory.dto.OrderStatusHistoryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;


    @PostMapping
    public ResponseEntity<OrderResponse> create(@RequestBody @Valid CreateOrderRequest request) {
        return ResponseEntity.ok(orderService.createOrder(request));
    }


    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getById(id));
    }


    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAll() {
        return ResponseEntity.ok(orderService.getAll());
    }


    @PutMapping("/{id}/pay")
    public ResponseEntity<Void> pay(@PathVariable Long id) {
        orderService.payOrder(id);
        return ResponseEntity.ok().build();
    }


    @PutMapping("/{id}/ship")
    public ResponseEntity<Void> ship(@PathVariable Long id) {
        orderService.shipOrder(id);
        return ResponseEntity.ok().build();
    }


    @PutMapping("/{id}/deliver")
    public ResponseEntity<Void> deliver(@PathVariable Long id) {
        orderService.deliverOrder(id);
        return ResponseEntity.ok().build();
    }


    @PutMapping("/{id}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<OrderStatusHistoryResponse>> getHistory(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderHistory(id));
    }
}