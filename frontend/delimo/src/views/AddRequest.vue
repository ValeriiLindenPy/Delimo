

<template>
  <PopUpModal :is-active="isPopUp" @close="tooglePopUp">
    <div class="flex flex-col items-center justify-center gap-2">
      <h1>Strvar je kreirana uspesno!</h1>
      <button class="bg-st3 text-white font-medium py-2 px-4 rounded-md hover:bg-st4 transition" @click="tooglePopUp">Hvala!</button>
    </div>
  </PopUpModal>

  <div class="flex justify-center mt-2 items-center md:container">
    <div class="bg-st2 rounded-lg w-full md:w-1/2 p-4 text-center">
      <h1 class="text-2xl mb-4">Dodaj Zahtev</h1>
      <form @submit.prevent="submitForm" class="flex flex-col text-start space-y-4">
        <!-- Name -->
        <div>
          <label for="name" class="text-sm font-medium text-st5">Naslov</label>
          <input
              id="name"
              type="text"
              v-model="formData.title"
              class="w-full mt-1 p-2 border rounded-md text-st5"
              placeholder="Unesite naslov stvari"
              required
          />
        </div>

        <!-- Description -->
        <div>
          <label for="description" class="text-sm font-medium text-st5">Opis</label>
          <textarea
              id="description"
              v-model="formData.description"
              class="w-full mt-1 p-2 border rounded-md text-st5"
              placeholder="Unesite opis stvari"
              rows="4"
              required
          ></textarea>
        </div>


        <!-- Price -->
        <div class="flex flex-col">
          <label for="price" class="text-sm font-medium text-st5">Cena</label>
          <div class="flex items-center gap-4">
            <input
                id="price"
                type="text"
                v-model="formData.pricePerDay"
                :disabled="isFree"
                class="w-full mt-1 p-2 border rounded-md text-st5"
                placeholder="100.00 RSD"
                required
            />
            <div class="flex items-center gap-1">
              <input
                  class="accent-st5 scale-125"
                  type="checkbox"
                  v-model="isFree"
                  id="besplatno"
              />
              <p>besplatno</p>
            </div>
          </div>
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
              required
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

        <!-- Submit button -->
        <button
            type="submit"
            class="bg-st3 text-white font-medium py-2 px-4 rounded-md hover:bg-st4 transition"
        >
          Dodaj stvar
        </button>
      </form>
    </div>
  </div>
</template>


<script>
import {useAuthStore} from "@/stores/auth.js";
import PopUpModal from "@/components/UI/PopUpModal.vue";
import { cities } from "@/assets/cities.js";
import {createRequest} from "@/services/requestService.js";

export default {
  name: "AddRequest",
  components: { PopUpModal },
  data() {
    const store = useAuthStore();
    return {
      store,
      cities,
      isPopUp: false,
      formData: {
        title: "",
        description: "",
        city: "",
        pricePerDay: null,
        phone: "",
        viber: "",
      },
      isFree: false,
    };
  },

  mounted() {
    // Auto-fill phone, viber, and location if available in the user profile
    if (this.store.profile) {
      this.formData.phone = this.store.profile.phone || "";
      this.formData.viber = this.store.profile.viber || "";
      this.formData.street = this.store.profile.street || "";
      this.formData.city = this.store.profile.city || "";
    }
  },
  methods: {
    tooglePopUp() {
      this.isPopUp = !this.isPopUp;
    },

    async submitForm() {
      const formData = new FormData();
      formData.append("title", this.formData.title);
      formData.append("description", this.formData.description);
      formData.append("city", this.formData.city);
      formData.append("pricePerDay", this.isFree ? 0 : this.formData.pricePerDay);
      formData.append("phone", this.formData.phone);
      formData.append("viber", this.formData.viber);


      try {
        const response = await createRequest(formData);

        this.tooglePopUp();
        this.formData = {
          title: "",
          description: "",
          city: this.store.profile.city || "",
          pricePerDay: null,
          phone: this.store.profile.phone || "",
          viber: this.store.profile.viber || "",
        };
        this.isFree = false;
      } catch (error) {
        console.error("Error:", error);
      }
    },
  },
};
</script>

