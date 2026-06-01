import {useEffect, useState} from "react";
import type {ProductResponse} from "../type/product.ts";
import {productGetId} from "../api/productApi.ts";

export function useProductGetId(id: number) {
    const [data, setData] = useState<ProductResponse | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        productGetId(id)
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
    return {
        data,
        loading,
        error
    };


}