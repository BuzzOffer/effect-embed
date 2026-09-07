import type { EffectCreation, EffectListResponse, EffectResponse } from "@/types/effect"

export class EffectService {
    private baseEffectUrl: string

    constructor() {
        const apiBaseUrl = `${import.meta.env.VITE_BACKEND_API_URL}/api`

        if (!apiBaseUrl) {
            throw new Error('VITE_BACKEND_API_URL environment variable is not set')
        }

        this.baseEffectUrl = `${apiBaseUrl}/effects`
    }

    private async handleResponse(response: Response): Promise<any> {
        if (!response.ok) {
            if (response.status === 401) {
                //Handle unauthorized access
                throw new Error('Unauthorized access')
            }
            
            const errorData = await response.json().catch(() => ({}))
            throw new Error(errorData.message || `HTTP error! Status: ${response.status}`)
        }
        return response.json()
    }

    async getEffectList(): Promise<EffectListResponse> {
        const response = await fetch(`${this.baseEffectUrl}`, {
            headers: {
                'Content-Type': 'application/json',
            },
        })
        return this.handleResponse(response)
    }

    async createEffect(effect: EffectCreation): Promise<EffectResponse> {
        const response = await fetch(`${this.baseEffectUrl}/create`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(effect)
        })
        return this.handleResponse(response)
    }

    async updateEffect(id: string, effect: EffectCreation): Promise<EffectResponse> {
        const response = await fetch(`${this.baseEffectUrl}/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(effect)
        })
        return this.handleResponse(response)
    }

    async deleteEffect(id: string): Promise<EffectResponse> {
        const response = await fetch(`${this.baseEffectUrl}/${id}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            },
        })
        return this.handleResponse(response)
    }
}