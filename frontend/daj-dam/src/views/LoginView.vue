<template>
  <div class="container flex flex-col justify-center items-center my-auto">
    <form class="flex flex-col p-6 gap-2 bg-st2 rounded-lg w-auto shadow-2xl md:w-1/3" action="">
      <div class="flex gap-2 justify-center items-center">
        <b class="text-3xl font-extrabold text-st5">Daj</b>
        <i class="fa-solid fa-arrows-spin text-3xl text-st3"></i>
        <b class="text-3xl font-extrabold text-st5">Dam</b>
      </div>
      <label class="pt-3 text-xs">EMAIL:</label>
      <input class="border-b-2 border-st5 bg-st2 transition duration-500
      text-st5 focus:outline-none focus:ring-2
      focus:ring-st5" type="email" name="email" id="email" v-model="email" required/>

      <label class="pt-3 text-xs">LOZINKA:</label>
      <input class="border-b-2 border-st5 bg-st2 transition duration-500
      text-st5 focus:outline-none focus:ring-2
      focus:ring-st5" type="password" name="password"
             id="password" v-model="password" required/>

      <div class="flex items-center justify-center pt-2">
        <button type="submit" class="bg-st3 shadow-lg p-3
        rounded-lg transition-colors duration-500
        hover:bg-st4 hover:text-white" >ULOGUJTE SE</button>
      </div>

      <div class="flex justify-center items-center mt-4">
        <hr class="flex-grow border-st5">
        <p class="mx-4 text-st5 font-bold">ILI</p>
        <hr class="flex-grow border-st5">
      </div>

      <!-- Google Login Button -->
      <div class="flex items-center justify-center pt-4">
        <button
            type="button"
            class="flex items-center gap-2 bg-white text-black
                 border border-st4 rounded-lg px-4 py-2
                 hover:bg-st4 hover:text-white transition-colors duration-500"
            @click="handleGoogleOneTap"
        >
          <img
              src="https://developers.google.com/identity/images/g-logo.png"
              alt="Google Icon"
              class="w-5 h-5"
          />
          Prijavite se putem Google-a
        </button>
      </div>
    </form>
  </div>
</template>

<script>
import { gapi } from 'gapi-script';
import {googleConst} from "@/assets/constants.js";
import {useUserStore} from "@/stores/counter.js";

export default {
  name: "LoginView",
  data() {
    return {
      email: "",
      password: "",
      useUserStore: useUserStore(),
    };
  },
  mounted() {
    this.initializeGapi();
  },
  methods: {
    initializeGapi() {
      gapi.load('client:auth2', () => {
        gapi.client.init({
          clientId: googleConst.clientId,
          scope: 'profile email'
        });
      });
    },
    async handleGoogleOneTap() {
      const authInstance = gapi.auth2.getAuthInstance();
      authInstance.signIn().then(googleUser => {
        const profile = googleUser.getBasicProfile();
        const idToken = googleUser.getAuthResponse().id_token;
        // Send idToken to your backend for verification and authentication
        if (idToken) {
          this.$router.push("/");
          useUserStore.authorized = true;
        }
      }).catch(error => {
        console.error('Google sign-in error:', error);
        //todo
        useUserStore.authorized = true;
        this.$router.push("/");
      });
    }
  }
};

</script>

