import type { EffectCreation, EffectListResponse, EffectResponse } from "@/types/effect"
import { getAuthHeaders, handleResponse } from "@/utils/api"

export class EffectService {
    private baseEffectUrl: string

    constructor() {
        const apiBaseUrl = `${import.meta.env.VITE_BACKEND_API_URL}/api`

        if (!apiBaseUrl) {
            throw new Error('VITE_BACKEND_API_URL environment variable is not set')
        }

        this.baseEffectUrl = `${apiBaseUrl}/effects`
    }

    async getEffectList(): Promise<EffectListResponse> {
        const response = await fetch(`${this.baseEffectUrl}`, {
            headers: getAuthHeaders(),
        })
        return handleResponse(response)
    }

    async createEffect(effect: EffectCreation): Promise<EffectResponse> {
        const response = await fetch(`${this.baseEffectUrl}/create`, {
            method: 'POST',
            headers: getAuthHeaders(),
            body: JSON.stringify(effect)
        })
        return handleResponse(response)
    }

    async updateEffect(id: string, effect: EffectCreation): Promise<EffectResponse> {
        const response = await fetch(`${this.baseEffectUrl}/${id}`, {
            method: 'PUT',
            headers: getAuthHeaders(),
            body: JSON.stringify(effect)
        })
        return handleResponse(response)
    }

    async deleteEffect(id: string): Promise<EffectResponse> {
        const response = await fetch(`${this.baseEffectUrl}/${id}`, {
            method: 'DELETE',
            headers: getAuthHeaders(),
        })
        return handleResponse(response)
    }
}