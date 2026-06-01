import { useLowStockProducts } from '../hooks/useLowStockProducts.ts';
import styles from '../styles/Reports.module.css';

export default function LowStockProductsReport() {
    const { data: products, loading, error } = useLowStockProducts();

    if (loading) return <p>Cargando productos con bajo stock...</p>;
    if (error) return <p className={styles.error}>Error: {error}</p>;
    if (!products || products.length === 0) return <p>No hay productos con bajo stock para mostrar.</p>;

    return (
        <div className={styles.reportCard}>
            <h3 className={styles.reportTitle}>Productos con Bajo Stock</h3>
            <table className={styles.reportTable}>
                <thead>
                    <tr>
                        <th>ID Producto</th>
                        <th>Nombre Producto</th>
                        <th>Stock Disponible</th>
                        <th>Stock Mínimo</th>
                    </tr>
                </thead>
                <tbody>
                    {products.map((product) => (
                        <tr key={product.productId}>
                            <td>{product.productId}</td>
                            <td>{product.productName}</td>
                            <td>{product.availableStock}</td>
                            <td>{product.minStock}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}