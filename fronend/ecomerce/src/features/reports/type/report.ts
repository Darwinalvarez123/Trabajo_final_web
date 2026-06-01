export interface BestSellingProductResponse {
    productId: number;
    productName: string;
    totalSold: number;
    totalRevenue: number;
}

export interface LowStockProductResponse {
    productId: number;
    productName: string;
    availableStock: number;
    minStock: number;
}

export interface TopCustomerResponse {
    customerId: number;
    customerName: string;
    totalSpent: number;
}

export interface MonthlyIncomeResponse {
    year: number;
    month: number;
    totalIncome: number;
}