<template>
  <PopUpModal :is-active="isPopUp" @close="togglePopUp">
    <div class="flex flex-col items-center justify-center gap-2">
      <h1 class="bg-green-300 rounded-lg text-white font-bold p-3">
        Poslali smo vam potvrdu na e-poštu. Proverite svoj e-mail i potvrdite ga.
      </h1>
      <button class="bg-st3 text-white font-medium py-2 px-4 rounded-md hover:bg-st4 transition"
              @click="toggleOk">
        Hvala!
      </button>
    </div>
  </PopUpModal>

  <div class="w-full bg-st2 min-h-screen">
    <div class="flex flex-col justify-center items-center my-auto">
      <form class="flex flex-col w-full p-4 gap-2 bg-st2 rounded-lg md:p-6 md:w-1/3"
            @submit.prevent="validateAndSubmit">
        <router-link to="/">
        <div class="flex gap-2 justify-center items-center">
          <b class="text-3xl font-extrabold text-st5">Del</b>
          <i class="fa-solid fa-arrows-spin text-3xl text-st3"></i>
          <b class="text-3xl font-extra bold text-st5">imo</b>
        </div>
        </router-link>

        <div v-if="loading" class="mt-10">
          <Loader/>
        </div>

        <div class="flex flex-col" v-else>

        <!-- firstName -->
        <label class="pt-3 text-xs font-bold">IME (*):</label>
        <input
            class="border-b-2 border-st5 bg-st2 transition duration-500 text-st5 focus:outline-none focus:ring-2 focus:ring-st5"
            type="text"
            v-model="firstName"
            required
        />

        <!-- lastName -->
        <label class="pt-3 text-xs font-bold">PREZIME (*):</label>
        <input
            class="border-b-2 border-st5 bg-st2 transition duration-500 text-st5 focus:outline-none focus:ring-2 focus:ring-st5"
            type="text"
            v-model="lastName"
            required
        />

        <!-- EMAIL -->
        <label class="pt-3 text-xs font-bold">EMAIL (*):</label>
        <input
            class="border-b-2 border-st5 bg-st2 transition duration-500 text-st5 focus:outline-none focus:ring-2 focus:ring-st5"
            type="email"
            v-model="email"
            required
        />

        <!-- password -->
        <label class="pt-3 text-xs font-bold">LOZINKA (*):</label>
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

        <!-- city -->
        <div>
          <label for="city" class="pt-3 text-xs">GRAD:</label>
          <select
              id="city"
              v-model="city"
              class="w-full mt-1 p-2 border rounded-md text-st5"
          >
            <option value="" disabled>Izaberite grad</option>
            <option v-for="cty in cities" :key="cty.id" :value="cty.name">
              {{ cty.name }}
            </option>
          </select>
        </div>

        <!-- street -->
        <label class="pt-3 text-xs">ULICA:</label>
        <input
            class="border-b-2 border-st5 bg-st2 transition duration-500 text-st5 focus:outline-none focus:ring-2 focus:ring-st5"
            type="text"
            v-model="street"
        />

        <!-- phone -->
        <label class="pt-3 text-xs">TELEFON:</label>
        <input
            class="border-b-2 border-st5 bg-st2 transition duration-500 text-st5 focus:outline-none focus:ring-2 focus:ring-st5"
            type="text"
            v-model="phone"
        />

        <!-- viber -->
        <label class="pt-3 text-xs">VIBER <i>(ako imate)</i>:</label>
        <input
            class="border-b-2 border-st5 bg-st2 transition duration-500 text-st5 focus:outline-none focus:ring-2 focus:ring-st5"
            type="text"
            v-model="viber"
        />

        <div v-if="errorMessage" class="text-red-500 text-sm mt-2">{{ errorMessage }}</div>

        <p class="pt-2 text-xs font-bold">(*) Obavezna polja</p>

        <div class="flex items-center mt-2 mb-2">
          <input
              id="terms-checkbox"
              type="checkbox"
              class="w-4 h-4 rounded border-b-2 border-st5 bg-st2 transition duration-500 text-st5 focus:outline-none focus:ring-2 focus:ring-st5 checked:bg-st5 checked:border-st5"
              required
          />
          <label for="terms-checkbox" class="ms-2 text-sm font-medium text-gray-900 dark:text-gray-300">
            Prihvatam <a href="/pravila-uslovi" class="underline hover:text-st3">Pravila i uslove korišćenja</a>
          </label>
        </div>

        <div class="flex items-center mb-2">
          <input
              id="age-checkbox"
              type="checkbox"
              class="w-4 h-4 rounded border-b-2 border-st5 bg-st2 transition duration-500 text-st5 focus:outline-none focus:ring-2 focus:ring-st5 checked:bg-st5 checked:border-st5"
              required
          />
          <label for="age-checkbox" class="ms-2 text-sm font-medium text-st5 dark:text-st5">
            Imam više od 16 godina
          </label>
        </div>

        </div>

        <div class="flex items-center justify-center pt-2">
          <button
              :disabled="loading"
              type="submit"
              class="bg-st3 disabled:bg-gray-400 px-16 shadow-lg p-3 rounded-lg transition-colors duration-500 hover:bg-st4 hover:text-white"
          >
            {{ loading ? "Učitavanje..." : "Nastavi" }}
          </button>
        </div>

        <div class="flex justify-center items-center mt-2">
          <hr class="flex-grow border-st5">
          <p class="mx-4 text-st5 font-bold">ILI</p>
          <hr class="flex-grow border-st5">
        </div>

        <!-- Google Login Button -->
        <div class="flex items-center justify-center pt-4">
          <button
              type="button"
              class="flex disabled:bg-gray-400 items-center gap-2 bg-white text-black border border-st4 rounded-lg px-4 py-2 hover:bg-st4 hover:text-white transition-colors duration-500"
              @click="handleGoogleOneTap"
              :disabled="loading"
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
  </div>
