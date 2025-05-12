import axios from 'axios';
import { useAuthStore } from "@/stores/auth.js";
import router from "@/router/index.js";

const apiClient = axios.create({
    baseURL: import.meta.env.DELIMO_API_URL,
    withCredentials: true,
});

apiClient.interceptors.request.use((config) => {
    const authStore = useAuthStore();
    if (authStore?.token) {
        config.headers['Authorization'] = `Bearer ${authStore.token}`;
    }
    return config;
});

apiClient.interceptors.response.use(
    response => response,
    async (error) => {
        const status = error.response?.status;

        switch (status) {
            case 400:
                console.error("Bad Request (400):", error.response.data);
                break;

            case 401:
                console.error("Unauthorized (401):", error.response.data);
                useAuthStore().logout();
                await router.push('/login');
                break;

            case 404:
                console.error("Not Found (404):", error.response.data);
                await router.push('/not-found');
                break;

            case 500:
                console.error("Server Error (500):", error.response.data);
                await router.push('/server-error');
                break;

            default:
                console.error(`Error (${status}):`, error.response?.data || error);
                break;
        }

        return Promise.reject(error);
    }
);

export default apiClient;
