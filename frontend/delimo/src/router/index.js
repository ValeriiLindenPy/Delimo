// router/index.js
import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import ItemsView from '../views/ItemsView.vue'
import ItemDetails from '../views/ItemDetails.vue'
import UserDetails from '../views/UserDetails.vue'
import UserItems from '../views/UserItems.vue'
import LoginView from '../views/LoginView.vue'
import RegistrationView from '../views/RegistrationView.vue'
import AddItem from '../views/AddItem.vue'
import AddRequest from '../views/AddRequest.vue'
import EditUserDetails from '../views/EditUserDetails.vue'
import EditItem from '../views/EditItem.vue'
import RequestDetails from "@/views/RequestDetails.vue"
import UserRequests from "@/views/UserRequests.vue"
import EditRequest from "@/views/EditRequest.vue"
import FeedBack from "@/views/FeedBack.vue"
import RequestsView from "@/views/RequestsView.vue"
import PasswordForgot from "@/views/PasswordForgot.vue"
import PasswordReset from "@/views/PasswordReset.vue"
import SearchView from "@/views/SearchView.vue"
import PravilaView from "@/views/PravilaView.vue"
import Rules from "@/views/Rules.vue"
import { useAuthStore } from '@/stores/auth.js'
import NotFound from "@/views/NotFound.vue";
import ServerError from "@/views/ServerError.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    // Доступно всем:
    { path: '/', name: 'home', component: HomeView },
    { path: '/search', name: 'SearchView', component: SearchView },
    { path: '/pravila-uslovi', name: 'PravilaView', component: PravilaView },
    { path: '/rules', name: 'Rules', component: Rules },
    { path: '/login', name: 'LoginView', component: LoginView },
    { path: '/password-forgot', name: 'PasswordForgot', component: PasswordForgot },
    { path: '/registration', name: 'RegistrationView', component: RegistrationView },
    { path: '/requests', name: 'requests', component: RequestsView },
    { path: '/items', name: 'items', component: ItemsView },
    { path: '/feedback', name: 'FeedBack', component: FeedBack },
    { path: '/items/:id', name: 'ItemDetails', component: ItemDetails },
    { path: '/requests/:id', name: 'RequestDetails', component: RequestDetails },
    { path: '/not-found', name: 'NotFound', component: NotFound },
    { path: '/server-error', name: 'ServerError', component: ServerError },

    {
      path: '/reset-password',
      name: 'PasswordReset',
      component: PasswordReset,
      meta: { requiresAuth: true }
    },
    {
      path: '/items/adding-item',
      name: 'AddItem',
      component: AddItem,
      meta: { requiresAuth: true }
    },
    {
      path: '/items/adding-request',
      name: 'AddRequest',
      component: AddRequest,
      meta: { requiresAuth: true }
    },

    // Роуты, доступные только владельцу (авторизованный пользователь):
    {
      path: '/users/:id',
      name: 'UserDetails',
      component: UserDetails,
      meta: { requiresAuth: true, ownerOnly: true }
    },
    {
      path: '/users/:id/items',
      name: 'UserItems',
      component: UserItems,
      meta: { requiresAuth: true, ownerOnly: true }
    },
    {
      path: '/users/:id/requests',
      name: 'UserRequests',
      component: UserRequests,
      meta: { requiresAuth: true, ownerOnly: true }
    },
    {
      path: '/users/:id/edit',
      name: 'EditUserDetails',
      component: EditUserDetails,
      meta: { requiresAuth: true, ownerOnly: true }
    },
    {
      path: '/items/:id/edit',
      name: 'EditItem',
      component: EditItem,
      meta: { requiresAuth: true}
    },
    {
      path: '/requests/:id/edit',
      name: 'EditRequest',
      component: EditRequest,
      meta: { requiresAuth: true}
    },


    {
      path: '/:pathMatch(.*)*',
      name: 'CatchAll',
      redirect: { name: 'NotFound' }
    },
  ],
})

router.beforeEach(async (to, from, next) => {
  const authStore = useAuthStore()

  if (authStore.token && !authStore.profile) {
    try {
      await authStore.fetchUser()
    } catch (error) {
      return next({ name: 'LoginView' })
    }
  }

  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    return next({ name: 'LoginView', query: { redirect: to.fullPath } })
  }

  if (to.meta.ownerOnly) {
    if (!authStore.profile) {
      return next({ name: 'LoginView' })
    }
    if (to.params.id && String(to.params.id) !== String(authStore.profile.id)) {
      return next({ name: 'home' })
    }
  }

  next()
})

export default router
