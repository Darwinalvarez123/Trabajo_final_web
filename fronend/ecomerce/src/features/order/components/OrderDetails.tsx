import { useState } from "react";
import { useParams } from "react-router-dom";
import { useOrderGetById } from "../hooks/useOrderGetById.ts";
import { useOrderHistory } from "../hooks/useOrderHistory.ts";
import { useOrderPay } from "../hooks/useOrderPay.ts";
import { useOrderShip } from "../hooks/useOrderShip.ts";
import { useOrderDeliver } from "../hooks/useOrderDeliver.ts";
import { useOrderCancel } from "../hooks/useOrderCancel.ts";
import type { OrderStatus } from "../type/order.ts";
import styles from "../styles/OrderDetails.module.css";

export default function OrderDetails() {
    const { orderId } = useParams<{ orderId: string }>();
    const orderIdNum = orderId ? parseInt(orderId, 10) : 0;

    const [refreshKey, setRefreshKey] = useState(0);

    const { data: order, loading: orderLoading, error: orderError, refresh } = useOrderGetById(orderIdNum);
    const { data: history, loading: historyLoading, error: historyError } = useOrderHistory(orderIdNum, refreshKey);

    const { pay, loading: payLoading } = useOrderPay();
    const { ship, loading: shipLoading } = useOrderShip();
    const { deliver, loading: deliverLoading } = useOrderDeliver();
    const { cancel, loading: cancelLoading } = useOrderCancel();

    const handleStatusChange = async (action: 'pay' | 'ship' | 'deliver' | 'cancel') => {
        if (!orderIdNum) return;

        try {
            let success = false;
            if (action === 'pay') success = await pay(orderIdNum);
            else if (action === 'ship') success = await ship(orderIdNum);
            else if (action === 'deliver') success = await deliver(orderIdNum);
            else if (action === 'cancel') success = await cancel(orderIdNum);
            console.log("success:", success);
            alert("¡Éxito!");
            refresh();

        } catch (err: unknown) {
            const errorMessage = err instanceof Error ? err.message : "Error desconocido";
            console.error("Error capturado:", err);

            if (errorMessage.includes("409")) {
                alert("El estado de la orden ha cambiado. Recargando datos...");
                refresh();
                setRefreshKey((k) => k + 1);
            } else {
                alert(`Error: ${errorMessage}`);
            }
        }
    };

    const getStatusClassName = (status: OrderStatus) => {
        switch (status) {
            case "CREATED": return styles.statusCreated;
            case "PAID": return styles.statusPaid;
            case "SHIPPED": return styles.statusShipped;
            case "DELIVERED": return styles.statusDelivered;
            case "CANCELLED": return styles.statusCancelled;
            default: return '';
        }
    };

    if (orderLoading || historyLoading) return <p>Cargando detalles de la orden...</p>;
    if (orderError) return <p className={styles.error}>Error: {orderError}</p>;
    if (!order) return <p>No se encontró la orden.</p>;
    if(historyError) return <p>Error: {historyError}</p>

    return (
        <div className={styles.container}>
            <h2 className={styles.title}>Detalles de la Orden #{order.id}</h2>

            <div className={styles.orderSummary}>
                <p><strong>Estado:</strong> <span className={getStatusClassName(order.status)}>{order.status}</span></p>
                <p><strong>Total:</strong> ${order.total.toFixed(2)}</p>
                <p><strong>Fecha de Creación:</strong> {new Date(order.createdAt).toLocaleString()}</p>
            </div>

            <div className={styles.actionButtons}>
                {order.status === "CREATED" && (
                    <button onClick={() => handleStatusChange('pay')} disabled={payLoading} className={styles.payButton}>
                        {payLoading ? 'Procesando...' : 'Marcar como Pagada'}
                    </button>
                )}
                {order.status === "PAID" && (
                    <button onClick={() => handleStatusChange('ship')} disabled={shipLoading} className={styles.shipButton}>
                        {shipLoading ? 'Procesando...' : 'Marcar como Enviada'}
                    </button>
                )}
                {order.status === "SHIPPED" && (
                    <button onClick={() => handleStatusChange('deliver')} disabled={deliverLoading} className={styles.deliverButton}>
                        {deliverLoading ? 'Procesando...' : 'Marcar como Entregada'}
                    </button>
                )}
                {(order.status === "CREATED" || order.status === "PAID") && (
                    <button onClick={() => handleStatusChange('cancel')} disabled={cancelLoading} className={styles.cancelButton}>
                        {cancelLoading ? 'Procesando...' : 'Cancelar Orden'}
                    </button>
                )}
            </div>

            <h3 className={styles.sectionTitle}>Artículos de la Orden</h3>
            <table className={styles.itemsTable}>
                <thead>
                <tr><th>Producto ID</th><th>Cantidad</th><th>Subtotal</th></tr>
                </thead>
                <tbody>
                {order.items.map((item, index) => (
                    <tr key={index}>
                        <td>{item.productId}</td>
                        <td>{item.quantity}</td>
                        <td>${item.subtotal.toFixed(2)}</td>
                    </tr>
                ))}
                </tbody>
            </table>

            <h3 className={styles.sectionTitle}>Historial de Estado</h3>
            {history && (
                <ul className={styles.historyList}>
                    {history.map((entry) => (
                        <li key={entry.id}>
                            <span className={getStatusClassName(entry.status)}>{entry.status}</span> el {new Date(entry.createdAt).toLocaleString()}
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
}