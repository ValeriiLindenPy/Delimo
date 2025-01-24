import {defineStore} from "pinia";
import apiClient from "@/services/api.js";

export const useAuthStore = defineStore("auth", {
    state: () => ({
        token: localStorage.getItem("token") || null,
        profile: null,
    }),
    actions: {
        async register(userData) {
            try {
                return await apiClient.post("/auth/register", userData, {
                    withCredentials: true,
                    headers: {
                        "Content-Type": "application/json",
                    },
                });
            } catch (error) {
                console.error("Error with registration:", error);
                throw error;
            }
        },

        async forgotPassword(email) {
            try {
                return await apiClient.post("/auth/forgot-password", {
                    email: email,
                });
            }catch (error) {
                throw error;
            }
        },

        async verify(token) {
            try {
                return await apiClient.get("/auth/verify", {params: {token}});
            }catch (error) {
                throw error;
            }
        },

        async login(credentials) {
            try {
                const { data } = await apiClient.post("/auth/authenticate", credentials);
                this.token = data.token;
                localStorage.setItem("token", data.token);
                apiClient.defaults.headers.common["Authorization"] = `Bearer ${data.token}`;
                await this.fetchUser()
            } catch (error) {
                console.error("Ошибка при авторизации:", error);
                throw error;
            }
        },


        async setToken(token) {
            this.token = token;
            localStorage.setItem("token", token);
            apiClient.defaults.headers.common["Authorization"] = `Bearer ${token}`;
            await this.fetchUser();
        },

        logout() {
            this.token = null;
            this.user = null;
            localStorage.removeItem("token");
            delete apiClient.defaults.headers.common["Authorization"];
        },

        async fetchUser() {
            if (this.token) {
                try {
                    const { data } = await apiClient.get("/users/user-data");
                    this.profile = data;
                } catch (error) {
                    console.error("Ошибка при получении данных пользователя:", error);
                }
            }
        },
    },
    getters: {
        isAuthenticated: (state) => !!state.token,
    },
});
