package com.tienda.unimagtienda.app.order.dto;

import com.tienda.unimagtienda.app.orderItem.dto.CreateOrderItemRequest;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateOrderRequest(

        @NotNull(message = "customerId is required")
        Long customerId,

        @NotNull(message = "addressId is required")
        Long addressId,

        @NotEmpty(message = "Order must have at least one item")
        List<CreateOrderItemRequest> items

) {}