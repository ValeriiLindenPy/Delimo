import axios from 'axios';
import {useAuthStore} from "@/stores/auth.js";
import router from "@/router/index.js";


const apiClient = axios.create({
    baseURL: 'http://localhost:8080', // Backend base URL
    withCredentials: true,
});

apiClient.interceptors.request.use((config) => {
    const authStore = useAuthStore();
    if (authStore && authStore.token) {
        config.headers['Authorization'] = `Bearer ${authStore.token}`;
    }
    return config;
});

apiClient.interceptors.response.use(
    response => response,
    async (error) => {
        if (error.response && error.response.status === 401) {
            const authStore = useAuthStore();
            authStore.logout();
            await router.push('/login'); // Redirect to login page
        }
        return Promise.reject(error);
    }
);

export default apiClient;
