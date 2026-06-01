import {useState} from "react";
import type {ProductResponse, UpdateProductRequest} from "../type/product.ts";
import {productUpdate} from "../api/productApi.ts";
import {ApiError} from "../../../errors/ApiError.ts";

export function useProductUpdate() {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    async function updateProduct(id:number,data: UpdateProductRequest):Promise<ProductResponse | null> {
        setLoading(true);
        setError("");
        try {
            return await productUpdate(id, data);
        } catch (err) {
            if (err instanceof ApiError) {

                setError(err.message);
            }
            return null;
        } finally
        {
            setLoading(false);
        }

    }
    return {
        updateProduct,
        loading,
        error
    };

}