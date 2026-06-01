import type {AuthResponse, LoginRequest, RegisterRequest} from "../types/auth.ts";
import {fetchClient} from "../../../services/ApiCliente.ts";

export async function login(data:LoginRequest):Promise<AuthResponse>
{
    return fetchClient("/auth/login",
        {
            method:'POST',
            body: JSON.stringify(data),
        }
    )
}

export async function register(data:RegisterRequest):Promise<AuthResponse>
{
    return fetchClient("/auth/register",
        {
            method:"POST",
            body: JSON.stringify(data),

        })
}