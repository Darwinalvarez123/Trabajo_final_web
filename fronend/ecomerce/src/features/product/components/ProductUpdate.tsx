import { useParams, useNavigate } from "react-router-dom"; // 🚀 Para capturar el ID y redireccionar
import { useProductUpdate } from "../hooks/useProductUpdate";
import { useProductGetId } from "../hooks/useProducGetId.ts"; // 🚀 Tu nuevo hook
import type { UpdateProductRequest } from "../type/product";
import { ProductForm } from "./ProductForm.tsx";

export default function ProductUpdate() {
    const { id } = useParams<{ id: string }>();
    const productId = Number(id);

    const navigate = useNavigate();

    const { data: product, loading: isLoadingProduct, error: loadError } = useProductGetId(productId);

    const { updateProduct, loading: isUpdating, error: updateError } = useProductUpdate();

    const handleUpdate = async (data: UpdateProductRequest): Promise<void> => {

        await updateProduct(productId, data);
        alert("¡Producto actualizado con éxito!");

        navigate("/admin");
    };

    if (isLoadingProduct) return <p>Cargando datos del producto...</p>;
    if (loadError) return <p>Error al cargar producto: {loadError}</p>;
    if (!product) return <p>No se encontró el producto especificado.</p>;
    const productToEdit: UpdateProductRequest = {
        sku: product.sku,
        name: product.name,
        description: product.description || "",
        price: Number(product.price),
        categoryId: product.category.id,
    };

    return (
        <>
            <h2>Actualizar Producto: {product.name}</h2>

            {updateError && <p>Error al actualizar: {updateError}</p>}

            <ProductForm
                initialData={productToEdit}
                onSubmit={handleUpdate}
                isSubmitting={isUpdating}
                submitButtonText="Actualizar Producto"
            />
        </>
    );
}