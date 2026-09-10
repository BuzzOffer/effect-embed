import type { StegoLogListResponse, StegoLogResponse } from "@/types/stego"
import { getAuthHeaders, handleResponse } from "@/utils/api"

export class StegoLogService {
    private baseStegoUrl: string

    constructor() {
        const apiBaseUrl = `${import.meta.env.VITE_BACKEND_API_URL}/api`

        if (!apiBaseUrl) {
            throw new Error('VITE_BACKEND_API_URL environment variable is not set')
        }

        this.baseStegoUrl = `${apiBaseUrl}/stego`
    }

    async getStegoLogList(): Promise<StegoLogListResponse> {
        const response = await fetch(`${this.baseStegoUrl}`, {
            headers: getAuthHeaders(),
        })
        return handleResponse(response)
    }

    async getStegoLogById(id: string): Promise<StegoLogResponse> {
        const response = await fetch(`${this.baseStegoUrl}/${id}`, {
            headers: getAuthHeaders(),
        })
        return handleResponse(response)
    }

    async deleteStegoLog(id: string): Promise<StegoLogResponse> {
        const response = await fetch(`${this.baseStegoUrl}/${id}`, {
            method: 'DELETE',
            headers: getAuthHeaders(),
        })
        return handleResponse(response)
    }
}