import { useState } from "react";
import { useAuth } from "./useAuth";
import { register } from "../api/authApi";
import { isValidEmail } from "../utils/validators";
import { useNavigate } from "react-router-dom";

export function useRegister() {
    const { saveAuth } = useAuth();
    const navigate = useNavigate();

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
            const response = await register({ email, password });
            
            saveAuth(
                response.accessToken,
                response.roles,
                response.email,
                response.customerId
            );

            navigate("/user");

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