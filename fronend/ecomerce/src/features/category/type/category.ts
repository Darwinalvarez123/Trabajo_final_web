export interface CategoryResponse {
    id: number;
    name: string;
    description: string;
    active: boolean;
    createdAt: string;
    updatedAt: string;
}


export interface CreateCategoryRequest {
    name: string;
    description: string;
}


export interface UpdateCategoryRequest {
    name?: string;
    description?: string;
}