import { useEffect, useState } from "react";
import type { CategoryResponse } from "../type/category.ts";
import { categoryGetAll } from "../api/categoryApi.ts";

export function useGetCategory() {
    const [data, setData] = useState<CategoryResponse[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        categoryGetAll()
            .then((res) => {
                setData(res);
            })
            .catch((err) => {
                setError(err.message);
            })
            .finally(() => {
                setLoading(false);
            });
    }, []);


    const updateCategoryInState = (id: number, updatedFields: Partial<CategoryResponse>) => {
        setData((prevData) =>
            prevData.map((category) =>
                category.id === id ? { ...category, ...updatedFields } : category
            )
        );
    };

    return {
        data,
        loading,
        error,
        updateCategoryInState
    };
}