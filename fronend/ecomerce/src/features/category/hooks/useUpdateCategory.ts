import {useState} from "react";
import type {CategoryResponse, UpdateCategoryRequest} from "../type/category.ts";
import {categoryUpdate} from "../api/categoryApi.ts";
import {ApiError} from "../../../errors/ApiError.ts";

export function useUpdateCategory() {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    async function updateCategory(id: number, data: UpdateCategoryRequest): Promise<CategoryResponse | null> {
        setLoading(true);
        setError("");
        try {
            return await categoryUpdate(id, data);
        } catch (err) {
            if (err instanceof ApiError) {
                setError(err.message);
            }
            return null;
        } finally {
            setLoading(false);
        }
    }

    return {
        updateCategory,
        loading,
        error
    }
}