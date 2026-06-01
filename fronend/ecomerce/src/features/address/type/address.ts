export interface AddressResponse {
    id: number;
    street: string;
    city: string;
    state: string;
    postalCode: string;
    country: string;
    customerId: number;
}

export interface CreateAddressRequest {
    street: string;
    city: string;
    state: string;
    postalCode: string;
    country: string;
    customerId: number;
}