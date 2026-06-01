import {useEffect, useState} from "react";
import type {InventoryResponse} from "../type/inventory.ts";
import {getInventoryByProductIdApi} from "../api/inventoryApi.ts";

export function UseGetInventory(id: number)
{
    const [data, setData] = useState<InventoryResponse | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const updateInventoryInState = (updatedFields: Partial<InventoryResponse>) => {
        setData((prevData) =>
            prevData ? { ...prevData, ...updatedFields } : null
        );
    };

    useEffect(() => {
        getInventoryByProductIdApi(id)
            .then((res) => {
                setData(res);
            })
            .catch((err) => {
                setError(err.message);
            })
            .finally(() => {
                setLoading(false);
            });
    }, [id]);

    return{
        data,
        loading,
        error,
        updateInventoryInState,
    }

}