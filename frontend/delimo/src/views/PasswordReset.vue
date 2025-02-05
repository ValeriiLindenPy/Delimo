<template>
  <PopUpModal :is-active="success" @close="toggleOk">
    <div class="flex flex-col items-center justify-center gap-2">
      <h1 class="bg-green-300 rounded-lg text-white font-bold p-3">
        Vasa lozinka je promenina!
      </h1>
      <button class="bg-st3 text-white font-medium py-2 px-4 rounded-md hover:bg-st4 transition"
              @click="toggleOk">
        Hvala!
      </button>
    </div>
  </PopUpModal>
  <div class="flex flex-col justify-center items-center bg-st2 h-screen">
    <form
        class="flex flex-col p-6 gap-2 bg-st2 rounded-lg w-full md:w-1/3"
        @submit.prevent="handlePasswordForgot"
    >
      <a href="/" class="no-underline hover:underline">< Početna</a>
      <div class="flex gap-2 justify-center items-center">
        <b class="text-3xl font-extrabold text-st5">Del</b>
        <i class="fa-solid fa-arrows-spin text-3xl text-st3"></i>
        <b class="text-3xl font-extrabold text-st5">imo</b>
      </div>

      <div v-if="loading">
        <Loader />
      </div>

      <div v-else class="flex flex-col">

        <!-- password -->
        <label class="pt-3 text-xs font-bold">NOVA LOZINKA (*):</label>
        <input
            class="border-b-2 border-st5 bg-st2 transition duration-500 text-st5 focus:outline-none focus:ring-2 focus:ring-st5"
            type="password"
            v-model="password"
            required
        />

        <!-- password2 -->
        <label class="pt-3 text-xs font-bold">PONOVITE LOZINKU (*):</label>
        <input
            class="border-b-2 border-st5 bg-st2 transition duration-500 text-st5 focus:outline-none focus:ring-2 focus:ring-st5"
            type="password"
            v-model="password2"
            required
        />

      </div>

      <div v-if="error" class="text-red-500 mt-2">{{ error }}</div>

      <div class="flex items-center justify-center pt-2">
        <button
            type="submit"
            class="bg-st3 shadow-lg p-3 rounded-lg transition-colors duration-500 hover:bg-st4 hover:text-white"
            :disabled="loading"
        >
          {{ loading ? "Učitavanje..." : "Nastavi" }}
        </button>
      </div>


    </form>
  </div>
</template>

<script>
import { useRouter, useRoute } from "vue-router";
import Loader from "@/components/UI/Loader.vue";
import {useAuthStore} from "@/stores/auth.js";
import PopUpModal from "@/components/UI/PopUpModal.vue";

export default {
  name: "PasswordReset",
  data() {
    const router = useRouter();
    const route = useRoute();
    const auth = useAuthStore();
    return {
      password: "",
      password2: "",
      success: false,
      error: null,
      loading: false,
      token: auth.token || route.query.resetToken || null,
      router,
      auth
    };
  },
  components: {
    PopUpModal,
    Loader,
  },

  methods: {
    toggleOk() {
      this.toggleSuccess();
      this.router.push('/login')
    },
    toggleSuccess() {
      this.success = !this.success;
    },

    async handlePasswordForgot() {
      const passwordRegex = /^(?=.*\d)(?=.*[!@#$%^&*])(?=.*[a-zA-Z])[a-zA-Z\d!@#$%^&*]{8,20}$/;

      // 1. Basic password format checks
      if (!passwordRegex.test(this.password)) {
        this.error = "Lozinka mora imati od 8 do 20 karaktera, jednu cifru i jedan specijalni simbol.";
        return;
      }

      // 2. Matching passwords
      if (this.password !== this.password2) {
        this.error = "Lozinke se ne poklapaju.";
        return;
      }

      if (!this.token) {
        this.error = "Greska se desava";
        return;
      }

      try {
        this.loading = true;
        const response = await this.auth.passwordReset(this.token, this.password);
        if (response.status === 200) {
          this.success = true;
        }

      } catch (error) {
        this.$router.push('/server-error');
        this.error = "Не удалось начать вход через Google.";
      }finally {
        this.loading = false;
      }
    },
  },
};
</script>


