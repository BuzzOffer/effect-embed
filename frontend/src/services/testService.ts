import type { TestInfo, TestResults } from "@/types/test"
import { getAuthHeaders, handleResponse } from "@/utils/api"

export class TestService {
    private baseTestUrl: string

    constructor() {
        const apiBaseUrl = `${import.meta.env.VITE_TEST_PIPELINE_API_URL}/api/v1`

        if (!apiBaseUrl) {
            throw new Error('VITE_TEST_PIPELINE_API_URL environment variable is not set')
        }

        this.baseTestUrl = `${apiBaseUrl}/test`
    }

    async runTestService(imageData: FormData): Promise<TestInfo> {
        const response = await fetch(`${this.baseTestUrl}`,  {
            method: 'POST',
            body: imageData
        })
        return handleResponse(response)
    }

    async pollTestResultsService(id: string): Promise<TestResults> {
        const response = await fetch(`${this.baseTestUrl}/status/${id}`)
        return handleResponse(response)
    }
}