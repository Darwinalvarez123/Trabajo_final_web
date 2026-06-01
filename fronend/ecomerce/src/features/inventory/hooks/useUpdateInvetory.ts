import {useState} from "react";
import {updateInventoryByProductIdApi} from "../api/inventoryApi.ts";
import type  {UpdateInventoryRequest} from "../type/inventory.ts";

export function useUpdateInventory()
{
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    async function updateInventory(id:number,data:UpdateInventoryRequest)
    {
        setLoading(true);
        setError("");

        try {
            return await updateInventoryByProductIdApi(id,data);
        }catch (err:unknown)
        {
            if (err instanceof Error) {
                setError(err.message);
            }else {
                setError("error inesperado")
            }
        }finally {
            setLoading(false);
        }

    }
    return {
        updateInventory,
        loading,
        error
    }
}