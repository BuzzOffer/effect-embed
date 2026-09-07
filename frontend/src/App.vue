<template>
  <div id="app" class="bg-blue-100 min-h-screen lg:grid grid-cols-[auto_1fr] lg:h-screen">
    <div class="overflow-y-auto hidden lg:block">
      <Sidebar />
    </div>
    <div class="overflow-y-auto pt-16 sm:pt-12 lg:pt-0">
      <RouterView />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import Sidebar from './components/Sidebar.vue';

</script>

<style scoped></style>
