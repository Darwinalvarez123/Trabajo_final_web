import { ApiError } from "../errors/ApiError.ts";

const base_url = import.meta.env.VITE_API_URL;

interface FetchOptions extends RequestInit {
    token?: string;
}

export async function fetchClient(endpoint: string, options: FetchOptions = {}) {
    const { token, headers, ...rest } = options;

    const storedToken = token || localStorage.getItem("auth_token");

    const response = await fetch(`${base_url}${endpoint}`, {
        ...rest,
        headers: {
            "Content-Type": "application/json",
            ...(storedToken && {
                Authorization: `Bearer ${storedToken}`,
            }),
            ...headers,
        },
    });

    if (response.status === 204) {
        console.log("SUCCESS RESPONSE: NO CONTENT");
        return null;
    }

    if (response.status === 400) {
        const data = await response.json();
        const errorMessage = data.violations?.[0]?.message
            || data.message
            || "Error de validación en los datos.";

        throw new ApiError(
            data.status || response.status,
            errorMessage
        );
    }

    if (!response.ok) {
        const data = await response.json().catch(() => ({}));
        throw new ApiError(
            data.status || response.status,
            data.message || `Error en el servidor (${response.status})`
        );
    }

    const data = await response.json();
    console.log("SUCCESS RESPONSE:", data);
    return data;
}