import {useNavigate, useParams} from "react-router-dom";
import useCategoryGetId from "../hooks/useCategoryGetId.ts";
import {useUpdateCategory} from "../hooks/useUpdateCategory.ts";
import type {UpdateProductRequest} from "../../product/type/product.ts";
import type {UpdateCategoryRequest} from "../type/category.ts";
import {CategoryForm} from "./CategoryForm.tsx";

export default function CategoryUpdate() {
    const {id} = useParams<{ id: string }>();
    const categoryId = Number(id);
    const {data: category, loading: isLoadingCategory, error: loadError} = useCategoryGetId(categoryId);
    const {updateCategory, loading: isUpdating, error: updateError} = useUpdateCategory();
    const navigate = useNavigate();
    const handleUpdate = async (data: UpdateProductRequest): Promise<void> => {

        await updateCategory(categoryId, data);
        alert("¡Categoria actualizado con éxito!");

        navigate("/admin");
    };
    if (isLoadingCategory) return <p>Cargando datos del producto...</p>;
    if (loadError) return <p>Error al cargar producto: {loadError}</p>;
    if (!category) return <p>No se encontró el producto especificado.</p>;

    const categoryToEdit: UpdateCategoryRequest = {
        name: category.name,
        description: category.description || "",
    };

    return (
        <>
            <h2>Actualizar Categoría: {category.name}</h2>

            {updateError && <p style={{ color: "red" }}>Error al actualizar: {updateError}</p>}

            <CategoryForm
                initialData={categoryToEdit}
                onSubmit={handleUpdate}
                isSubmitting={isUpdating}
                submitButtonText="Actualizar Categoría"
            />
        </>
    );


}


