<template>
  <div class="container flex flex-col justify-center items-center my-auto">
    <form
        class="flex flex-col p-6 gap-2 bg-st2 rounded-lg w-auto shadow-2xl md:w-1/3"
        @submit.prevent="handleEmailPasswordLogin"
    >
      <div class="flex gap-2 justify-center items-center">
        <b class="text-3xl font-extrabold text-st5">Del</b>
        <i class="fa-solid fa-arrows-spin text-3xl text-st3"></i>
        <b class="text-3xl font-extrabold text-st5">imo</b>
      </div>

      <label class="pt-3 text-xs">EMAIL:</label>
      <input
          class="border-b-2 border-st5 bg-st2 transition duration-500
               text-st5 focus:outline-none focus:ring-2
               focus:ring-st5"
          type="email"
          v-model="email"
          required
      />

      <label class="pt-3 text-xs">LOZINKA:</label>
      <input
          class="border-b-2 border-st5 bg-st2 transition duration-500
               text-st5 focus:outline-none focus:ring-2
               focus:ring-st5"
          type="password"
          v-model="password"
          required
      />

      <div class="flex items-center justify-center pt-2">
        <button
            type="submit"
            class="bg-st3 shadow-lg p-3
                 rounded-lg transition-colors duration-500
                 hover:bg-st4 hover:text-white"
        >
          ULOGUJTE SE
        </button>
      </div>

      <div class="flex justify-center items-center mt-4">
        <hr class="flex-grow border-st5" />
        <p class="mx-4 text-st5 font-bold">ILI</p>
        <hr class="flex-grow border-st5" />
      </div>

      <div class="flex items-center justify-center pt-4">
        <button
            type="button"
            class="flex items-center gap-2 bg-white text-black
                 border border-st4 rounded-lg px-4 py-2
                 hover:bg-st4 hover:text-white transition-colors duration-500"
            @click="handleGoogleLogin"
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
import { useUserStore } from "@/stores/counter.js";
import { useRouter } from "vue-router";

export default {
  name: "LoginView",
  setup() {
    const userStore = useUserStore();
    const router = useRouter();

    const handleGoogleLogin = () => {
      // Redirect to the Spring OAuth2 login endpoint
      window.location.href = "http://localhost:8080/oauth2/authorization/google";
    };

    return {
      handleGoogleLogin,
    };
  },
};
</script>


