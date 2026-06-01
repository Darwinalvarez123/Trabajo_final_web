import {useProductCreate} from "../hooks/useProductCreate.ts";
import type {CreateProductRequest} from "../type/product.ts";
import {ProductForm} from "./ProductForm.tsx";
import {useNavigate} from "react-router-dom";

export default function ProductCreate() {
    const {createProduct, loading: isCreating, error: createError} = useProductCreate();
    const navigate = useNavigate();
    const dataForm: CreateProductRequest = {
        sku: "",
        name: "",
        description: "",
        price: 0.01,
        categoryId: 0,
    };

    const handleCreate = async (data: CreateProductRequest): Promise<void> => {
        console.log("📦 BODY ENVIADO AL HOOK:", JSON.stringify(data, null, 2));
        const newProduct = await createProduct(data);
        if (newProduct) {
            alert("¡Producto creado con éxito!");
            navigate("/admin");
        }
    };

    return (
        <div>
            <h2>Crear Producto</h2>

            {createError && <p>Error al crear: {createError}</p>}


            <ProductForm
                initialData={dataForm}
                onSubmit={handleCreate}
                isSubmitting={isCreating}
                submitButtonText="Crear Producto"
            />
        </div>
    );
}