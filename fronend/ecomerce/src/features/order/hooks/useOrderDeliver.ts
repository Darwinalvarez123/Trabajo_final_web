import { useState } from "react";
import { deliverOrder } from "../api/orderApi.ts";
import { ApiError } from "../../../errors/ApiError.ts";

export function useOrderDeliver() {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    async function deliver(id: number): Promise<boolean> {
        setLoading(true);
        setError("");
        try {
            await deliverOrder(id);
            return true;
        } catch (err: unknown) {
            if (err instanceof ApiError) {
                setError(err.message);
            } else {
                setError("Ocurrió un error inesperado al entregar la orden.");
            }
            return false;
        } finally {
            setLoading(false);
        }
    }

    return {
        deliver,
        loading,
        error
    };
}