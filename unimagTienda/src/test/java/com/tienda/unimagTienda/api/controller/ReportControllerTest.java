package com.tienda.unimagTienda.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tienda.unimagTienda.api.dto.ReportDto.BestSellingProductResponse;
import com.tienda.unimagTienda.api.dto.ReportDto.LowStockProductResponse;
import com.tienda.unimagTienda.domine.service.ReportService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReportController.class)
public class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReportService reportService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/reports/best-selling-products - Debe retornar lista")
    void getBestSellingProducts_Success() throws Exception {
        BestSellingProductResponse response = new BestSellingProductResponse(1L, "Laptop", 10L);
        when(reportService.getBestSellingProducts()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/reports/best-selling-products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productName").value("Laptop"))
                .andExpect(jsonPath("$[0].totalSold").value(10));
    }

    @Test
    @DisplayName("GET /api/reports/low-stock-products - Debe retornar productos con bajo stock")
    void getLowStockProducts_Success() throws Exception {
        LowStockProductResponse response = new LowStockProductResponse(1L, "Mouse", 2, 5);
        when(reportService.getLowStockProducts()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/reports/low-stock-products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productName").value("Mouse"))
                .andExpect(jsonPath("$[0].availableStock").value(2));
    }
}
