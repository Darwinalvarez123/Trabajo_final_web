import {Navigate} from "react-router-dom";
import {useAuth} from "../../features/auth/hooks/useAuth";
import * as React from "react";

interface Props {
    children: React.ReactNode;
    requiredRole?: string;
}

export default function PrivateRoute({
                                         children,
                                         requiredRole
                                     }: Props) {
    const {token, roles} = useAuth();

    if (!token) {
        return <Navigate to="/login"/>;
    }

    if (requiredRole && !roles.includes(requiredRole)) {
        return <Navigate to="/user"/>;
    }

    return children;
}