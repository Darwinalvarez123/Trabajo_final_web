import {fetchClient} from "../../../services/ApiCliente.ts";
import type {AddressResponse, CreateAddressRequest} from "../type/address.ts";

export function addressGetById(id: number): Promise<AddressResponse> {
    return fetchClient(`/addresses/${id}`);
}
export function addressGetByCustomerId(customerId: number): Promise<AddressResponse[]> {
    return fetchClient(`/addresses/customer/${customerId}`);
}
export function addressGetAll(): Promise<AddressResponse[]> {
    return fetchClient("/addresses");
}
export function createAddress(data: CreateAddressRequest): Promise<AddressResponse> {
    return fetchClient("/addresses", {
        method: "POST",
        body: JSON.stringify(data),
    });
}