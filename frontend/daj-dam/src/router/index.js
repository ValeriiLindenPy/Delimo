import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import ItemsView from '../views/ItemsView.vue'
import ItemDetails from '../views/ItemDetails.vue';
import UserDetails from '../views/UserDetails.vue';
import UserOglasi from '../views/UserOglasi.vue';
import LoginView from '../views/LoginView.vue';
import RegistrationView from '../views/RegistrationView.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/login',
      name: 'LoginView',
      component: LoginView,
    },
    {
      path: '/registration',
      name: 'RegistrationView',
      component: RegistrationView,
    },
    {
      path: '/items',
      name: 'items',
      component: ItemsView,
    },
    {
      path: '/items/:id',
      name: 'ItemDetails',
      component: ItemDetails
    },
    {
      path: '/users/:id',
      name: 'UserDetails',
      component: UserDetails
    },
    {
      path: '/users/:id/oglasi',
      name: 'UserOglasi',
      component: UserOglasi
    },
  ],
})

export default router