</template>

<script>
import { useAuthStore } from "@/stores/auth.js";
import { cities } from "@/assets/cities.js";
import PopUpModal from "@/components/UI/PopUpModal.vue";
import Loader from "@/components/UI/Loader.vue";

export default {
  name: "RegistrationView",
  components: {Loader, PopUpModal },
  data() {
    return {
      loading: false,
      cities,
      isPopUp: false,
      firstName: "",
      lastName: "",
      email: "",
      city: "",
      street: "",
      phone: "",
      viber: "",
      password: "",
      password2: "",
      errorMessage: "",
      auth: useAuthStore(),
    };
  },
  methods: {
    togglePopUp() {
      this.isPopUp = !this.isPopUp;
    },
    toggleOk() {
      this.isPopUp = !this.isPopUp;
      this.$router.push("/");
    },
    async handleGoogleOneTap() {
      try {
        window.location.href = `https://delimo.rs/api/oauth2/authorization/google`;
      } catch (error) {
        this.error = "Nije uspelo pokretanje prijave preko Google-a.";
      }
    },
    async validateAndSubmit() {
      const passwordRegex = /^(?=.*\d)(?=.*[!@#$%^&*])(?=.*[a-zA-Z])[a-zA-Z\d!@#$%^&*]{8,20}$/;

      // 1. Basic password format checks
      if (!passwordRegex.test(this.password)) {
        this.errorMessage = "Lozinka mora imati između 8 i 20 karaktera, najmanje jednu cifru i jedan specijalni simbol.";
        return;
      }

      // 2. Matching passwords
      if (this.password !== this.password2) {
        this.errorMessage = "Lozinke se ne poklapaju.";
        return;
      }

      // 3. Build a plain JS object for JSON submission
      const userData = {
        firstName: this.firstName,
        lastName: this.lastName,
        email: this.email,
        password: this.password,
        city: this.city,
        street: this.street,
        phone: this.phone,
        viber: this.viber,
      };

      try {
        this.loading = true;
        this.errorMessage = "";
        const res = await this.auth.register(userData);

        if (res.status === 200) {
          this.togglePopUp();
        }
      } catch (error) {
        this.errorMessage = error.response?.data?.message || error.message;
      } finally {
        this.loading = false;
      }
    },
  },
};
</script>
