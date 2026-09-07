<template>
    <div class="h-full font-body overflow-hidden p-4 bg-slate-200">
        <nav>
            <menu>
                <li v-for="(route, index) in routes" :key="index" class="p-2 hover:bg-slate-300">
                    <RouterLink :to="route.path">{{ route.name }}</RouterLink>
                </li>
            </menu>
        </nav>
    </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'


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