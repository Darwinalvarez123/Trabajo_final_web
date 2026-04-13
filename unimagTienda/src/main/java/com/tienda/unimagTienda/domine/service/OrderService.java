package com.tienda.unimagTienda.domine.service;

import com.tienda.unimagTienda.api.dto.OrderDto.*;
import java.util.List;

public interface OrderService {
    OrderResponse create(CreateOrderRequest req);
    OrderResponse get(Long id);
    List<OrderResponse> getAllByCustomerId(Long customerId);
    
    // Transiciones de estado obligatorias
    OrderResponse pay(Long id);
    OrderResponse ship(Long id);
    OrderResponse deliver(Long id);
    OrderResponse cancel(Long id);
}
