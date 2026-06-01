export type CustomerStatus = "ACTIVE" | "INACTIVE";

export interface CustomerResponse {
    id: number;
    firstName: string;
    lastName: string;
    email: string;
    status: CustomerStatus;
}
export interface UpdateCustomerRequest {
    firstName: string;
    lastName: string;
    email: string;
    status: CustomerStatus;
}
export interface CreateCustomerRequest {
    firstName: string;
    lastName: string;
    email: string;
}