<template>
  <div id="app" class="bg-background min-h-screen" :class="authStore.isAuthenticated ? 'lg:grid grid-cols-[auto_1fr] lg:h-screen' : 'flex lg:h-full'">
    <div v-if="authStore.isAuthenticated" class="overflow-y-auto hidden lg:block">
      <Sidebar />
    </div>
    <div :class="authStore.isAuthenticated ? 'overflow-y-auto pt-16 sm:pt-12 lg:pt-0' : 'flex-1 m-auto'">
      <RouterView />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import Sidebar from './components/Sidebar.vue';
import { useAuthStore } from './stores/authStore.ts';

const authStore = useAuthStore()

</script>

<style scoped></style>
