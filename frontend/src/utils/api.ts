import { useAuthStore } from "@/stores/authStore";

export const getAuthHeaders =(): HeadersInit => {
    const authStore = useAuthStore()
    const headers: HeadersInit = {
        'Content-Type': 'application/json'
    }

    if (authStore.token) {
        headers['Authorization'] = `Bearer ${authStore.token}`
    }

    return headers;
}

export const handleResponse = async(response: Response): Promise<any> => {
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