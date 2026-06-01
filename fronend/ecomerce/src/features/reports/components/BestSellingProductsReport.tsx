import { useBestSellingProducts } from '../hooks/useBestSellingProducts.ts';
import styles from '../styles/Reports.module.css'; // Assuming a general Reports.module.css

export default function BestSellingProductsReport() {
    const { data: products, loading, error } = useBestSellingProducts();

    if (loading) return <p>Cargando productos más vendidos...</p>;
    if (error) return <p className={styles.error}>Error: {error}</p>;
    if (!products || products.length === 0) return <p>No hay productos más vendidos para mostrar.</p>;

    return (
        <div className={styles.reportCard}>
            <h3 className={styles.reportTitle}>Productos Más Vendidos</h3>
            <table className={styles.reportTable}>
                <thead>
                    <tr>
                        <th>ID Producto</th>
                        <th>Nombre Producto</th>
                        <th>Total Vendido</th>
                        <th>Ingresos Totales</th>
                    </tr>
                </thead>
                <tbody>
                    {products.map((product) => (
                        <tr key={product.productId}>
                            <td>{product.productId}</td>
                            <td>{product.productName}</td>
                            <td>{product.totalSold}</td>
                            <td>${product.totalRevenue.toFixed(2)}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}