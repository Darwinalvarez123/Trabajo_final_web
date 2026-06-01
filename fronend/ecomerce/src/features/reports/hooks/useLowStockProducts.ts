import { useEffect, useState } from "react";
import type { LowStockProductResponse } from "../type/report.ts";
import { getLowStockProducts } from "../api/reportApi.ts";
import { ApiError } from "../../../errors/ApiError.ts";

export function useLowStockProducts() {
    const [data, setData] = useState<LowStockProductResponse[] | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        getLowStockProducts()
            .then((res) => {
                setData(res);
            })
            .catch((err) => {
                if (err instanceof ApiError) {
                    setError(err.message);
                } else {
                    setError("An unexpected error occurred while fetching low stock products.");
                }
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