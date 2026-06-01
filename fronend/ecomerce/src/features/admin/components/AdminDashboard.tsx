import {useAdminDashboard} from "../hooks/useAdminDashboard";
import styles from '../styles/AdminDashboard.module.css'; // Ruta corregida

export default function AdminDashboard() {
    const {data, loading, error} = useAdminDashboard();

    if (loading) return <p>Cargando...</p>;
    if (error) return <p>Error: {error}</p>;

    return (
        <div className={styles.adminDashboardContainer}>
            <h2 className={styles.title}>Dashboard de Administración</h2>

            <div className={styles.statsGrid}>
                <div className={`${styles.statCard} ${styles.totalUsers}`}>
                    <h3>Total Usuarios</h3>
                    <p>{data?.totalUsers}</p>
                </div>
                <div className={`${styles.statCard} ${styles.totalAdmins}`}>
                    <h3>Total Administradores</h3>
                    <p>{data?.totalAdmins}</p>
                </div>
                <div className={`${styles.statCard} ${styles.totalNormalUsers}`}>
                    <h3>Usuarios Normales</h3>
                    <p>{data?.totalNormalUsers}</p>
                </div>
                <div className={`${styles.statCard} ${styles.totalProducts}`}>
                    <h3>Total Productos</h3>
                    <p>{data?.totalProducts}</p>
                </div>
                <div className={`${styles.statCard} ${styles.lowStockProducts}`}>
                    <h3>Productos con Stock Bajo</h3>
                    <p>{data?.lowStockProducts}</p>
                </div>
            </div>
        </div>
    );
}