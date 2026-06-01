import { useEffect, useState } from "react";
import type { BestSellingProductResponse } from "../type/report.ts";
import { getBestSellingProducts } from "../api/reportApi.ts";
import { ApiError } from "../../../errors/ApiError.ts";

export function useBestSellingProducts() {
    const [data, setData] = useState<BestSellingProductResponse[] | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        getBestSellingProducts()
            .then((res) => {
                setData(res);
            })
            .catch((err) => {
                if (err instanceof ApiError) {
                    setError(err.message);
                } else {
                    setError("An unexpected error occurred while fetching best selling products.");
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