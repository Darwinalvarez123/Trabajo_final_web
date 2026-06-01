import { useState } from "react";
import { payOrder } from "../api/orderApi.ts";
import { ApiError } from "../../../errors/ApiError.ts";

export function useOrderPay() {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    async function pay(id: number): Promise<boolean> {
        setLoading(true);
        setError("");
        try {
            await payOrder(id);
            return true;
        } catch (err: unknown) {
            if (err instanceof ApiError) {
                setError(err.message);
            } else {
                setError("Ocurrió un error inesperado al pagar la orden.");
            }
            return false;
        } finally {
            setLoading(false);
        }

    }


    return {
        pay,
        loading,
        error
    };
}