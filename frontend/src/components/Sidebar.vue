<template>
    <div class="h-full font-body overflow-hidden py-4 bg-primary w-40">
        <nav>
            <menu>
                <li v-for="(route, index) in routes" :key="index" :class="currentRoute === route.path ? 'bg-primary-light border-l-primary-var border-l-6' : ''" class="p-4 pr-10 hover:bg-primary-light">
                    <RouterLink :to="route.path">
                        <p class="font-bold">{{ route.name }}</p>
                    </RouterLink>
                </li>
            </menu>
        </nav>
    </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRoute } from 'vue-router';

const route = useRoute();

const currentRoute = computed(() => route.path);

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
])
</script>