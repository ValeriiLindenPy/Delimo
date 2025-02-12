<template>
  <div class="flex flex-col justify-center items-center bg-st2 h-screen">
    <form
        class="flex flex-col p-6 gap-2 bg-st2 rounded-lg w-full md:w-1/3"
        @submit.prevent="handleEmailPasswordLogin"
    >
      <div v-if="verificationSuccess" class="flex gap-2 bg-green-500 rounded-lg">
        <p class="p-4 text-white font-bold">Vaša e-mail adresa je potvrđena, možete da se prijavite</p>
      </div>
      <a href="/" class="no-underline hover:underline">< Početna</a>
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
      <div>
      <p @click="goReset" class="pt-2 no-underline hover:underline cursor-pointer">
        Zaboravili ste lozinku?
      </p>
      </div>

      <div v-if="error" class="text-red-500 mt-2">{{ error }}</div>

      <div class="flex items-center justify-center pt-2">
        <button
            type="submit"
            class="bg-st3 shadow-lg p-3 rounded-lg transition-colors duration-500 hover:bg-st4 hover:text-white"
            :disabled="loading"
        >
          {{ loading ? "Učitavanje..." : "ULOGUJTE SE" }}
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
import { useAuthStore } from "@/stores/auth.js";
import { useRouter, useRoute } from "vue-router";


export default {
  name: "LoginView",
  data() {
    const authStore = useAuthStore();
    const router = useRouter();
    const route = useRoute(); // Доступ к параметрам маршрута
    return {
      email: "",
      password: "",
      error: null,
      loading: false,
      verificationToken: route.query.verificationToken || null,
      verificationSuccess: false,
      authStore,
      router,
    };
  },
  async created() {
    if (this.verificationToken) {
      try {
        this.loading = true;
        await this.authStore.verify(this.verificationToken).then(
            ()=> {
              this.verificationSuccess = true;
            }
        );

      } catch (error) {
        this.error = error.message;
      } finally {
        this.loading = false;
      }
    }
  },
  methods: {
    goReset() {
      this.router.push('/password-forgot')
    },
    async handleEmailPasswordLogin() {
      this.loading = true;
      try {
        await this.authStore.login({
          email: this.email,
          password: this.password,
        });
        this.error = null;
        if (this.authStore.isAuthenticated) {
          await this.router.push("/");
        }
      } catch (error) {
        this.error = error.response?.data?.message || "Neuspešna prijava.";
      } finally {
        this.loading = false;
      }
    },
    handleGoogleLogin() {
      try {
        console.log(import.meta.env.VITE_API_URL)
        window.location.href = `${import.meta.env.VITE_API_URL}/oauth2/authorization/google`;
      } catch (error) {
        console.log(error);
        this.error = "Nije uspelo pokretanje prijave preko Google-a.";
      }
    },
  },
};
</script>


