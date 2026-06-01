import { useEffect, useState } from "react";
import type { OrderStatusHistoryResponse } from "../type/order.ts";
import { getOrderHistory } from "../api/orderApi.ts";
import { ApiError } from "../../../errors/ApiError.ts";

export function useOrderHistory(orderId: number, refreshKey: number = 0) {
    const [data, setData] = useState<OrderStatusHistoryResponse[] | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        let cancelled = false;

        getOrderHistory(orderId)
            .then((res) => {
                if (!cancelled) setData(res);
            })
            .catch((err) => {
                if (!cancelled) {
                    if (err instanceof ApiError) {
                        setError(err.message);
                    } else {
                        setError("An unexpected error occurred while fetching order history.");
                    }
                }
            })
            .finally(() => {
                if (!cancelled) setLoading(false);
            });

        return () => { cancelled = true; };
    }, [orderId, refreshKey]);

    return { data, loading, error };
}