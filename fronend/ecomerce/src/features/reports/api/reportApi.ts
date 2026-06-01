import { fetchClient } from "../../../services/ApiCliente.ts";
import type {
    BestSellingProductResponse,
    LowStockProductResponse,
    MonthlyIncomeResponse,
    TopCustomerResponse
} from "../type/report.ts";

export function getBestSellingProducts(): Promise<BestSellingProductResponse[]> {
    return fetchClient("/reports/best-selling-products");
}

export function getLowStockProducts(): Promise<LowStockProductResponse[]> {
    return fetchClient("/reports/low-stock-products");
}

export function getTopCustomers(): Promise<TopCustomerResponse[]> {
    return fetchClient("/reports/top-customers");
}

export function getMonthlyIncome(): Promise<MonthlyIncomeResponse[]> {
    return fetchClient("/reports/monthly-income");
}