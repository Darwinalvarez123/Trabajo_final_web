package com.tienda.unimagtienda.app.report.service;

import com.tienda.unimagtienda.app.report.dto.BestSellingProductResponse;
import com.tienda.unimagtienda.app.report.dto.LowStockProductResponse;
import com.tienda.unimagtienda.app.report.dto.MonthlyIncomeResponse;
import com.tienda.unimagtienda.app.report.dto.TopCustomerResponse;

import java.util.List;

public interface ReportService {

    List<BestSellingProductResponse> bestSellingProducts();

    List<MonthlyIncomeResponse> monthlyIncome();

    List<TopCustomerResponse> topCustomers();

    List<LowStockProductResponse> lowStockProducts();
}