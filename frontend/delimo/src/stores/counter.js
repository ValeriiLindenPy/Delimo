import {defineStore} from "pinia";


export const useUserStore = defineStore("user", {
  state: () => ({
    userId: null,
    profile: {
      id: null,
      name: "",
      email: "",
      phone: "",
      city: "",
      street: "",
      viber: ""
    },
    loggingStatus: "not logging",
    authorized: false,
  }),
  actions: {
    setUserInfo(userInfo) {
      this.loggingStatus = "logged in";
      localStorage.removeItem("loggingStatus")
      this.profile.id = userInfo.id;
      this.userId = this.profile.id;
      this.profile.phone = userInfo.phone;
      this.profile.name = userInfo.name;
      this.profile.email = userInfo.email;
      this.profile.city = userInfo.city;
      this.profile.street = userInfo.street;
      this.profile.viber = userInfo.viber;
      this.authorized = true;
    },
    loggingIn() {
      localStorage.setItem("loggingStatus", "logging")
    },
    checkLogginStatus() {
      this.loggingStatus = localStorage.getItem("loggingStatus");
    },
    logout() {
      this.userId = null;
      this.profile = null;
      this.authorized = false;
    },
  },
});
