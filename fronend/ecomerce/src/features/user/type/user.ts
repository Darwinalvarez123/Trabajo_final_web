import type { CategoryResponse } from "../../category/type/category.ts";

export interface UserProduct {
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

// Puedes añadir otros tipos relacionados con el usuario aquí si son necesarios más adelante
// export interface UserResponse { ... }
