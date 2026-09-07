import type { StegoLogCreation, StegoLogListResponse, StegoLogResponse } from "@/types/stego"

export class StegoLogService {
    private baseStegoUrl: string

    constructor() {
        const apiBaseUrl = `${import.meta.env.VITE_BACKEND_API_URL}/api`

        if (!apiBaseUrl) {
            throw new Error('VITE_BACKEND_API_URL environment variable is not set')
        }

        this.baseStegoUrl = `${apiBaseUrl}/stego`
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

    async getStegoLogList(): Promise<StegoLogListResponse> {
        const response = await fetch(`${this.baseStegoUrl}`, {
            headers: {
                'Content-Type': 'application/json',
            },
        })
        return this.handleResponse(response)
    }

    async getStegoLogById(id: string): Promise<StegoLogResponse> {
        const response = await fetch(`${this.baseStegoUrl}/${id}`, {
            headers: {
                'Content-Type': 'application/json',
            },
        })
        return this.handleResponse(response)
    }

    async deleteStegoLog(id: string): Promise<StegoLogResponse> {
        const response = await fetch(`${this.baseStegoUrl}/${id}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            },
        })
        return this.handleResponse(response)
    }
}