package com.tienda.unimagtienda.app.admin.dto;

public record AdminDashboardResponse(
        long totalUsers,
        long totalAdmins,
        long totalNormalUsers,
        long totalProducts,
        long lowStockProducts
) {}