import { useEffect, useState } from "react";
import type { OrderResponse } from "../type/order.ts";
import { getAllOrders } from "../api/orderApi.ts";
import { ApiError } from "../../../errors/ApiError.ts";

export function useOrderGetAll() {
    const [data, setData] = useState<OrderResponse[] | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        getAllOrders()
            .then((res) => {
                setData(res);
            })
            .catch((err) => {
                if (err instanceof ApiError) {
                    setError(err.message);
                } else {
                    setError("An unexpected error occurred while fetching orders.");
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