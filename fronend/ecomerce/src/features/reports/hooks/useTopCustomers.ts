import { useEffect, useState } from "react";
import type { TopCustomerResponse } from "../type/report.ts";
import { getTopCustomers } from "../api/reportApi.ts";
import { ApiError } from "../../../errors/ApiError.ts";

export function useTopCustomers() {
    const [data, setData] = useState<TopCustomerResponse[] | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        getTopCustomers()
            .then((res) => {
                setData(res);
            })
            .catch((err) => {
                if (err instanceof ApiError) {
                    setError(err.message);
                } else {
                    setError("An unexpected error occurred while fetching top customers.");
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