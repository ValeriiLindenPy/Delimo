import {defineStore} from "pinia";
import apiClient from "@/services/api.js";
import {authenticateUser, registerUser, fetchUserData, forgotPasswordUser, passwordResetUser, verifyUser} from "@/services/userService.js"

export const useAuthStore = defineStore("auth", {
    state: () => ({
        token: localStorage.getItem("token") || null,
        profile: null,
    }),
    actions: {
        async register(userData) {
            return registerUser(userData);
        },

        async forgotPassword(email) {
           return forgotPasswordUser(email);
        },

        async passwordReset(token , password) {
            return passwordResetUser(token, password);
        },

        async verify(token) {
            return verifyUser(token);
        },

        async login(credentials) {
            const { data } = await authenticateUser(credentials);
            this.token = data.token;
            localStorage.setItem("token", data.token);
            apiClient.defaults.headers.common["Authorization"] = `Bearer ${data.token}`;
            await this.fetchUser()
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
                const { data } = await fetchUserData();
                this.profile = data;
            }
        },
    },
    getters: {
        isAuthenticated: (state) => !!state.token,
    },
});
