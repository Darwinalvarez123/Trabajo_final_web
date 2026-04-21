package com.tienda.unimagtienda.app.report.controller;

import com.tienda.unimagtienda.app.report.dto.*;
import com.tienda.unimagtienda.app.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/best-selling-products")
    public ResponseEntity<List<BestSellingProductResponse>> bestSellingProducts() {
        return ResponseEntity.ok(reportService.bestSellingProducts());
    }

    @GetMapping("/monthly-income")
    public ResponseEntity<List<MonthlyIncomeResponse>> monthlyIncome() {
        return ResponseEntity.ok(reportService.monthlyIncome());
    }

    @GetMapping("/top-customers")
    public ResponseEntity<List<TopCustomerResponse>> topCustomers() {
        return ResponseEntity.ok(reportService.topCustomers());
    }

    @GetMapping("/low-stock-products")
    public ResponseEntity<List<LowStockProductResponse>> lowStockProducts() {
        return ResponseEntity.ok(reportService.lowStockProducts());
    }
}