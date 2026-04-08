package com.tienda.unimagTienda.domine.service;

import com.tienda.unimagTienda.api.dto.ReportDto.*;
import com.tienda.unimagTienda.domine.repository.OrderItemRepository;
import com.tienda.unimagTienda.domine.repository.OrderRepository;
import com.tienda.unimagTienda.domine.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final InventoryRepository inventoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<BestSellingProductResponse> getBestSellingProducts() {
        return orderItemRepository.findBestSellingProducts().stream()
                .map(obj -> new BestSellingProductResponse(
                        (Long) obj[0],
                        (String) obj[1],
                        (Long) obj[2]
                ))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MonthlyIncomeResponse> getMonthlyIncome() {
        return orderRepository.findMonthlyIncome().stream()
                .map(obj -> new MonthlyIncomeResponse(
                        (LocalDateTime) obj[0],
                        (BigDecimal) obj[1]
                ))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TopCustomerResponse> getTopCustomers() {
        return orderRepository.findTopCustomers().stream()
                .map(obj -> new TopCustomerResponse(
                        obj[0] + " " + obj[1],
                        (BigDecimal) obj[2]
                ))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LowStockProductResponse> getLowStockProducts() {
        return inventoryRepository.findInventoriesWithLowStock().stream()
                .map(inventory -> new LowStockProductResponse(
                        inventory.getProduct().getId(),
                        inventory.getProduct().getName(),
                        inventory.getAvailableStock(),
                        inventory.getMinStock()
                ))
                .collect(Collectors.toList());
    }
}
