import * as React from "react";
import {useState} from "react";
import type {CreateProductRequest, UpdateProductRequest} from "../type/product.ts";
import styles from "../styles/ProductForm.module.css";
import type {CategoryResponse} from "../../category/type/category.ts";
import {useGetCategory} from "../../category/hooks/useGetCategory.ts";

interface ProductFormProps<T> {
    initialData: T;
    onSubmit: (data: T) => Promise<void>;
    isSubmitting: boolean;
    submitButtonText: string;
}

export function ProductForm<T extends CreateProductRequest | UpdateProductRequest>({
                                                                                       initialData,
                                                                                       onSubmit,
                                                                                       isSubmitting,
                                                                                       submitButtonText
                                                                                   }: ProductFormProps<T>) {
    const {data: categories, loading: isLoadingCategories, error: categoriesError} = useGetCategory();
    const [formData, setFormData] = useState(initialData);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
        const {name, value} = e.target;
        let processedValue: string | number = value;

        if (name === "price") {

            let sanitizedValue = value.replace(/[^0-9.]/g, '');
            const parts = sanitizedValue.split('.');
            if (parts.length > 2) {
                sanitizedValue = parts[0] + '.' + parts.slice(1).join('');
            }
            processedValue = sanitizedValue === "" ? "" : Number(sanitizedValue);
        } else if (name === "categoryId") {
            processedValue = value === "" ? "" : Number(value);
        } else {
            processedValue = value;
        }

        setFormData((prev: T) => ({
            ...prev,
            [name]: processedValue,
        }));
    };

    const handleSubmit = async (e: React.SubmitEvent<HTMLFormElement>) => {
        e.preventDefault();
        await onSubmit(formData);
    };

    return (
        <form onSubmit={handleSubmit} className={styles.form}>
            {categoriesError && <p>Error al cargar categorías: {categoriesError}</p>}

            <input
                className={styles.input}
                type="text"
                name="sku"
                placeholder="SKU"
                value={formData.sku}
                onChange={handleChange}
                required
            />

            <input
                className={styles.input}
                type="text"
                name="name"
                placeholder="Nombre"
                value={formData.name}
                onChange={handleChange}
                required
            />

            <input
                className={styles.input}
                type="number"
                name="price"
                placeholder="Precio"
                value={formData.price}
                onChange={handleChange}
                required
                min="0"
                step="0.01"
            />

            <select
                className={styles.select}
                name="categoryId"
                value={formData.categoryId}
                onChange={handleChange}
                required
            >
                <option value="">Selecciona una categoría</option>
                {isLoadingCategories ? (
                    <option disabled>Cargando categorías...</option>
                ) : (
                    categories
                        ?.filter((cat: CategoryResponse) => cat.active) // 👈 Filtra y deja solo las activas
                        .map((cat: CategoryResponse) => (
                            <option key={cat.id} value={cat.id}>
                                {cat.name}
                            </option>
                        ))
                )}
            </select>

            <textarea
                className={styles.textarea}
                name="description"
                placeholder="Descripción"
                value={formData.description}
                onChange={handleChange}
            />

            <button className={styles.button} type="submit" disabled={isSubmitting || isLoadingCategories}>
                {isSubmitting ? "Guardando..." : submitButtonText}
            </button>
        </form>
    );
}