export type OrderStatus = "CREATED" | "PAID" | "SHIPPED" | "DELIVERED" | "CANCELLED";

export interface OrderItemResponse {
    productId: number;
    quantity: number;
    unitPrice: number;
    subtotal: number;
}
export interface CreateOrderRequest {
    customerId: number;
    addressId: number;
    items: CreateOrderItemRequest[];
}


export interface OrderResponse {
    id: number;
    customerId: number;
    addressId: number;
    status: OrderStatus;
    total: number;
    createdAt: string;
    items: OrderItemResponse[];
}
export interface OrderStatusHistoryResponse {
    id: number;
    status: OrderStatus;
    comment: string;
    createdAt: string;
}
export interface CreateOrderItemRequest {
    productId: number;
    quantity: number;
}