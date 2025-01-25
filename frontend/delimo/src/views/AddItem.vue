

<template>
  <PopUpModal :is-active="isPopUp" @close="tooglePopUp">
    <div class="flex flex-col items-center justify-center gap-2">
      <h1>Strvar je kreirana uspesno!</h1>
      <button class="bg-st3 text-white font-medium py-2 px-4 rounded-md hover:bg-st4 transition" @click="goItem">Hvala!</button>
    </div>
  </PopUpModal>

  <div class="flex justify-center mt-2 items-center md:container">
    <div class="bg-st2 rounded-lg w-full md:w-1/2 p-4 text-center">
      <h1 class="text-2xl mb-4">Dodaj stvar</h1>
      <div v-if="loading" class="flex items-center justify-center h-svh">
        <Loader/>
      </div>
      <form v-else @submit.prevent="submitForm" class="flex flex-col text-start space-y-4">
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
              v-model="formData.maxPeriodDays"
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


        <div>
          <label for="street" class="text-sm font-medium text-st5">Ulica</label>
          <input
              id="street"
              type="text"
              v-model="formData.street"
              class="w-full mt-1 p-2 border rounded-md text-st5"
              placeholder="Unesite lokaciju (grad, adresa)"
              required
          />
        </div>

        <!-- Upload photos -->
        <div>
          <label class="text-sm font-medium text-st5">Fotografije (maks. 5)</label>
          <div
              class="cursor-pointer bg-gray-400 p-3 rounded-lg mt-2 text-center hover:bg-st5 hover:text-white flex items-center justify-center"
          >
            <label for="custom-file-input" class="w-full">
              Kliknite da dodate fotografije
              <input
                  id="custom-file-input"
                  type="file"
                  @change="handleFileUpload"
                  class="hidden"
                  accept="image/*"
                  multiple
              />
            </label>
          </div>
          <ul class="mt-2">
            <li
                v-for="(file, index) in uploadedFiles"
                :key="index"
                class="text-sm text-st5"
            >
              {{ file.name }}
            </li>
          </ul>
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
import {createItem} from "@/services/itemService.js";
import router from "@/router/index.js";
import Loader from "@/components/UI/Loader.vue";


export default {
  name: "AddItem",
  components: {Loader, PopUpModal },
  data() {
    const store = useAuthStore();
    return {
      itemId: null,
      loading: false,
      router,
      store,
      cities,
      isPopUp: false,
      uploadedFiles: [],
      formData: {
        title: "",
        description: "",
        maxPeriodDays: null,
        street: "",
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
    handleFileUpload(event) {
      const files = Array.from(event.target.files);
      if (this.uploadedFiles.length + files.length > 5) {
        alert("Možete dodati najviše 5 fotografija.");
        return;
      }
      this.uploadedFiles = [...this.uploadedFiles, ...files];
    },
    goItem() {
      this.tooglePopUp();
      if (this.itemId) {
        this.$router.push(`/items/${this.itemId}`);
      }
    },
    async submitForm() {

      const formData = new FormData();
      formData.append("title", this.formData.title);
      formData.append("description", this.formData.description);
      formData.append("maxPeriodDays", this.formData.maxPeriodDays);
      formData.append("street", this.formData.street);
      formData.append("city", this.formData.city);
      formData.append("pricePerDay", this.isFree ? 0 : this.formData.pricePerDay);
      formData.append("phone", this.formData.phone);
      formData.append("viber", this.formData.viber);
      this.uploadedFiles.forEach(file => formData.append("images", file));

      try {
        this.loading = true;
        const response = await createItem(formData);

        if (response.status === 201) {
          this.itemId = response.data.id;
          this.loading = false;
          this.uploadedFiles = [];
          this.formData = {
            title: "",
            description: "",
            maxPeriodDays: 1,
            street: this.store.profile.street || "",
            city: this.store.profile.city || "",
            pricePerDay: null,
            phone: this.store.profile.phone || "",
            viber: this.store.profile.viber || "",
          };
          this.isFree = false;
          this.tooglePopUp();
        }

      } catch (error) {
        await this.router.push("/");
        console.error("Error:", error);
      }
    },
  },
};
</script>

