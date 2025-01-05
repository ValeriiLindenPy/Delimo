

<template>
  <div class="flex justify-center mt-2 items-center md:container">
    <div class="bg-st2 rounded-lg w-full md:w-1/2 p-4 text-center">
      <h1 class="text-2xl mb-4">Kreiraj zahtev</h1>
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

        <!-- Max rent period -->
        <div>
          <label for="maxDays" class="text-sm font-medium text-st5">Maksimalni period (dani)</label>
          <input
              id="maxDays"
              type="number"
              v-model="formData.maxDays"
              class="w-full mt-1 p-2 border rounded-md text-st4"
              min="1"
              placeholder="Unesite maksimalni broj dana"
              required
          />
        </div>

        <!-- Price -->
        <div class="flex flex-col">
          <label for="price" class="text-sm font-medium text-st5">Cena</label>
          <div class="flex items-center gap-4">
            <input
                id="price"
                type="text"
                v-model="formData.price"
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
          <label for="location" class="text-sm font-medium text-st5">Lokacija</label>
          <input
              id="location"
              type="text"
              v-model="formData.location"
              class="w-full mt-1 p-2 border rounded-md text-st5"
              placeholder="Unesite lokaciju (grad, adresa)"
              required
          />
        </div>


        <!-- Submit button -->
        <button
            type="submit"
            class="bg-st3 text-white font-medium py-2 px-4 rounded-md hover:bg-st4 transition"
        >
          Kreiraj zahtev
        </button>
      </form>
    </div>
  </div>
</template>


<script>
import { useUserStore } from "@/stores/counter.js";

export default {
  name: "AddRequest",
  data() {
    return {
      uploadedFiles: [],
      formData: {
        title: "",
        description: "",
        status: "novo",
        maxDays: 1,
        location: "",
        price: null,
        images: [],
        phone: "", // Added phone
        viber: "", // Added viber
      },
      isFree: false,
    };
  },
  computed: {
    userProfile() {
      return useUserStore().profile;
    },
  },
  methods: {
    handleFileUpload(event) {
      const files = Array.from(event.target.files);
      // Limit to 5 files
      if (this.uploadedFiles.length + files.length > 5) {
        alert("Možete dodati najviše 5 fotografija.");
        return;
      }
      this.uploadedFiles = [...this.uploadedFiles, ...files];
      this.formData.images = [...this.uploadedFiles];
    },
    submitForm() {
      console.log("Podaci poslati:", this.formData);
      alert("Stvar je uspešno dodata!");
      // Reset the form
      this.uploadedFiles = [];
      this.formData = {
        title: "",
        description: "",
        status: "novo",
        maxDays: 1,
        location: this.userProfile.location || "",
        phone: this.userProfile.phone || "",
        viber: this.userProfile.viber || "",
        price: null,
        images: [],
      };
      this.isFree = false;
    },
  },
  mounted() {
    // Auto-fill phone, viber, and location if available in the user profile
    if (this.userProfile) {
      this.formData.phone = this.userProfile.phone || "";
      this.formData.viber = this.userProfile.viber || "";
      this.formData.location = this.userProfile.location || "";
    }
  },
};
</script>
