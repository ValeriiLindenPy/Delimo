<template>
  <PopUpModal :is-active="success" @close="toggleOk">
    <div class="flex flex-col items-center justify-center gap-2">
      <h1 class="bg-green-300 rounded-lg text-white font-bold p-3">
        Poslali smo vam potvrdu na e-poštu. Proverite svoju e-poštu i potvrdite je.
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

      <label class="pt-3 text-xs mb-2">EMAIL:</label>
      <input
          class="border-b-2 border-st5 bg-st2 transition duration-500
               text-st5 focus:outline-none focus:ring-2
               focus:ring-st5"
          type="email"
          placeholder="mojaposta@mail.com"
          v-model="email"
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
  name: "PasswordForgot",
  data() {
    const router = useRouter();
    const route = useRoute();
    const auth = useAuthStore();
    return {
      email: "",
      success: true,
      error: null,
      loading: false,
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
      this.router.push('/')
    },
    toggleSuccess() {
      this.success = !this.success;
    },

    async handlePasswordForgot() {
      try {
        this.loading = true;
        const response = await this.auth.forgotPassword(this.email);
        if (response.status === 200) {
          this.success = true;
        }

      } catch (error) {
        this.error = "Не удалось начать вход через Google.";
      }finally {
        this.loading = false;
      }
    },
  },
};
</script>


