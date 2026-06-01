import { useNavigate } from "react-router-dom";
import { useOrderGetAll } from "../hooks/useOrderGetAll.ts";
import styles from "../styles/OrdersGetAll.module.css";
import type { OrderStatus } from "../type/order.ts";

export default function OrdersGetAll() {
    const { data: orders, loading, error } = useOrderGetAll();
    const navigate = useNavigate();

    if (loading) return <p>Cargando órdenes...</p>;
    if (error) return <p className={styles.error}>Error al cargar órdenes: {error}</p>;
    if (!orders || orders.length === 0) return <p>No hay órdenes para mostrar.</p>;

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

    const handleViewDetails = (orderId: number) => {
        navigate(`/admin/orders/${orderId}`); // Assuming a route for order details
    };

    return (
        <div className={styles.container}>
            <h2 className={styles.title}>Todas las Órdenes</h2>
            <div className={styles.orderList}>
                {orders.map((order) => (
                    <div key={order.id} className={styles.orderCard}>
                        <div className={styles.cardHeader}>
                            <h3>Orden #{order.id}</h3>
                            <span className={getStatusClassName(order.status)}>
                                {order.status}
                            </span>
                        </div>
                        <p><strong>Cliente ID:</strong> {order.customerId}</p>
                        <p><strong>Dirección ID:</strong> {order.addressId}</p>
                        <p><strong>Total:</strong> ${order.total.toFixed(2)}</p>
                        <p><strong>Fecha:</strong> {new Date(order.createdAt).toLocaleString()}</p>
                        <button
                            className={styles.detailsButton}
                            onClick={() => handleViewDetails(order.id)}
                        >
                            Ver Detalles
                        </button>
                    </div>
                ))}
            </div>
        </div>
    );
}