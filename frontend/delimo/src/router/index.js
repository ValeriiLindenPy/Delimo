import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import ItemsView from '../views/ItemsView.vue'
import ItemDetails from '../views/ItemDetails.vue';
import UserDetails from '../views/UserDetails.vue';
import UserItems from '../views/UserItems.vue';
import LoginView from '../views/LoginView.vue';
import RegistrationView from '../views/RegistrationView.vue';
import AddItem from '../views/AddItem.vue';
import AddRequest from '../views/AddRequest.vue';
import EditUserDetails from '../views/EditUserDetails.vue';
import EditItem from '../views/EditItem.vue';
import RequestDetails from "@/views/RequestDetails.vue";
import UserRequests from "@/views/UserRequests.vue";
import EditRequest from "@/views/EditRequest.vue";
import FeedBack from "@/views/FeedBack.vue";
import RequestsView from "@/views/RequestsView.vue";
import PasswordForgot from "@/views/PasswordForgot.vue";
import PasswordReset from "@/views/PasswordReset.vue";

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
      path: '/password-forgot',
      name: 'PasswordForgot',
      component: PasswordForgot,
    },
    {
      path: '/reset-password',
      name: 'PasswordReset',
      component: PasswordReset,
    },
    {
      path: '/registration',
      name: 'RegistrationView',
      component: RegistrationView,
    },
    {
      path: '/requests',
      name: 'requests',
      component: RequestsView,
    },
    {
      path: '/items',
      name: 'items',
      component: ItemsView,
    },
    {
      path: '/feedback',
      name: 'FeedBack',
      component: FeedBack,
    },
    {
      path: '/items/adding-item',
      name: 'AddItem',
      component: AddItem,
    },
    {
      path: '/items/adding-request',
      name: 'AddRequest',
      component: AddRequest,
    },
    {
      path: '/items/:id',
      name: 'ItemDetails',
      component: ItemDetails
    },
    {
      path: '/requests/:id',
      name: 'RequestDetails',
      component: RequestDetails
    },
    {
      path: '/users/:id',
      name: 'UserDetails',
      component: UserDetails
    },
    {
      path: '/users/:id/items',
      name: 'UserItems',
      component: UserItems
    },
    {
      path: '/users/:id/requests',
      name: 'UserRequests',
      component: UserRequests
    },
    {
      path: '/users/:id/edit',
      name: 'EditUserDetails',
      component: EditUserDetails
    },
    {
      path: '/items/:id/edit',
      name: 'EditItem',
      component: EditItem
    },
    {
      path: '/requests/:id/edit',
      name: 'EditRequest',
      component: EditRequest
    },
  ],
})

export default router
