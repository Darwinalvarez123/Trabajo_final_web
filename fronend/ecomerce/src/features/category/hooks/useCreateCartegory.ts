import {useState} from "react";
import type {CategoryResponse, CreateCategoryRequest} from "../type/category.ts";
import {categoryCreate} from "../api/categoryApi.ts";
import {ApiError} from "../../../errors/ApiError.ts";

export function useCreateCategory()
{
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    async function createCategory(data: CreateCategoryRequest): Promise<CategoryResponse | null>
    {
        setLoading(true);
        setError("");

        try {
            return await categoryCreate(data);
        }catch (err: unknown) {
            if (err instanceof ApiError) {
                setError(err.message);
            } else {
                setError("Ocurrió un error inesperado al crear el producto.");
            }
            throw err


        } finally {
            setLoading(false);

        }

    }
    return{
        createCategory,
        loading,
        error
    }
}