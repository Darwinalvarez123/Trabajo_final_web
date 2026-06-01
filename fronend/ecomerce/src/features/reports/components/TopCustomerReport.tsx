import { useTopCustomers } from '../hooks/useTopCustomers.ts';
import styles from '../styles/Reports.module.css';

export default function TopCustomerReport() {
    const { data: customers, loading, error } = useTopCustomers();

    if (loading) return <p>Cargando clientes principales...</p>;
    if (error) return <p className={styles.error}>Error: {error}</p>;
    if (!customers || customers.length === 0) return <p>No hay clientes principales para mostrar.</p>;

    return (
        <div className={styles.reportCard}>
            <h3 className={styles.reportTitle}>Clientes Principales</h3>
            <table className={styles.reportTable}>
                <thead>
                    <tr>
                        <th>ID Cliente</th>
                        <th>Nombre Cliente</th>
                        <th>Total Gastado</th>
                    </tr>
                </thead>
                <tbody>
                    {customers.map((customer) => (
                        <tr key={customer.customerId}>
                            <td>{customer.customerId}</td>
                            <td>{customer.customerName}</td>
                            <td>${customer.totalSpent.toFixed(2)}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}