import { useState } from "react";
import { productDelete } from "../api/productApi.ts";
import {ApiError} from "../../../errors/ApiError.ts";

export function useProductDelete() {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    const deleteProduct = async (id: number): Promise<boolean> => {
        setLoading(true);
        setError("");

        try {
            await productDelete(id);
            return true; 
        } catch (err) {
            if (err instanceof ApiError) {
                setError(err.message);
            } else {
                setError("No se pudo eliminar el producto de forma inesperada.");
            }
            return false;
        } finally {
            setLoading(false);
        }
    };

    return {
        deleteProduct,
        loading,
        error
    };
}