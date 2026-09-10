import type { LoginCredentials, LoginResponse } from "@/types/auth"
import { handleResponse } from "@/utils/api"

export class AuthService {
    private baseAuthUrl: string

    constructor() {
        const apiBaseUrl = `${import.meta.env.VITE_BACKEND_API_URL}/api`

        if (!apiBaseUrl) {
            throw new Error('VITE_BACKEND_API_URL environment variable is not set')
        }

        this.baseAuthUrl = `${apiBaseUrl}/auth`
    }

    async loginUser(credentials: LoginCredentials): Promise<LoginResponse> {
        const response = await fetch(`${this.baseAuthUrl}/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(credentials)
        })
        return handleResponse(response);
    }
}