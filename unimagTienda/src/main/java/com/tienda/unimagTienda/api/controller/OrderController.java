package com.tienda.unimagTienda.api.controller;

import com.tienda.unimagTienda.api.dto.OrderDto.*;
import com.tienda.unimagTienda.domine.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody CreateOrderRequest req) {
        return new ResponseEntity<>(orderService.create(req), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.get(id));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllByCustomer(@RequestParam(required = false) Long customerId) {
        if (customerId != null) {
            return ResponseEntity.ok(orderService.getAllByCustomerId(customerId));
        }
        return ResponseEntity.ok(List.of()); 
    }

    @PutMapping("/{id}/pay")
    public ResponseEntity<OrderResponse> pay(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.pay(id));
    }

    @PutMapping("/{id}/ship")
    public ResponseEntity<OrderResponse> ship(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.ship(id));
    }

    @PutMapping("/{id}/deliver")
    public ResponseEntity<OrderResponse> deliver(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.deliver(id));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<OrderResponse> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.cancel(id));
    }
}
