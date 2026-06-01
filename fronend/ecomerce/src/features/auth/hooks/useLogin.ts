import { useState } from "react";
import { login } from "../api/authApi";
import { isValidEmail } from "../utils/validators";
import { useAuth } from "./useAuth";

export function useLogin() {
    const { saveAuth } = useAuth();

    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    async function submit(email: string, password: string) {
        setError("");

        if (!isValidEmail(email)) {
            setError("El correo no es válido");
            return null;
        }

        setLoading(true);

        try {
            const response = await login({ email, password });

           
            saveAuth(
                response.accessToken,
                response.roles,
                response.email,
                response.customerId
            );

            return response;
        } catch (err: unknown) {
            if (err instanceof Error) {
                setError(err.message);
            } else {
                setError("Error inesperado");
            }
            return null;
        } finally {
            setLoading(false);
        }
    }

    return {
        submit,
        loading,
        error
    };
}