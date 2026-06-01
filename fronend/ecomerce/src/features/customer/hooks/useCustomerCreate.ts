import { useState } from "react";
import type { CustomerResponse, CreateCustomerRequest } from "../types/customer.ts";
import { CreateCustomer } from "../api/customerApi.ts";
import { ApiError } from "../../../errors/ApiError.ts";

export function useCustomerCreate() {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    async function create(data: CreateCustomerRequest): Promise<CustomerResponse | null> {
        setLoading(true);
        setError("");
        try {
            return await CreateCustomer(data);
        } catch (err) {
            if (err instanceof ApiError) {
                setError(err.message);
            } else {
                setError("Ocurrió un error inesperado al crear el cliente.");
            }
            return null;
        } finally {
            setLoading(false);
        }
    }

    return {
        create,
        loading,
        error,
    };
}
