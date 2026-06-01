import type { UserProduct } from "../type/user.ts";
import { useOrderCreate } from "../../order/hooks/useOrderCreate.ts";
import type { CreateOrderItemRequest } from "../../order/type/order.ts";
import styles from "../styles/UserProductCard.module.css";

interface UserProductCardProps {
    product: UserProduct;
    customerId: number;
    addressId: number;
    onOrderCreated?: () => void;
}

export default function UserProductCard({ product, customerId, addressId, onOrderCreated }: UserProductCardProps) {
    const { createOrder, loading, error } = useOrderCreate();

    const handleBuyClick = async () => {
        if (!customerId || !addressId) {
            alert("Error: Customer ID o Address ID no definidos. No se puede crear la orden.");
            return;
        }

        const orderItem: CreateOrderItemRequest = {
            productId: product.id,
            quantity: 1,
        };

        const orderData = {
            customerId: customerId,
            addressId: addressId,
            items: [orderItem],
        };

        const newOrder = await createOrder(orderData);

        if (newOrder) {
            alert(`Orden #${newOrder.id} creada con éxito para el producto: ${product.name}`);
            onOrderCreated?.();
        } else {
            alert(`Error al crear la orden: ${error || "Error desconocido"}`);
        }
    };

    return (
        <div className={styles.card}>
            <h3>{product.name}</h3>
            <p>{product.description}</p>
            <p className={styles.price}>${parseFloat(product.price).toFixed(2)}</p>
            <p>SKU: {product.sku}</p>
            <p>Categoría: {product.category.name}</p>
            <button onClick={handleBuyClick} disabled={loading}>
                {loading ? "Comprando..." : "Comprar"}
            </button>
            {error && <p className={styles.error}>{error}</p>}
        </div>
    );
}
