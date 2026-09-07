import EffectsView from '@/views/EffectsView.vue'
import Home from '@/views/Home.vue'
import Login from '@/views/Login.vue'
import StegoLogs from '@/views/StegoLogs.vue'
import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'login',
    component: Login
  },
  {
    path: '/home',
    name: 'home',
    component: Home
  },
  {
    path: '/stego-logs',
    name: 'stego-logs',
    component: StegoLogs
  },
  {
    path: '/effects',
    name: 'effects',
    component: EffectsView
  },
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})

export default router
