package com.tienda.unimagtienda.app.report.service;

import com.tienda.unimagtienda.app.report.dto.BestSellingProductResponse;
import com.tienda.unimagtienda.app.report.dto.LowStockProductResponse;
import com.tienda.unimagtienda.app.report.dto.MonthlyIncomeResponse;
import com.tienda.unimagtienda.app.report.dto.TopCustomerResponse;
import com.tienda.unimagtienda.app.report.repository.ReportRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceImplTest {

    @Mock
    private ReportRepository reportRepository;

    @InjectMocks
    private ReportServiceImpl service;

    @Test
    @DisplayName("ReportService: retorna productos más vendidos")
    void shouldReturnBestSellingProducts() {
        BestSellingProductResponse item = mock(BestSellingProductResponse.class);
        var data = List.of(item);

        when(reportRepository.bestSellingProducts()).thenReturn(data);
        var result = service.bestSellingProducts();

        assertThat(result).hasSize(1);
        verify(reportRepository).bestSellingProducts();
    }

    @Test
    @DisplayName("ReportService: retorna ingresos mensuales")
    void shouldReturnMonthlyIncome() {
        MonthlyIncomeResponse item = mock(MonthlyIncomeResponse.class);

        var data = List.of(item);

        when(reportRepository.monthlyIncome()).thenReturn(data);

        var result = service.monthlyIncome();

        assertThat(result).hasSize(1);
        verify(reportRepository).monthlyIncome();
    }

    @Test
    @DisplayName("ReportService: retorna mejores clientes")
    void shouldReturnTopCustomers() {
        TopCustomerResponse item = mock(TopCustomerResponse.class);
        var data = List.of(item);

        when(reportRepository.topCustomers()).thenReturn(data);
        var result = service.topCustomers();

        assertThat(result).hasSize(1);
        verify(reportRepository).topCustomers();
    }

    @Test
    @DisplayName("ReportService: retorna productos con bajo stock")
    void shouldReturnLowStockProducts() {
        LowStockProductResponse item = mock(LowStockProductResponse.class);
        var data = List.of(item);

        when(reportRepository.lowStockProducts()).thenReturn(data);
        var result = service.lowStockProducts();

        assertThat(result).hasSize(1);
        verify(reportRepository).lowStockProducts();
    }
}