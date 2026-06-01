import {useState} from "react";
import type {CustomerResponse, UpdateCustomerRequest} from "../types/customer.ts";
import {CustomerUpdate} from "../api/customerApi.ts";
import {ApiError} from "../../../errors/ApiError.ts";

export function UseUpdateCustomer()
{
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    async function updateCustomer(id:number,data:UpdateCustomerRequest):Promise<CustomerResponse | null>
    {
        setLoading(true);
        setError("");
        try {
            return await CustomerUpdate(id, data);
        } catch (err) {
            if (err instanceof ApiError) {
                setError(err.message);
            }
            return null;
        } finally {
            setLoading(false);
        }
    }

    return {
        updateCustomer,
        loading,
        error
    };
}