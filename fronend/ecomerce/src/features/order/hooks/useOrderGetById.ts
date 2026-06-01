import { useCallback, useEffect, useRef, useState } from "react";
import type { OrderResponse } from "../type/order.ts";
import { getOrderById } from "../api/orderApi.ts";
import { ApiError } from "../../../errors/ApiError.ts";

export function useOrderGetById(id: number) {
    const [data, setData] = useState<OrderResponse | null>(null);
    const [error, setError] = useState("");
    const [refreshKey, setRefreshKey] = useState(0);
    const [fetchedKey, setFetchedKey] = useState(-1);
    const refreshKeyRef = useRef(refreshKey);


    const loading = fetchedKey !== refreshKey;

    useEffect(() => {
        let cancelled = false;

        getOrderById(id)
            .then((res) => {
                if (!cancelled) {
                    setData(res);
                    setError("");
                }
            })
            .catch((err) => {
                if (!cancelled) {
                    setData(null);
                    if (err instanceof ApiError) {
                        setError(err.message);
                    } else {
                        setError("An unexpected error occurred while fetching the order.");
                    }
                }
            })
            .finally(() => {
                if (!cancelled) {
                    setFetchedKey(refreshKeyRef.current);
                }
            });

        return () => { cancelled = true; };
    }, [id, refreshKey]);

    const updateOrderState = (updatedFields: Partial<OrderResponse>) => {
        setData((prev) => (prev ? { ...prev, ...updatedFields } : null));
    };

    const refresh = useCallback(() => {
        setRefreshKey((k) => k + 1);
    }, []);

    return { data, loading, error, updateOrderState, refresh };
}
