import {defineStore} from "pinia";


export const useUserStore = defineStore("user", {
  state: () => ({
    userId: null,
    profile: {
      id: null,
      name: "",
      email: "",
      phone: "+381 621110623",
      grad: "Novi Sad",
      street: "Omladinskog pokreta 2",
      viber: ""
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
