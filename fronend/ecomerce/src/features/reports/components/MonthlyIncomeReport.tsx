import { useMonthlyIncome } from '../hooks/useMonthlyIncome.ts';
import styles from '../styles/Reports.module.css';

export default function MonthlyIncomeReport() {
    const { data: incomeData, loading, error } = useMonthlyIncome();

    if (loading) return <p>Cargando ingresos mensuales...</p>;
    if (error) return <p className={styles.error}>Error: {error}</p>;
    if (!incomeData || incomeData.length === 0) return <p>No hay datos de ingresos mensuales para mostrar.</p>;

    return (
        <div className={styles.reportCard}>
            <h3 className={styles.reportTitle}>Ingresos Mensuales</h3>
            <table className={styles.reportTable}>
                <thead>
                    <tr>
                        <th>Año</th>
                        <th>Mes</th>
                        <th>Ingresos Totales</th>
                    </tr>
                </thead>
                <tbody>
                    {incomeData.map((item, index) => (
                        <tr key={index}> {/* Using index as key, assuming no unique ID for monthly income */}
                            <td>{item.year}</td>
                            <td>{item.month}</td>
                            <td>${item.totalIncome.toFixed(2)}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}