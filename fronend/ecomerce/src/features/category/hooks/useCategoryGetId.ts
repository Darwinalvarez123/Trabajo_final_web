import {useEffect, useState} from "react";
import type {CategoryResponse} from "../type/category.ts";
import {categoryGetId} from "../api/categoryApi.ts";

export default function useCategoryGetId(id:number)
{
    const [data, setData] = useState<CategoryResponse | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        categoryGetId(id)
            .then((res) => {
                setData(res);
            })
            .catch((err) => {
                setError(err.message);
            })
            .finally(() => {
                setLoading(false);
            });
    }, [id]);

    return{
        data,
        loading,
        error
    }
}