// store/auth.js
import { defineStore } from 'pinia';
import apiClient from "@/services/api.js";


export const useAuthStore = defineStore('auth', {
    state: () => ({
        token: localStorage.getItem('token') || null,
        profile: null,
    }),
    actions: {
        async register(formData) {
            try {
                const res = await apiClient.post('/auth/register', formData, {
                    withCredentials: true,
                    headers: {"Content-Type": "application/json"},
                });
                return res.data;
            } catch (error) {
                console.error('Error with registration:', error);
                throw error;
            }
        },
        async login(credentials) {
            try {
                const { data } = await apiClient.post('/auth/login', credentials);
                this.token = data.token;
                localStorage.setItem('token', data.token); // сохраняем токен
                apiClient.defaults.headers.common['Authorization'] = `Bearer ${data.token}`; // добавляем токен в заголовки
            } catch (error) {
                console.error('Ошибка при авторизации:', error);
                throw error;
            }
        },
        logout() {
            this.token = null;
            this.user = null;
            localStorage.removeItem('token'); // очищаем токен
            delete apiClient.defaults.headers.common['Authorization'];
        },
        async fetchUser() {
            if (this.token) {
                try {
                    const { data } = await apiClient.get('/users/user-data');
                    this.profile = data;
                } catch (error) {
                    console.error('Ошибка при получении данных пользователя:', error);
                }
            }
        },
    },
    getters: {
        isAuthenticated: (state) => !!state.token,
    },
});
