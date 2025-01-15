import {defineStore} from "pinia";


export const useUserStore = defineStore("user", {
  state: () => ({
    userId: null,
    profile: {
      id: null,
      name: "",
      email: "",
      phone: "",
      grad: "",
      street: "",
      viber: ""
    },
    authorized: false,
  }),
  actions: {
    setUserInfo(userInfo) {
      this.profile.id = userInfo.id;
      this.userId = this.profile.id;
      this.profile.phone = userInfo.telephone;
      this.profile.name = userInfo.name;
      this.profile.email = userInfo.email;
      this.profile.grad = userInfo.city;
      this.profile.street = userInfo.address;
      this.profile.viber = userInfo.viber;
      this.authorized = true;
    },
    logout() {
      this.userId = null;
      this.profile = null;
      this.authorized = false;
    },
  },
});
