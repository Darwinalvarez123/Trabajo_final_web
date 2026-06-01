export interface InventoryResponse {
    id: number;
    productId: number;
    availableStock: number;
    minStock: number;
}

export interface UpdateInventoryRequest {
    availableStock: number;
    minStock: number;
}