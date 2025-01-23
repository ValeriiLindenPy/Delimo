import axios from 'axios';
import {useAuthStore} from "@/stores/auth.js";


const apiClient = axios.create({
    baseURL: 'http://localhost:8080', // Backend base URL
    withCredentials: true,
});

apiClient.interceptors.request.use((config) => {
    const authStore = useAuthStore();
    if (authStore.token) {
        config.headers['Authorization'] = `Bearer ${authStore.token}`;
    }
    return config;
});

apiClient.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response && error.response.status === 401) {
            const authStore = useAuthStore();
            authStore.logout();
        }
        return Promise.reject(error);
    }
);

export default apiClient;
