import { useEffect, useState } from "react";
import type { AdminDashboardResponse } from "../types/admin";
import { getDashboard } from "../api/adminApi";

export function useAdminDashboard() {
    const [data, setData] = useState<AdminDashboardResponse | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        getDashboard()
            .then((res) => {
                setData(res);
            })
            .catch((err) => {
                setError(err.message);
            })
            .finally(() => {
                setLoading(false);
            });
    }, []);

    return {
        data,
        loading,
        error
    };
}