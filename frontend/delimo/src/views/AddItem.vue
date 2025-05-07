<template>
  <!-- Попап при успешном создании -->
  <PopUpModal :is-active="isPopUp" @close="togglePopUp">
    <div class="flex flex-col items-center justify-center gap-2">
      <h1>Stvar je kreirana uspešno!</h1>
      <button
          class="bg-st3 text-white font-medium py-2 px-4 rounded-md hover:bg-st4 transition"
          @click="goToItem"
      >
        Hvala!
      </button>
    </div>
  </PopUpModal>

  <!-- Форма добавления вещи -->
  <div class="flex justify-center mt-2 items-center md:container">
    <div class="bg-st2 md:rounded-lg w-full md:w-1/2 p-4 text-center">
      <h1 class="text-2xl mb-4">Dodaj stvar</h1>

      <!-- Лоадер -->
      <div v-if="loading" class="flex items-center justify-center h-svh">
        <Loader />
      </div>

      <!-- Форма -->
      <form
          v-else
          @submit.prevent="submitForm"
          class="flex flex-col text-start space-y-4"
      >
        <!-- Title -->
        <div>
          <label for="title" class="text-sm font-medium text-st5">Naslov</label>
          <input
              id="title"
              v-model="formData.title"
              type="text"
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
              rows="4"
              placeholder="Unesite opis stvari"
              required
          ></textarea>
        </div>

        <!-- Max period -->
        <div>
          <label for="maxDays" class="text-sm font-medium text-st5"
          >Maksimalni period (dani)</label
          >
          <input
              id="maxDays"
              v-model.number="formData.maxPeriodDays"
              type="number"
              min="1"
              class="w-full mt-1 p-2 border rounded-md text-st4"
              placeholder="Unesite maksimalni broj dana"
              required
          />
        </div>

        <!-- Price / Free -->
        <div class="flex flex-col">
          <label for="price" class="text-sm font-medium text-st5">Cena</label>
          <div class="flex items-center gap-4">
            <input
                id="price"
                v-model.number="formData.pricePerDay"
                :disabled="isFree"
                type="number"
                min="0"
                step="0.01"
                class="w-full mt-1 p-2 border rounded-md text-st5"
                placeholder="100.00 RSD"
                required
            />
            <div class="flex items-center gap-1">
              <input
                  id="besplatno"
                  v-model="isFree"
                  type="checkbox"
                  class="accent-st5 scale-125"
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
              v-model="formData.phone"
              type="text"
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
              v-model="formData.viber"
              type="text"
              class="w-full mt-1 p-2 border rounded-md text-st5"
              placeholder="+381 ..."
          />
        </div>

        <!-- City -->
        <div>
          <label for="city" class="text-sm font-medium text-st5">Grad</label>
          <select
              id="city"
              v-model="formData.city"
              class="w-full mt-1 p-2 border rounded-md text-st5"
              required
          >
            <option value="" disabled>Izaberite grad</option>
            <option v-for="city in cities" :key="city.id" :value="city.name">
              {{ city.name }}
            </option>
          </select>
        </div>

        <!-- Street -->
        <div>
          <label for="street" class="text-sm font-medium text-st5">Ulica</label>
          <input
              id="street"
              v-model="formData.street"
              type="text"
              class="w-full mt-1 p-2 border rounded-md text-st5"
              placeholder="Unesite lokaciju (grad, adresa)"
              required
          />
        </div>

        <!-- Photos upload -->
        <div>
          <label class="text-sm font-medium text-st5">Fotografije (maks. 5)</label>
          <div
              class="cursor-pointer bg-gray-400 p-3 rounded-lg mt-2 text-center hover:bg-st5 hover:text-white flex items-center justify-center"
          >
            <label for="file-input" class="w-full">
              Kliknite da dodate fotografije
              <input
                  id="file-input"
                  type="file"
                  accept="image/*"
                  multiple
                  class="hidden"
                  @change="handleFileUpload"
              />
            </label>
          </div>
          <ul class="mt-2">
            <li
                v-for="(file, i) in uploadedFiles"
                :key="i"
                class="text-sm text-st5"
            >
              {{ file.name }}
            </li>
          </ul>
          <div v-if="imageError" class="text-red-500 mt-1">
            {{ imageError }}
          </div>
        </div>

        <!-- Submit -->
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
import { useAuthStore } from "@/stores/auth.js";
import PopUpModal from "@/components/UI/PopUpModal.vue";
import Loader from "@/components/UI/Loader.vue";
import { cities } from "@/assets/cities.js";
import { createItem } from "@/services/itemService.js";
import router from "@/router/index.js";

export default {
  name: "AddItem",
  components: { Loader, PopUpModal },
  data() {
    return {
      store: useAuthStore(),
      cities,
      isPopUp: false,
      loading: false,
      imageError: null,
      uploadedFiles: [],
      itemId: null,
      isFree: false,
      formData: {
        title: "",
        description: "",
        maxPeriodDays: 1,
        street: "",
        city: "",
        pricePerDay: null,
        phone: "",
        viber: "",
      },
    };
  },
  mounted() {
    if (this.store.profile) {
      Object.assign(this.formData, {
        phone: this.store.profile.phone || "",
        viber: this.store.profile.viber || "",
        street: this.store.profile.street || "",
        city: this.store.profile.city || "",
      });
    }
  },
  methods: {
    togglePopUp() {
      this.isPopUp = !this.isPopUp;
    },
    goToItem() {
      this.togglePopUp();
      if (this.itemId) {
        router.push(`/items/${this.itemId}`);
      }
    },
    handleFileUpload(event) {
      const MAX_FILES = 5;
      const MAX_SIZE = 10 * 1024 * 1024;
      const ALLOWED = ["image/jpeg", "image/png", "image/jpg"];
      const files = Array.from(event.target.files);

      for (const file of files) {
        if (!ALLOWED.includes(file.type)) {
          this.imageError = `Slika \"${file.name}\" nije podržana.`;
          return;
        }
        if (file.size > MAX_SIZE) {
          this.imageError = `Slika \"${file.name}\" je prevelika.`;
          return;
        }
      }

      if (this.uploadedFiles.length + files.length > MAX_FILES) {
        this.imageError = "Možete dodati najviše 5 fotografija.";
        return;
      }

      this.uploadedFiles.push(...files);
      this.imageError = null;
    },
    async submitForm() {
      try {
        this.loading = true;

        const itemPayload = {
          title: this.formData.title,
          description: this.formData.description,
          pricePerDay: this.isFree ? 0 : this.formData.pricePerDay,
          maxPeriodDays: this.formData.maxPeriodDays,
          city: this.formData.city,
          street: this.formData.street,
          phone: this.formData.phone,
          viber: this.formData.viber,
        };
        const formData = new FormData();
        formData.append("item", new Blob([JSON.stringify(itemPayload)], {type: "application/json"}));

        this.uploadedFiles.forEach(file => formData.append("images", file));

        const response = await createItem(formData);
        if (response.status === 201) {
          this.itemId = response.data.id;
          this.togglePopUp();
        }
      } catch (e) {
        console.error(e);
      } finally {
        this.loading = false;
      }
    },
  },
};
</script>
