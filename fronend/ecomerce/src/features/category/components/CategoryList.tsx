import { useNavigate } from "react-router-dom";
import { useGetCategory } from "../hooks/useGetCategory.ts";
import { useCategoryActivate } from "../hooks/useCategoryActivate.ts"; // Importamos tu hook de activación
import type { CategoryResponse } from "../type/category.ts";
import CategoryCard from "./CategoryCard.tsx";
import styles from "../styles/CategoryList.module.css";
import { categoryDelete } from "../api/categoryApi.ts";
import {ApiError} from "../../../errors/ApiError.ts";

export default function CategoryList() {
    // 1. Consumimos 'updateCategoryInState' en lugar de 'removeCategoryFromState'
    const { data: categories, loading, error, updateCategoryInState } = useGetCategory();
    const { activateCategory } = useCategoryActivate();
    const navigate = useNavigate();

    const handleEdit = (categoryId: number) => {
        navigate(`/admin/categories/${categoryId}`);
    };

    // 2. FUNCIÓN UNIFICADA: Decide si desactivar (Soft Delete) o reactivar según el estado actual
    const handleToggleStatus = async (categoryId: number, currentActiveStatus: boolean, categoryName: string) => {
        if (currentActiveStatus) {
            // SI ESTÁ ACTIVA: Pasamos a desactivarla (Borrado lógico)
            if (window.confirm(`¿Estás seguro de que quieres desactivar la categoría "${categoryName}"?`)) {
                try {
                    await categoryDelete(categoryId);
                    alert("Categoría desactivada con éxito.");
                    // Cambiamos el estado local en memoria a false (Sigue visible pero inactiva)
                    updateCategoryInState(categoryId, { active: false });
                } catch (err: unknown) {

                    if (err instanceof ApiError) {
                        alert(`Error: ${err.message}`);
                    } else {
                        alert("Ocurrió un error inesperado al desactivar la categoría.");
                    }
                }
            }
        } else {
            if (window.confirm(`¿Quieres volver a activar la categoría "${categoryName}"?`)) {
                const success = await activateCategory(categoryId);
                if (success) {
                    alert("Categoría activada con éxito.");
                    updateCategoryInState(categoryId, { active: true });
                } else {
                    alert("No se pudo activar la categoría.");
                }
            }
        }
    };

    if (loading) return <p>Cargando categorías...</p>;
    if (error) return <p>Error: {error}</p>;

    return (
        <div className={styles.container}>
            <h2 className={styles.title}>Categorías</h2>

            <button
                onClick={() => navigate(`/admin/categories/new`)}
                className={styles.createButton}
            >
                Crear +
            </button>

            <div className={styles.categoryList}>
                {categories?.map((category: CategoryResponse) => (
                    <CategoryCard
                        key={category.id}
                        category={category}
                        onEdit={handleEdit}
                        onToggleStatus={handleToggleStatus} 
                    />
                ))}
            </div>
        </div>
    );
}