package com.tienda.unimagtienda.app.report.dto;
import java.math.BigDecimal;

public interface BestSellingProductResponse {

    Long getProductId();
    String getProductName();
    Long getTotalSold();
    BigDecimal getTotalRevenue();
}