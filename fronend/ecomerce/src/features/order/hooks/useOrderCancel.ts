import { useState } from "react";
import { cancelOrder } from "../api/orderApi.ts";
import { ApiError } from "../../../errors/ApiError.ts";

export function useOrderCancel() {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    async function cancel(id: number): Promise<boolean> {
        setLoading(true);
        setError("");
        try {
            await cancelOrder(id);
            return true;
        } catch (err: unknown) {
            if (err instanceof ApiError) {
                setError(err.message);
            } else {
                setError("Ocurrió un error inesperado al cancelar la orden.");
            }
            return false;
        } finally {
            setLoading(false);
        }
    }

    return {
        cancel,
        loading,
        error
    };
}