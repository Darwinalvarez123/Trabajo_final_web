import { useProductGetAll } from "../hooks/useProductGetAll.ts";
import { useNavigate } from "react-router-dom";
import styles from "../styles/ProductsGetAll.module.css";
import ProductCard from "./ProductCard.tsx";
import { useProductDelete } from "../hooks/useProductDelete.ts";
import { useProductUpdate } from "../hooks/useProductUpdate.ts"; // Importar useProductUpdate

export default function ProductsGetAll() {
    const { data, loading, error, updateProductInState} = useProductGetAll();
    const { deleteProduct, loading: isDeleting, error: deleteError } = useProductDelete();
    const { updateProduct, loading: isUpdating, error: updateError } = useProductUpdate(); // Obtener updateProduct
    const navigate = useNavigate();

    const handleEdit = (productId: number) => {
        navigate(`/admin/products/${productId}`);
    };
    const handleInventory = (productId: number) => {
        navigate(`/admin/inventory/${productId}`);
    };
    const handleDelete = async (productId: number, productName: string) => {
        if (window.confirm(`¿Estás seguro de que quieres eliminar el producto "${productName}"?`)) {
            const success = await deleteProduct(productId);
            if (success) {
                alert("Producto eliminado con éxito.");

            } else {
                alert(`Error al eliminar el producto: ${deleteError}`);
            }
        }
    };

    const handleToggleActive = async (productId: number, currentActiveStatus: boolean) => {
        const newActiveStatus = !currentActiveStatus;
        const confirmMessage = newActiveStatus
            ? "¿Estás seguro de que quieres activar este producto?"
            : "¿Estás seguro de que quieres desactivar este producto?";

        if (window.confirm(confirmMessage)) {
            const success = await updateProduct(productId, { active: newActiveStatus });
            if (success) {
                alert(`Producto ${newActiveStatus ? "activado" : "desactivado"} con éxito.`);
                updateProductInState(productId, { active: newActiveStatus });

            } else {
                alert(`Error al cambiar el estado del producto: ${updateError}`);
            }
        }
    };

    if (loading) return <p>Cargando productos...</p>;
    if (error) return <p>Error al cargar productos: {error}</p>;

    return (
        <div className={styles.container}>
            <h2 className={styles.title}>Productos</h2>
            <button
                onClick={() => navigate("/admin/products/new")}
                className={styles.createButton}
            >
                Crear +
            </button>

            {(isDeleting || isUpdating) && <p>Procesando...</p>}
            {deleteError && <p>Error al eliminar: {deleteError}</p>}
            {updateError && <p>Error al actualizar: {updateError}</p>}

            <div className={styles.productList}>
                {data?.map((product) => (
                    <ProductCard
                        key={product.id}
                        product={product}
                        onEdit={handleEdit}
                        onDelete={handleDelete}
                        onToggleActive={handleToggleActive}
                        onInventory={handleInventory}
                    />
                ))}
            </div>
        </div>
    );
}