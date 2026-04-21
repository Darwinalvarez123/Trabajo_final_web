package com.tienda.unimagtienda.app.report.dto;

import java.math.BigDecimal;

public interface TopCustomerResponse {

    Long getCustomerId();
    String getCustomerName();
    BigDecimal getTotalSpent();
}