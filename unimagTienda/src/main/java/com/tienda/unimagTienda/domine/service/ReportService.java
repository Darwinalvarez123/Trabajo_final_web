package com.tienda.unimagTienda.domine.service;

import com.tienda.unimagTienda.api.dto.ReportDto.*;
import java.util.List;

public interface ReportService {
    List<BestSellingProductResponse> getBestSellingProducts();
    List<MonthlyIncomeResponse> getMonthlyIncome();
    List<TopCustomerResponse> getTopCustomers();
    List<LowStockProductResponse> getLowStockProducts();
}
