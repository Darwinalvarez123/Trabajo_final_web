import { useState } from "react";
import type { CreateOrderRequest, OrderResponse } from "../type/order.ts";
import { createOrder as apiCreateOrder } from "../api/orderApi.ts";
import { ApiError } from "../../../errors/ApiError.ts";

export function useOrderCreate() {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    async function createOrder(data: CreateOrderRequest): Promise<OrderResponse | null> {
        setLoading(true);
        setError("");
        try {
            return await apiCreateOrder(data);
        } catch (err: unknown) {
            if (err instanceof ApiError) {
                setError(err.message);
            } else {
                setError("Ocurrió un error inesperado al crear la orden.");
            }
            return null;
        } finally {
            setLoading(false);
        }
    }

    return {
        createOrder,
        loading,
        error
    };
}