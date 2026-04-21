package com.tienda.unimagtienda.app.report.dto;

import java.math.BigDecimal;

public interface MonthlyIncomeResponse {

    Integer getYear();
    Integer getMonth();
    BigDecimal getTotalIncome();
}