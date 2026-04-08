package com.tienda.unimagTienda.api.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ReportDto {
    public record BestSellingProductResponse(
            Long productId,
            String productName,
            Long totalSold) implements Serializable {}

    public record MonthlyIncomeResponse(
            LocalDateTime month,
            BigDecimal totalIncome) implements Serializable {}

    public record TopCustomerResponse(
            String customerName,
            BigDecimal totalSpent) implements Serializable {}

    public record LowStockProductResponse(
            Long productId,
            String productName,
            Integer availableStock,
            Integer minStock) implements Serializable {}
}
