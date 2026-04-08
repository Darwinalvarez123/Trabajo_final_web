package com.tienda.unimagTienda.domine.service;

import com.tienda.unimagTienda.api.dto.OrderStatusHistoryDto.*;
import java.util.List;

public interface OrderStatusHistoryService {
    List<OrderStatusHistoryResponse> getHistoryByOrderId(Long orderId);
    void addHistory(Long orderId, String comment);
}
