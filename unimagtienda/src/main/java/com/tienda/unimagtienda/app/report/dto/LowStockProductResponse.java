package com.tienda.unimagtienda.app.report.dto;

public interface LowStockProductResponse {

    Long getProductId();
    String getProductName();
    Integer getAvailableStock();
    Integer getMinStock();
}