import { createContext, type ReactNode, useState } from "react";

const TOKEN_KEY = "auth_token";
const ROLES_KEY = "auth_roles";
const EMAIL_KEY = "auth_email";
const CUSTOMER_ID_KEY = "auth_cid";

interface AuthContextType {
    token: string | null;
    roles: string[];
    email: string | null;
    customerId: number | null;
    saveAuth: (token: string, roles: string[], email: string, customerId: number) => void;
    logout: () => void;
}

export const AuthContext = createContext<AuthContextType>({
    token: null,
    roles: [],
    email: null,
    customerId: null,
    saveAuth: () => {},
    logout: () => {},
});

interface Props {
    children: ReactNode;
}

export function AuthProvider({ children }: Props) {
    const [token, setToken] = useState<string | null>(() => localStorage.getItem(TOKEN_KEY));
    const [roles, setRoles] = useState<string[]>(() => {
        const stored = localStorage.getItem(ROLES_KEY);
        return stored ? JSON.parse(stored) : [];
    });
    const [email, setEmail] = useState<string | null>(() => localStorage.getItem(EMAIL_KEY));
    const [customerId, setCustomerId] = useState<number | null>(() => {
        const stored = localStorage.getItem(CUSTOMER_ID_KEY);
        return stored ? Number(stored) : null;
    });

    function saveAuth(token: string, roles: string[], email: string, customerId: number) {
        localStorage.setItem(TOKEN_KEY, token);
        localStorage.setItem(ROLES_KEY, JSON.stringify(roles));
        localStorage.setItem(EMAIL_KEY, email);
        localStorage.setItem(CUSTOMER_ID_KEY, customerId.toString());

        setToken(token);
        setRoles(roles);
        setEmail(email);
        setCustomerId(customerId);
    }

    function logout() {
        localStorage.removeItem(TOKEN_KEY);
        localStorage.removeItem(ROLES_KEY);
        localStorage.removeItem(EMAIL_KEY);
        localStorage.removeItem(CUSTOMER_ID_KEY);

        setToken(null);
        setRoles([]);
        setEmail(null);
        setCustomerId(null);
    }

    return (
        <AuthContext.Provider
            value={{ token, roles, email, customerId, saveAuth, logout }}
        >
            {children}
        </AuthContext.Provider>
    );
}