<template>
  <div class="flex justify-center mt-2 items-center md:container">
    <div class="bg-st2 rounded-lg w-full md:w-1/2 p-4 text-center">
      <h1 class="text-2xl mb-4">Izmena ličnih podataka</h1>
      <form @submit.prevent="submitForm" class="flex flex-col text-start space-y-4">

        <!-- Name -->
        <div>
          <label for="name" class="text-sm font-medium text-st5">Ime</label>
          <input
              id="name"
              type="text"
              v-model="formData.name"
              class="w-full mt-1 p-2 border rounded-md text-st5"
              placeholder="Unesite ime"
              required
          />
        </div>

        <!-- Email -->
        <div>
          <label for="email" class="text-sm font-medium text-st5">Email</label>
          <input
              id="email"
              type="email"
              v-model="formData.email"
              class="w-full mt-1 p-2 border rounded-md text-st5"
              placeholder="Unesite email"
              required
          />
        </div>


        <!-- Phone -->
        <div>
          <label for="phone" class="text-sm font-medium text-st5">Telefon</label>
          <input
              id="phone"
              type="text"
              v-model="formData.phone"
              class="w-full mt-1 p-2 border rounded-md text-st5"
              placeholder="+381 ..."
          />
        </div>

        <!-- Viber -->
        <div>
          <label for="viber" class="text-sm font-medium text-st5">Viber</label>
          <input
              id="viber"
              type="text"
              v-model="formData.viber"
              class="w-full mt-1 p-2 border rounded-md text-st5"
              placeholder="+381 ..."
          />
        </div>

        <!-- Location -->
        <div>
          <label for="city" class="text-sm font-medium text-st5">Grad</label>
          <select
              id="city"
              v-model="formData.city"
              class="w-full mt-1 p-2 border rounded-md text-st5"
          >
            <option value="" disabled>Izaberite grad</option>
            <option v-for="city in cities" :key="city.id" :value="city.name">
              {{ city.name }}
            </option>
          </select>
        </div>

        <div>
          <label for="street" class="text-sm font-medium text-st5">Ulica</label>
          <input
              id="street"
              type="text"
              v-model="formData.street"
              class="w-full mt-1 p-2 border rounded-md text-st5"
              placeholder="Unesite ulicu"
          />
        </div>

        <!--        error  -->

        <div v-if="error" class="mt-2 flex items-center justify-center text-red-600">
          <h2>{{ error }}</h2>
        </div>


        <!-- Submit button -->
        <button
            type="submit"
            class="bg-st3 text-white font-medium py-2 px-4 rounded-md hover:bg-st4 transition"
        >
          Izmeni
        </button>
      </form>
    </div>
  </div>
</template>


<script>
import {useAuthStore} from "@/stores/auth.js";
import {cities} from "@/assets/cities.js";
import {updateUserDetails} from "@/services/userService.js";

export default {
  name: "EditUserDetails",
  data() {
    const store = useAuthStore();
    return {
      cities,
      store,
      error: null,
      formData: {
        name: "",
        email: "",
        city: "",
        street: "",
        phone: "",
        viber: "",
      },
    };
  },
  methods: {
    async submitForm() {
      try {

        const userId = this.store.profile.id;

        const updatedUser = await updateUserDetails(userId, {
          name: this.formData.name,
          email: this.formData.email,
          street: this.formData.street,
          city: this.formData.city,
          phone: this.formData.phone,
          viber: this.formData.viber,
        });

        await this.store.fetchUser();
        this.$router.push("/users/" + userId);
      } catch (error) {
        console.error("Error updating user:", error.message);
        this.error = error.message;
      }
    },
  },
  mounted() {
    if (this.store.profile) {
      this.formData.name = this.store.profile.name || "";
      this.formData.email = this.store.profile.email || "";
      this.formData.phone = this.store.profile.phone || "";
      this.formData.viber = this.store.profile.viber || "";
      this.formData.city = this.store.profile.city || "";
      this.formData.street = this.store.profile.street || "";
    }
  },
};
</script>

