import {useEffect, useState} from "react";
import type {ProductResponse} from "../type/product.ts";
import {productGetAllApi} from "../api/productApi.ts";

export function useProductGetAll() {
    const [data, setData] = useState<ProductResponse[] | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        productGetAllApi()
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

    const removeProductFromState = (id: number) => {
        setData((prevData) =>
            (prevData ? prevData.filter(p =>
                p.id !== id) : null));
    };

    const updateProductInState = (id: number, updatedFields: Partial<ProductResponse>) => {
        setData((prevData) =>
            prevData
                ? prevData.map((p) => (p.id === id ? { ...p, ...updatedFields } : p))
                : null
        );
    };

    return {
        data,
        loading,
        error,
        removeProductFromState,
        updateProductInState,
    };
}