import { useState } from "react";
import type { AddressResponse, CreateAddressRequest } from "../type/address.ts";
import { createAddress } from "../api/addressApi.ts";
import { ApiError } from "../../../errors/ApiError.ts";

export function useAddressCreate() {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    async function create(data: CreateAddressRequest): Promise<AddressResponse | null> {
        setLoading(true);
        setError("");
        try {
            return await createAddress(data);
        } catch (err) {
            if (err instanceof ApiError) {
                setError(err.message);
            } else {
                setError("Ocurrió un error inesperado al crear la dirección.");
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
