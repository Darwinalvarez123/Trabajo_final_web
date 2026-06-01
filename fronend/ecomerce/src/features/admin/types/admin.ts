export interface AdminDashboardResponse {
    totalUsers: number;
    totalAdmins: number;
    totalNormalUsers: number;
    totalProducts: number;
    lowStockProducts:number;
}
export interface AdminUserResponse {
    id: number;
    email: string;
    roles: string[];
    enabled:boolean;
}
export type Role =
    | "ROLE_ADMIN"
    | "ROLE_SELLER"
    | "ROLE_USER";