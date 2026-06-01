import UserProductCard from "./UserProductCard.tsx";
import styles from "../styles/UserCatalog.module.css";
import { useProductGetUserAll } from "../../product/hooks/useProductGetAllUser.ts";
import { useAuth } from "../../auth/hooks/useAuth";
import { useAddressGetByCustomerId } from "../../address/hooks/useAddressGetByCustomerId.ts";

export default function UserCatalog() {
    const { data: products, loading, error } = useProductGetUserAll();
    const { customerId } = useAuth();
    const { data: addresses, loading: addrLoading } = useAddressGetByCustomerId(customerId!);

    if (loading || addrLoading) {
        return <p className={styles.loading}>Cargando catálogo...</p>;
    }

    if (error) return <p className={styles.error}>Error: {error}</p>;
    if (!customerId) return <p className={styles.error}>Inicia sesión para ver el catálogo.</p>;

    const primaryAddress = addresses?.[0];

    if (!primaryAddress) {
        return <p className={styles.error}>Debes registrar una dirección de envío para comprar.</p>;
    }

    if (!products?.length) {
        return <p className={styles.loading}>No hay productos disponibles.</p>;
    }

    return (
        <div className={styles.catalogContainer}>
            <h2 className={styles.catalogTitle}>Nuestro Catálogo de Productos</h2>
            <div className={styles.productList}>
                {products.map((product) => (
                    <UserProductCard
                        key={product.id}
                        product={product}
                        customerId={customerId}
                        addressId={primaryAddress.id}
                    />
                ))}
            </div>
        </div>
    );
}