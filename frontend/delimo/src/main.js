
import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'
import "./assets/tailwind.css"
import {useAuthStore} from "@/stores/auth.js";


const app = createApp(App)

app.use(createPinia())
app.use(router)

const authStore = useAuthStore();
authStore.fetchUser();

app.mount('#app')
