import {useEffect, useState} from "react";
import type {CustomerResponse} from "../types/customer.ts";
import {GetCustomer} from "../api/customerApi.ts";

export function useGetCustomer()
{
    const [data, setData] = useState<CustomerResponse[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        GetCustomer()
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

    return{
        data,
        loading,
        error
    }
}