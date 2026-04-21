package com.tienda.unimagtienda.app.report.repository;
import com.tienda.unimagtienda.app.order.entity.Order;
import com.tienda.unimagtienda.app.orderItem.entity.OrderItem;
import com.tienda.unimagtienda.app.report.dto.BestSellingProductResponse;
import com.tienda.unimagtienda.app.report.dto.LowStockProductResponse;
import com.tienda.unimagtienda.app.report.dto.MonthlyIncomeResponse;
import com.tienda.unimagtienda.app.report.dto.TopCustomerResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Order, Long> {

    @Query("""
        SELECT
            p.id as productId,
            p.name as productName,
            SUM(oi.quantity) as totalSold,
            SUM(oi.subtotal) as totalRevenue
        FROM OrderItem oi
        JOIN oi.product p
        GROUP BY p.id, p.name
        ORDER BY totalSold DESC
    """)
    List<BestSellingProductResponse> bestSellingProducts();


    @Query("""
        SELECT
            YEAR(o.createdAt) as year,
            MONTH(o.createdAt) as month,
            SUM(o.total) as totalIncome
        FROM Order o
        WHERE o.status = 'DELIVERED'
        GROUP BY YEAR(o.createdAt), MONTH(o.createdAt)
        ORDER BY YEAR(o.createdAt) DESC, MONTH(o.createdAt) DESC
    """)
    List<MonthlyIncomeResponse> monthlyIncome();


    @Query("""
        SELECT
            c.id as customerId,
            CONCAT(c.firstName, ' ', c.lastName) as customerName,
            SUM(o.total) as totalSpent
        FROM Order o
        JOIN o.customer c
        GROUP BY c.id, c.firstName, c.lastName
        ORDER BY totalSpent DESC
    """)
    List<TopCustomerResponse> topCustomers();


    @Query("""
        SELECT
            p.id as productId,
            p.name as productName,
            i.availableStock as availableStock,
            i.minStock as minStock
        FROM Inventory i
        JOIN i.product p
        WHERE i.availableStock <= i.minStock
    """)
    List<LowStockProductResponse> lowStockProducts();
}