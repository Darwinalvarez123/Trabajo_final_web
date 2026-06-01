import { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { UseGetInventory } from "../hooks/useGetInventory.ts";
import { useUpdateInventory } from "../hooks/useUpdateInvetory.ts";
import styles from "../styles/IventoryList.module.css";

export default function InventarioList() {
    const { productId } = useParams();
    const id = parseInt(productId as string);
    const { data, loading, error, updateInventoryInState } = UseGetInventory(id);
    const { updateInventory, loading: updating } = useUpdateInventory();
    const navigate = useNavigate();

    const [isEditing, setIsEditing] = useState(false);
    const [tempStock, setTempStock] = useState(0);
    const [tempMinStock, setTempMinStock] = useState(0);

    if (loading) return <p>Cargando...</p>;
    if (error) return <p>Error: {error}</p>;
    if (!data) return <p>No se encontraron datos.</p>;

    const handleEdit = () => {
        setTempStock(data.availableStock);
        setTempMinStock(data.minStock);
        setIsEditing(true);
    };

    const handleSave = async () => {
        const success = await updateInventory(id, {
            availableStock: tempStock,
            minStock: tempMinStock
        });

        if (success) {
            updateInventoryInState({ availableStock: tempStock, minStock: tempMinStock });
            setIsEditing(false);
        }
    };

    const percentage = Math.min((data.availableStock / (data.minStock * 2)) * 100, 100);
    let barClass = styles.barSafe;
    if (data.availableStock <= data.minStock) barClass = styles.barDanger;
    else if (data.availableStock <= data.minStock * 1.5) barClass = styles.barWarning;

    return (
        <div className={styles.container}>
            <h2 className={styles.title}>Gestión de Inventario</h2>

            <div className={styles.dataGrid}>
                <div className={styles.dataItem}>
                    <label>Producto ID</label>
                    <span className={styles.value}>{data.productId}</span>
                </div>
                <div className={styles.dataItem}>
                    <label>Registro ID</label>
                    <span className={styles.value}>{data.id}</span>
                </div>
                <div className={styles.dataItem}>
                    <label>Stock Disponible</label>
                    {isEditing ? (
                        <div className={styles.counter}>
                            <button onClick={() => setTempStock(prev => Math.max(0, prev - 1))}>-</button>
                            <span>{tempStock}</span>
                            <button onClick={() => setTempStock(prev => prev + 1)}>+</button>
                        </div>
                    ) : (
                        <span className={styles.value}>{data.availableStock}</span>
                    )}
                </div>
                <div className={styles.dataItem}>
                    <label>Stock Mínimo</label>
                    {isEditing ? (
                        <div className={styles.counter}>
                            <button onClick={() => setTempMinStock(prev => Math.max(0, prev - 1))}>-</button>
                            <span>{tempMinStock}</span>
                            <button onClick={() => setTempMinStock(prev => prev + 1)}>+</button>
                        </div>
                    ) : (
                        <span className={styles.value}>{data.minStock}</span>
                    )}
                </div>
            </div>

            <div className={styles.dataItem}>
                <label>Nivel de Inventario</label>
                <div className={styles.progressContainer}>
                    <div
                        className={`${styles.progressBar} ${barClass}`}
                        style={{ width: `${percentage}%` }}
                    />
                </div>
                <small style={{ marginTop: '5px', color: '#6b7280' }}>
                    {data.availableStock} unidades disponibles (Mínimo: {data.minStock})
                </small>
            </div>

            <div className={styles.buttonGroup}>
                <button
                    className={styles.backButton}
                    onClick={() => navigate("/admin/products")}
                >
                    Volver
                </button>
                <button
                    className={styles.editButton}
                    onClick={isEditing ? handleSave : handleEdit}
                    disabled={updating}
                >
                    {updating ? "Guardando..." : (isEditing ? "Guardar" : "Editar Stock")}
                </button>
            </div>
        </div>
    );
}