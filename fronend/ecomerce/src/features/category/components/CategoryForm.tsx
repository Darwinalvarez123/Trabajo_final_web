import * as React from "react";
import { useState } from "react";
import type { CreateCategoryRequest, UpdateCategoryRequest } from "../type/category.ts";
import styles from "../styles/CategoryForm.module.css"; // Importar los estilos

interface CategoryFormProps<T> {
    initialData: T;
    onSubmit: (data: T) => Promise<void>;
    isSubmitting: boolean;
    submitButtonText: string;
}

export function CategoryForm<T extends CreateCategoryRequest | UpdateCategoryRequest>({
                                                                                          initialData,
                                                                                          onSubmit,
                                                                                          isSubmitting,
                                                                                          submitButtonText
                                                                                      }: CategoryFormProps<T>) {

    const [formData, setFormData] = useState<T>(initialData);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value } = e.target;
        setFormData((prev: T) => ({
            ...prev,
            [name]: value,
        }));
    };

    const handleSubmit = async (e: React.SubmitEvent<HTMLFormElement>) => {
        e.preventDefault();
        await onSubmit(formData);
    };

    return (
        <form onSubmit={handleSubmit} className={styles.form}>

            <input
                className={styles.input}
                type="text"
                name="name"
                placeholder="Nombre de la categoría"
                value={formData.name || ""}
                onChange={handleChange}
                required
            />

            <textarea
                className={styles.textarea}
                name="description"
                placeholder="Descripción (Opcional)"
                value={formData.description || ""}
                onChange={handleChange}
            />

            <button className={styles.button} type="submit" disabled={isSubmitting}>
                {isSubmitting ? "Guardando..." : submitButtonText}
            </button>
        </form>
    );
}