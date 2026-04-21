package com.tienda.unimagtienda.app.report.service;

import com.tienda.unimagtienda.app.report.dto.BestSellingProductResponse;
import com.tienda.unimagtienda.app.report.dto.LowStockProductResponse;
import com.tienda.unimagtienda.app.report.dto.MonthlyIncomeResponse;
import com.tienda.unimagtienda.app.report.dto.TopCustomerResponse;
import com.tienda.unimagtienda.app.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;

    @Override
    public List<BestSellingProductResponse> bestSellingProducts() {
        return reportRepository.bestSellingProducts();
    }

    @Override
    public List<MonthlyIncomeResponse> monthlyIncome() {
        return reportRepository.monthlyIncome();
    }

    @Override
    public List<TopCustomerResponse> topCustomers() {
        return reportRepository.topCustomers();
    }

    @Override
    public List<LowStockProductResponse> lowStockProducts() {
        return reportRepository.lowStockProducts();
    }
}