import { useState } from "react";
import { shipOrder } from "../api/orderApi.ts";
import { ApiError } from "../../../errors/ApiError.ts";

export function useOrderShip() {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    async function ship(id: number): Promise<boolean> {
        setLoading(true);
        setError("");
        try {
            await shipOrder(id);
            return true;
        } catch (err: unknown) {
            if (err instanceof ApiError) {
                setError(err.message);
            } else {
                setError("Ocurrió un error inesperado al enviar la orden.");
            }
            return false;
        } finally {
            setLoading(false);
        }
    }

    return {
        ship,
        loading,
        error
    };
}