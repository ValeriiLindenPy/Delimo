
import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'
import "./assets/tailwind.css"
import {useAuthStore} from "@/stores/auth.js";
import VueGtag from "vue-gtag";

const app = createApp(App)

app.use(createPinia())
app.use(router)

app.use(VueGtag, {
    config: { id: "G-BL4CV46VHH" },
    appName: "Delimo",
    pageTrackerScreenviewEnabled: true
}, router);

const authStore = useAuthStore();
authStore.fetchUser();

app.mount('#app')
