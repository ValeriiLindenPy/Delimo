import {defineStore} from "pinia";
import apiClient from "@/services/api.js";


export const useUserStore = defineStore("user", {
  state: () => ({
    userId: null,
    profile: {
      id: null,
      name: "",
      email: "",
    },
    authorized: false,
  }),
  actions: {
    setUserInfo(userInfo) {
      this.userId = 1; //todo
      this.profile.name = userInfo.name;
      this.profile.email = userInfo.email;
      this.authorized = true;
    },
    logout() {
      this.userId = null;
      this.profile = null;
      this.authorized = false;
    },
  },
});
