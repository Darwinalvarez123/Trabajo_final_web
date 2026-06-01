import {fetchClient} from "../../../services/ApiCliente.ts";
import type {CategoryResponse, CreateCategoryRequest, UpdateCategoryRequest} from "../type/category.ts";

export function categoryGetAll(): Promise<CategoryResponse[]> {
    return fetchClient("/categories");
}

export function categoryCreate(data: CreateCategoryRequest): Promise<CategoryResponse> {
    return fetchClient("/categories", {
        method: "POST",
        body: JSON.stringify(data)
    });
}

export function categoryUpdate(id: number, data: UpdateCategoryRequest): Promise<CategoryResponse> {
    return fetchClient(`/categories/${id}`, {
        method: "PUT",
        body: JSON.stringify(data)
    });
}

export function categoryGetId(id: number): Promise<CategoryResponse> {
    return fetchClient(`/categories/${id}`, {
        method: "GET",
    });
}

export function categoryDelete(id: number): Promise<void> { // Asumiendo que DELETE devuelve 204 No Content
    return fetchClient(`/categories/${id}`, {
        method: "DELETE",
    });
}

export function categoryReactivate(id: number): Promise<void> {
    return fetchClient(`/categories/${id}/reactivate`, {
        method: "PUT",
    });
}
