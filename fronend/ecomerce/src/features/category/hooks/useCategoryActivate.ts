import { useState } from "react";
import { categoryReactivate } from "../api/categoryApi.ts"; // Importamos la función de reactivación
import { ApiError } from "../../../errors/ApiError.ts";

export function useCategoryActivate() {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    async function activateCategory(id: number): Promise<boolean> {
        setLoading(true);
        setError("");

        try {
            await categoryReactivate(id);
            return true;
        } catch (err: unknown) {
            if (err instanceof ApiError) {
                setError(err.message);
            } else {
                setError("Ocurrió un error inesperado al activar la categoría.");
            }
            return false;
        } finally {
            setLoading(false);
        }
    }

    return {
        activateCategory,
        loading,
        error
    };
}