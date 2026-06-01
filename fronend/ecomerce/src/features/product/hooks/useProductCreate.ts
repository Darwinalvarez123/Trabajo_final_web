import { useState } from "react";
import type { CreateProductRequest, ProductResponse } from "../type/product";
import { productCreate } from "../api/productApi";
import {ApiError} from "../../../errors/ApiError.ts";

export function useProductCreate() {

    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    async function createProduct(data: CreateProductRequest): Promise<ProductResponse | null> {
        setLoading(true);
        setError("");

        try {
            return await productCreate(data);
        }catch (err: unknown) {
            if (err instanceof ApiError) {
                setError(err.message);
            } else {
                setError("Ocurrió un error inesperado al crear el producto.");
            }
            return null;

        } finally {
            setLoading(false);
        }
    }

    return {
        createProduct,
        loading,
        error
    };
}