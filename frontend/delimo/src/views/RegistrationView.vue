<template>
  <div class="w-full bg-st2">
  <div class="container flex flex-col justify-center items-center my-auto">
    <form class="flex flex-col p-6 gap-2 bg-st2 rounded-lg w-auto md:w-1/3"
          @submit.prevent="validateAndSubmit">
      <div class="flex gap-2 justify-center items-center">
        <b class="text-3xl font-extrabold text-st5">Del</b>
        <i class="fa-solid fa-arrows-spin text-3xl text-st3"></i>
        <b class="text-3xl font-extra bold text-st5">imo</b>
      </div>

      <!-- firstName -->
      <label class="pt-3 text-xs font-bold">IME (*):</label>
      <input class="border-b-2 border-st5 bg-st2 transition duration-500
      text-st5 focus:outline-none focus:ring-2
      focus:ring-st5" type="text" name="firstName" id="firstName" v-model="firstName" required/>

      <!-- lastName -->
      <label class="pt-3 text-xs font-bold">PREZIME (*):</label>
      <input class="border-b-2 border-st5 bg-st2 transition duration-500
      text-st5 focus:outline-none focus:ring-2
      focus:ring-st5" type="text" name="lastName" id="lastName" v-model="lastName" required/>

      <!-- EMAIL -->
      <label class="pt-3 text-xs font-bold">EMAIL (*):</label>
      <input class="border-b-2 border-st5 bg-st2 transition duration-500
      text-st5 focus:outline-none focus:ring-2
      focus:ring-st5" type="email" name="email" id="email" v-model="email" required/>

      <!-- password -->
      <label class="pt-3 text-xs font-bold">LOZINKA (*):</label>
      <input class="border-b-2 border-st5 bg-st2 transition duration-500
      text-st5 focus:outline-none focus:ring-2
      focus:ring-st5" type="password" name="password"
             id="password" v-model="password" required/>

      <!-- password2 -->
      <label class="pt-3 text-xs font-bold">PONOVITE LOZINKU (*):</label>
      <input class="border-b-2 border-st5 bg-st2 transition duration-500
      text-st5 focus:outline-none focus:ring-2
      focus:ring-st5" type="password" name="password2"
             id="password2" v-model="password2" required/>

      <!-- city -->
      <div>
        <label for="city" class="pt-3 text-xs">GRAD:</label>
        <select
            id="city"
            v-model="city"
            class="w-full mt-1 p-2 border rounded-md text-st5"
        >
          <option value="" disabled>Izaberite grad</option>
          <option v-for="city in cities" :key="city.id" :value="city.name">
            {{ city.name }}
          </option>
        </select>
      </div>

      <!-- street -->
      <label class="pt-3 text-xs">ULICA:</label>
      <input class="border-b-2 border-st5 bg-st2 transition duration-500
      text-st5 focus:outline-none focus:ring-2
      focus:ring-st5" type="text" name="street" id="street" v-model="street"/>

      <!-- phone -->
      <label class="pt-3 text-xs">TELEFON:</label>
      <input class="border-b-2 border-st5 bg-st2 transition duration-500
      text-st5 focus:outline-none focus:ring-2
      focus:ring-st5" type="text" name="phone" id="phone" v-model="phone"/>

      <!-- phone -->
      <label class="pt-3 text-xs">VIBER <i>(ako imate)</i>:</label>
      <input class="border-b-2 border-st5 bg-st2 transition duration-500
      text-st5 focus:outline-none focus:ring-2
      focus:ring-st5" type="text" name="phone" id="phone" v-model="phone"/>

      <div v-if="errorMessage" class="text-red-500 text-sm mt-2">{{ errorMessage }}</div>

      <p class="pt-2 text-xs font-bold">(*) required fields</p>

      <div class="flex items-center mt-2 mb-2">
        <input id="default-checkbox" type="checkbox" value="" class="w-4 h-4 rounded
border-b-2 border-st5 bg-st2 transition duration-500
      text-st5 focus:outline-none focus:ring-2
      focus:ring-st5 checked:bg-st5 checked:border-st5">
        <label for="default-checkbox" class="ms-2 text-sm font-medium text-gray-900
        dark:text-gray-300">Prihvatam Pravila i uslove korišćenja</label>
      </div>
      <div class="flex items-center mb-2">
        <input id="default-checkbox" type="checkbox" value="" class="w-4 h-4 rounded
border-b-2 border-st5 bg-st2 transition duration-500
      text-st5 focus:outline-none focus:ring-2
      focus:ring-st5 checked:bg-st5 checked:border-st5">
        <label for="default-checkbox" class="ms-2 text-sm font-medium text-st5
        dark:text-st5">Imam više od 16 godina</label>
      </div>

      <div class="flex items-center justify-center pt-2">
        <button type="submit" class="bg-st3 px-16 shadow-lg p-3
        rounded-lg transition-colors duration-500
        hover:bg-st4 hover:text-white">Nastavi
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
  </div>
</template>

<script>
import {useUserStore} from "@/stores/counter.js";
import {cities} from "@/assets/cities.js";

export default {
  name: "RegistrationView",
  data() {
    return {
      cities,
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
      useUserStore: useUserStore(),
    };
  },

  methods: {

    async handleGoogleOneTap() {
      // Logic for handling Google One Tap Login
    },
    validateAndSubmit() {
      const passwordRegex = /^(?=.*\d)(?=.*[!@#$%^&*])(?=.*[a-zA-Z])[a-zA-Z\d!@#$%^&*]{8,20}$/;

      if (!passwordRegex.test(this.password)) {
        this.errorMessage = "Lozinka mora imati od 8 do 20 karaktera, jednu cifru i jedan specijalni simbol.";
        return;
      }

      if (this.password !== this.password2) {
        this.errorMessage = "Lozinke se ne poklapaju.";
        return;
      }



      this.errorMessage = "";
      // Proceed with form submission logic
      console.log("Form submitted:", this.email, this.password);
    },
  },
};
</script>

<!--<style scoped>-->
<!--.container {-->
<!--  height: 100vh;-->
<!--}-->
<!--</style>-->
