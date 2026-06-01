import {useEffect, useState} from "react";
import type {ProductResponse} from "../type/product.ts";
import { productGetAllUserApi} from "../api/productApi.ts";

export function useProductGetUserAll() {
    const [data, setData] = useState<ProductResponse[] | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        productGetAllUserApi()
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




    return {
        data,
        loading,
        error,

    };
}