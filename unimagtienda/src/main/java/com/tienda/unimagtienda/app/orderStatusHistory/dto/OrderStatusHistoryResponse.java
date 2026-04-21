package com.tienda.unimagtienda.app.orderStatusHistory.dto;

import java.time.LocalDateTime;

public record OrderStatusHistoryResponse(
        Long id,
        String status,
        String comment,
        LocalDateTime createdAt
) {}
