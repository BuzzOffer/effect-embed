import { AuthService } from "@/services/authService";
import { useAuthStore } from "@/stores/authStore";
import type { LoginCredentials } from "@/types/auth";
import { ref } from "vue";
import { useRouter } from "vue-router";

export function useAuth() {
    const loading = ref<boolean>(false);
    const error = ref<string>('');

    const authService = new AuthService()
    const authStore = useAuthStore()
    const router = useRouter()

    const handleError = (err: any) => {
        error.value = err instanceof Error ? err.message: 'An unexpected error occured';
        console.error('Auth API error: ', err)
    }

    const login = async (credentials: LoginCredentials, rememberMe: boolean = false): Promise<void> => {
        try {
            loading.value = true;
            const response = await authService.loginUser(credentials);

            if (!response.success && response.message) {
                error.value = response.message
                return
            }

            if (response.data.user && response.data.token) {
                authStore.setAuthData(response.data.user, response.data.token, rememberMe);
                router.push('/home')
            }
        } catch (error) {
            handleError(error)
        } finally {
            loading.value = false;
        }
    }

    return {
        loading,
        error,

        login,
    }
}