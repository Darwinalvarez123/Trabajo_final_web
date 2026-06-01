import { useEffect, useState } from "react";
import type { AdminUserResponse } from "../types/admin";
import {
    getAllUsers,
    getRoles,
    toggleUserStatus,
    updateUserRole
} from "../api/adminApi";

export function useAdminUsers() {
    const [data, setData] = useState<AdminUserResponse[] | null>(null);
    const [loading, setLoading] = useState(true);
    const [roles, setRoles] = useState<string[]>([]);
    const [error, setError] = useState("");
    const [selectedRoles, setSelectedRoles] = useState<Record<number, string>>({});

    useEffect(() => {
        Promise.all([getAllUsers(), getRoles()])
            .then(([users, roles]) => {
                setData(users);
                setRoles(roles);
            })
            .catch((err) => {
                setError(err.message);
            })
            .finally(() => {
                setLoading(false);
            });
    }, []);

    const handleUpdateRole = async (userId: number) => {
        const role = selectedRoles[userId];
        if (!role) return;

        await updateUserRole(userId, role);
        const users = await getAllUsers();
        setData(users);

    };

    const handleToggle = async (id: number) => {
        await toggleUserStatus(id);
        const users = await getAllUsers();
        setData(users);
    };

    return {
        data,
        loading,
        roles,
        error,
        selectedRoles,
        setSelectedRoles,
        handleUpdateRole,
        handleToggle
    };
}