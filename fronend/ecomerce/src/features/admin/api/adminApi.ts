import {fetchClient} from "../../../services/ApiCliente";
import type {AdminDashboardResponse, AdminUserResponse} from "../types/admin";

export function getDashboard(): Promise<AdminDashboardResponse> {
    return fetchClient("/admin/dashboard");
}

export function getAllUsers(): Promise<AdminUserResponse[]> {
    return fetchClient("/admin/users");
}

export function getRoles(): Promise<string[]> {
    return fetchClient("/admin/roles");
}

export function updateUserRole(
    userId: number,
    role: string
): Promise<void> {
    return fetchClient(`/admin/users/${userId}/role`, {
        method: "PUT",
        body: JSON.stringify({role})
    });
}
export function toggleUserStatus(id: number): Promise<void> {
    return fetchClient(`/admin/users/${id}/toggle-status`, {
        method: "PUT"
    });
}