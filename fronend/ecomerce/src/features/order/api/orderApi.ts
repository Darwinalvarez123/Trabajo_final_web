import { fetchClient } from "../../../services/ApiCliente.ts";
import type {
    OrderResponse,
    CreateOrderRequest,
    OrderStatusHistoryResponse,
} from "../type/order.ts";


export function createOrder(data: CreateOrderRequest): Promise<OrderResponse> {
    return fetchClient("/orders", {
        method: "POST",
        body: JSON.stringify(data),
    });
}

export function getOrderById(id: number): Promise<OrderResponse> {
    return fetchClient(`/orders/${id}`);
}

export function getAllOrders(): Promise<OrderResponse[]> {
    return fetchClient("/orders");
}

export function payOrder(id: number): Promise<void> {
    return fetchClient(`/orders/${id}/pay`, {
        method: "PUT",
    });
}

export function shipOrder(id: number): Promise<void> {
    return fetchClient(`/orders/${id}/ship`, {
        method: "PUT",
    });
}

export function deliverOrder(id: number): Promise<void> {
    return fetchClient(`/orders/${id}/deliver`, {
        method: "PUT",
    });
}

export function cancelOrder(id: number): Promise<void> {
    return fetchClient(`/orders/${id}/cancel`, {
        method: "PUT",
    });
}

export function getOrderHistory(id: number): Promise<OrderStatusHistoryResponse[]> {
    return fetchClient(`/orders/${id}/history`);
}
