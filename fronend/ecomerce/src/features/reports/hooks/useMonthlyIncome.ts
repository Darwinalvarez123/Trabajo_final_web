import { useEffect, useState } from "react";
import type { MonthlyIncomeResponse } from "../type/report.ts";
import { getMonthlyIncome } from "../api/reportApi.ts";
import { ApiError } from "../../../errors/ApiError.ts";

export function useMonthlyIncome() {
    const [data, setData] = useState<MonthlyIncomeResponse[] | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        getMonthlyIncome()
            .then((res) => {
                setData(res);
            })
            .catch((err) => {
                if (err instanceof ApiError) {
                    setError(err.message);
                } else {
                    setError("An unexpected error occurred while fetching monthly income.");
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