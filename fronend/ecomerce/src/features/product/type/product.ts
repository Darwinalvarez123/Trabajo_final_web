import type  {CategoryResponse} from "../../category/type/category.ts";


export interface ProductResponse {
    id: number;
    sku: string;
    name: string;
    description: string;
    price: string;
    active: boolean;
    createdAt: string;
    updatedAt: string;
    category: CategoryResponse;
}
export interface CreateProductRequest {
    sku: string;
    name: string;
    description?: string;
    price: number;

    categoryId: number;
}
export interface UpdateProductRequest {
    sku?: string;
    name?: string;
    description?: string;
    price?: number;
    active?: boolean;
    categoryId?: number;
}
