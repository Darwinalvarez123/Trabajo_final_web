import {fetchClient} from "../../../services/ApiCliente.ts";
import type {InventoryResponse, UpdateInventoryRequest} from "../type/inventory.ts";

export function getInventoryByProductIdApi(productId: number): Promise<InventoryResponse> {
    return fetchClient(`/products/${productId}/inventory`);
}

export function updateInventoryByProductIdApi(productId: number, data: UpdateInventoryRequest): Promise<InventoryResponse> {
    return fetchClient(`/products/${productId}/inventory`,
        {
            method: "PUT",
            body: JSON.stringify(data)
        }
        );
}