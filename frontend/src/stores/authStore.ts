import { defineStore } from "pinia";
import type { UserInfo } from "@/types/auth";
import { ref, computed } from "vue";
import { isTokenExpired } from "@/utils/helpers";

export const useAuthStore = defineStore('auth', () => {
    const user = ref<UserInfo | null>(null);
    const token = ref<string | null>(null);

    const isAuthenticated = computed(() => !!token.value && !!user.value); //state of authentication
    const userRole = computed(() => user.value?.role || 'user');

    const setAuthData = (userData: UserInfo, authToken: string, rememberMe: boolean = false): void => {
        user.value = userData;
        token.value = authToken;

        const storage = rememberMe ? localStorage : sessionStorage;
        const otherStorage = rememberMe ? sessionStorage : localStorage;
        otherStorage.removeItem('authToken');
        otherStorage.removeItem('user');
        storage.setItem('authToken', authToken);
        storage.setItem('user', JSON.stringify(userData));
    }

    const clearAuthData = (): void => {
        user.value = null
        token.value = null

        localStorage.removeItem('authToken')
        localStorage.removeItem('user')
        sessionStorage.removeItem('authToken')
        sessionStorage.removeItem('user')
    }

    const initializeAuth = (): void => {
        const storages: Storage[] = [localStorage, sessionStorage];

        for (const storage of storages) {
            const storedToken = storage.getItem('authToken');
            const storedUser = storage.getItem('user');

            if (!storedToken || !storedUser) continue;

            if (isTokenExpired(storedToken)) {
                storage.removeItem('authToken');
                storage.removeItem('user');
                continue;
            }

            try {
                token.value = storedToken;
                user.value = JSON.parse(storedUser);
            } catch {
                storage.removeItem('authToken');
                storage.removeItem('user');
            }
        }
    }

    return {
        user,
        token,
        isAuthenticated,
        userRole,
        
        setAuthData,
        clearAuthData,
        initializeAuth
    }


})