import {useEffect, useState} from "react";
import type {AddressResponse} from "../type/address.ts";
import {addressGetByCustomerId} from "../api/addressApi.ts"; // Importamos la nueva función
import {ApiError} from "../../../errors/ApiError.ts";

export function useAddressGetByCustomerId(id: number) {
    const [data, setData] = useState<AddressResponse[] | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {



        addressGetByCustomerId(id)
            .then((res) => {
                setData(res);
            })
            .catch((err) => {
                if (err instanceof ApiError) {
                    setError(err.message);
                } else {
                    setError("An unexpected error occurred.");
                }
            })
            .finally(() => {
                setLoading(false);
            });
    }, [id]);

    return {
        data,
        loading,
        error,
    };
}