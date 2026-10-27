<template>
    <div class="h-full flex flex-col justify-between font-body overflow-hidden py-4 bg-primary w-40">
        <nav>
            <menu>
                <li v-for="(route, index) in routes" :key="index" :class="currentRoute === route.path ? 'bg-primary-light border-l-primary-var border-l-6' : ''" class="p-4 pr-10 hover:bg-primary-light">
                    <RouterLink :to="route.path">
                        <p class="font-bold">{{ route.name }}</p>
                    </RouterLink>
                </li>
            </menu>
        </nav>
        <button type="button" @click="showLogoutDialog = true" class="text-left p-4 pr-10 hover:bg-error-var/50 active:bg-error-var/80 w-full cursor-pointer">
            <p class="font-bold text-error">Logout</p>
        </button>
    </div>
    <DialogBox v-model="showLogoutDialog" variant="confirm" title="Logout" message="Are you sure you want to log out?" confirm-label="Yes" @confirm="logout" />
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router';
import DialogBox from './DialogBox.vue';
import { useAuthStore } from '@/stores/authStore.ts';

const route = useRoute();
const router = useRouter()
const authStore = useAuthStore()

const currentRoute = computed(() => route.path);
const showLogoutDialog = ref(false);

const routes = ref([
    {
        path: '/home',
        name: 'Home'
    },
    {
        path: '/stego-logs',
        name: 'Stego Logs'
    },
    {
        path: '/effects',
        name: 'Effects'
    },
    {
        path: '/tests',
        name: 'Testing'
    },
])

const logout = () => {
    authStore.clearAuthData()
    router.replace('/login')
}
</script>