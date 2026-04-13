package com.tienda.unimagTienda.api.controller;

import com.tienda.unimagTienda.api.dto.ReportDto.*;
import com.tienda.unimagTienda.domine.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/best-selling-products")
    public ResponseEntity<List<BestSellingProductResponse>> getBestSellingProducts() {
        return ResponseEntity.ok(reportService.getBestSellingProducts());
    }

    @GetMapping("/monthly-income")
    public ResponseEntity<List<MonthlyIncomeResponse>> getMonthlyIncome() {
        return ResponseEntity.ok(reportService.getMonthlyIncome());
    }

    @GetMapping("/top-customers")
    public ResponseEntity<List<TopCustomerResponse>> getTopCustomers() {
        return ResponseEntity.ok(reportService.getTopCustomers());
    }

    @GetMapping("/low-stock-products")
    public ResponseEntity<List<LowStockProductResponse>> getLowStockProducts() {
        return ResponseEntity.ok(reportService.getLowStockProducts());
    }
}
