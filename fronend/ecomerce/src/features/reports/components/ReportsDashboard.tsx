import BestSellingProductsReport from './BestSellingProductsReport.tsx';
import LowStockProductsReport from './LowStockProductsReport.tsx';
import TopCustomerReport from './TopCustomerReport.tsx';
import MonthlyIncomeReport from './MonthlyIncomeReport.tsx';
import styles from '../styles/Reports.module.css';

export default function ReportsDashboard() {
    return (
        <div className={styles.reportsDashboardContainer}>
            <h1 className={styles.dashboardTitle}>Panel de Reportes</h1>
            <div className={styles.dashboardGrid}>
                <BestSellingProductsReport />
                <LowStockProductsReport />
                <TopCustomerReport />
                <MonthlyIncomeReport />
            </div>
        </div>
    );
}