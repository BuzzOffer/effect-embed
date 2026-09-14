import { useAuthStore } from '@/stores/authStore'
import EffectsView from '@/views/EffectsView.vue'
import Home from '@/views/Home.vue'
import Login from '@/views/Login.vue'
import StegoLogs from '@/views/StegoLogs.vue'
import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'login',
    component: Login,
    meta: {
      requiresAuth: false
    }
  },
  {
    path: '/home',
    name: 'home',
    component: Home,
    meta: {
      requiresAuth: true
    }
  },
  {
    path: '/stego-logs',
    name: 'stego-logs',
    component: StegoLogs,
    meta: {
      requiresAuth: true
    }
  },
  {
    path: '/effects',
    name: 'effects',
    component: EffectsView,
    meta: {
      requiresAuth: true
    }
  },
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})

router.beforeEach((to, from) => {
  const authStore = useAuthStore()

  authStore.initializeAuth()

  //check if page requires authentication
  if (to.meta.requiresAuth) {
    //if user is not authenticated, redirect to login page with full path of the page user tried to access
    if (!authStore.isAuthenticated) {
      return { name: 'login', query: { redirect: to.fullPath } }
    }
  } else { //if user attempts to go to login page while logged in, redirects to home page
    if (to.name === 'login' && authStore.isAuthenticated) {
      return { name: 'home' }
    }
  }
})

export default router
