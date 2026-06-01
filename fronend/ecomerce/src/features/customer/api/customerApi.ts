import type {CreateCustomerRequest, CustomerResponse, UpdateCustomerRequest} from "../types/customer.ts";
import {fetchClient} from "../../../services/ApiCliente.ts";


export function CustomerUpdate(id:number, data:UpdateCustomerRequest): Promise<CustomerResponse>
{
    return fetchClient(`/customers/${id}`,
        {
            method: "PUT",
            body: JSON.stringify(data)
        }
    );
}
export function CreateCustomer(data: CreateCustomerRequest): Promise<CustomerResponse> {
    return fetchClient("/customers", {
        method: "POST",
        body: JSON.stringify(data),
    });
}
export function GetCustomer(): Promise<CustomerResponse[]> {
    return fetchClient(`/customers`, {
        method: "GET"
    });
}