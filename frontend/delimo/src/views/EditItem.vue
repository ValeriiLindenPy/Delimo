<template>
  <!-- Loader отображается, когда isLoading равен true -->


  <PopUpModal :is-active="isPopUp" @close="togglePopUp">
    <div class="flex flex-col items-center justify-center gap-2">
      <h1>Stvar je promenjena uspešno!</h1>
      <button class="bg-st3 text-white font-medium py-2 px-4 rounded-md hover:bg-st4 transition" @click="togglePopUp">
        Hvala!
      </button>
    </div>
  </PopUpModal>

  <Loader v-if="isLoading" />

  <div v-else class="flex justify-center mt-2 items-center md:container flex-col">
    <div class="flex flex-col w-full bg-st2 p-4 rounded-lg md:w-1/2">
      <h1 class="text-2xl font-bold mb-4">Izmeni stvar</h1>

      <div v-if="post">
        <form @submit.prevent="updatePost">
          <!-- Naslov -->
          <div class="mb-4">
            <label class="block font-semibold mb-1" for="name">Naslov</label>
            <input
                id="name"
                v-model="formData.title"
                class="border px-2 py-1 w-full rounded-lg"
                type="text"
            />
          </div>

          <!-- Opis -->
          <div class="mb-4">
            <label class="block font-semibold mb-1" for="description">Opis</label>
            <textarea
                id="description"
                v-model="formData.description"
                class="border px-2 py-1 w-full rounded-lg"
            ></textarea>
          </div>

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

          <!-- Cena -->
          <div class="flex flex-col mb-4">
            <label for="price" class="text-sm font-medium text-st5">Cena</label>
            <div class="flex items-center gap-4">
              <input
                  id="price"
                  type="text"
                  v-model="formData.pricePerDay"
                  :disabled="formData.isFree"
                  class="w-full mt-1 p-2 border rounded-md text-st5"
                  placeholder="100.00 RSD"
              />
              <div class="flex items-center gap-1">
                <input
                    class="accent-st5 scale-125"
                    type="checkbox"
                    v-model="isPriceFree"
                    id="besplatno"
                />
                <p>besplatno</p>
              </div>
            </div>
          </div>

          <!-- Dostupno -->
          <div class="mb-4 flex items-center">
            <input
                id="available"
                v-model="formData.available"
                class="mr-2 scale-125 accent-st5"
                type="checkbox"
            />
            <label for="available" class="text-sm font-medium text-st5">Dostupno</label>
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

          <!-- Grad -->
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

          <!-- Ulica -->
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

          <!-- Fotografije -->
          <div class="mb-4">
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
                    :disabled="formData.images.length >= 5"
                />
              </label>
            </div>

            <!-- Pregled fotografija -->
            <div class="flex flex-wrap gap-4 mt-2">
              <div
                  v-for="(image, index) in formData.images"
                  :key="index"
                  class="relative"
              >
                <img
                    :src="image.preview"
                    class="w-24 h-24 object-cover rounded border"
                    alt="Preview"
                />
                <i
                    class="fa-solid fa-circle-xmark absolute top-0 right-0 text-red-500 cursor-pointer text-lg"
                    @click="removeImage(index)"
                ></i>
              </div>
            </div>
            <p v-if="formData.images.length >= 5" class="text-sm text-red-500 mt-2">
              Maksimalno 5 fotografija.
            </p>
          </div>

          <!-- Submit i Odustani dugmad -->
          <button
              type="submit"
              class="bg-st4 mr-2 text-white py-2 px-4 rounded hover:bg-st3 transition duration-500"
              :disabled="isLoading"
          >
            Sačuvaj
          </button>
          <router-link :to="cancelUrl" class="bg-gray-300 text-black px-4 py-2 rounded">
            Odustani
          </router-link>
        </form>
      </div>

      <div v-else>
        <p>Učitavanje...</p>
      </div>
    </div>
  </div>
</template>

<script>
import { fetchMyItem, updateItem } from "@/services/itemService";
import PopUpModal from "@/components/UI/PopUpModal.vue";
import Loader from "@/components/UI/Loader.vue";
import { cities } from "@/assets/cities.js";
import { useAuthStore } from "@/stores/auth.js";

export default {
  name: "EditItem",
  components: {PopUpModal, Loader},
  data() {
    return {
      store: useAuthStore(),
      cities,
      id: Number(this.$route.params.id),
      isPopUp: false,
      isLoading: false,
      post: null,
      formData: {
        title: "",
        description: "",
        pricePerDay: "",
        isFree: false,
        available: false,
        maxPeriodDays: 1,
        phone: "",
        viber: "",
        images: [],
        street: null,
        city: null,
      },
      imageError: null,
    };
  },
  computed: {
    cancelUrl() {
      return `/items/${this.id}`;
    },
    isPriceFree: {
      get() {
        return this.formData.isFree;
      },
      set(value) {
        this.formData.isFree = value;
        if (value) this.formData.pricePerDay = 0;
      },
    },
  },
  async created() {
    try {
      // Приказ Loader-а може бити додат ако имате дуго чекање приликом преузимања података.
      this.isLoading = true;
      const {data} = await fetchMyItem(this.id);
      if (data.owner.id !== this.store.profile.id) {
        this.$router.push(`/`);
      }
      this.post = data;
      this.formData = this.mapPostToFormData(data);
    } catch (error) {
      console.error("Error fetching item:", error);
    } finally {
      this.isLoading = false;
    }
  },
  methods: {
    togglePopUp() {
      this.isPopUp = !this.isPopUp;
    },
    async handleFileUpload(event) {
      const maxSize = 1.5 * 1024 * 1024;
      const allowedTypes = ["image/jpeg", "image/png", "image/jpg"];
      const files = Array.from(event.target.files);
      const remainingSlots = 5 - this.formData.images.length;
      let validFiles = [];

      for (const file of files.slice(0, remainingSlots)) {
        if (!allowedTypes.includes(file.type)) {
          this.imageError = `Slika "${file.name}" nije podržana. Dozvoljeni formati: JPG, JPEG, PNG.`;
          return;
        }

        if (file.size > maxSize) {
          this.imageError = `Slika "${file.name}" je prevelika. Maksimalna veličina je 1.5MB.`;
          return;
        }

        validFiles.push(file);
      }

      validFiles.forEach((file) => {
        const reader = new FileReader();
        reader.onload = (e) => {
          this.formData.images.push({
            preview: e.target.result,
            file: file,
          });
        };
        reader.readAsDataURL(file);
      });

      this.imageError = null;
      event.target.value = null;
    },
    removeImage(index) {
      this.formData.images.splice(index, 1);
    },
    mapPostToFormData(post) {
      return {
        street: post.owner.street || "",
        phone: post.owner.phone || "",
        viber: post.owner.viber || "",
        city: post.owner.city || "",
        title: post.title || "",
        description: post.description || "",
        pricePerDay: post.pricePerDay ?? "",
        isFree: post.pricePerDay === 0 || post.pricePerDay === null,
        available: post.available || false,
        maxPeriodDays: post.maxPeriodDays || 1,
        images: post.images?.map((url) => ({preview: url})) || [],
      };
    },
    async updatePost() {
      // Показање Loader-а током ажурирања
      this.isLoading = true;
      try {
        const formData = new FormData();

        formData.append("title", this.formData.title || "");
        formData.append("description", this.formData.description || "");
        formData.append(
            "pricePerDay",
            this.formData.isFree ? "0" : this.formData.pricePerDay || ""
        );
        formData.append("available", this.formData.available ? "true" : "false");
        formData.append("maxPeriodDays", this.formData.maxPeriodDays || "1");
        formData.append("phone", this.formData.phone || "");
        formData.append("viber", this.formData.viber || "");
        formData.append("city", this.formData.city || "");
        formData.append("street", this.formData.street || "");

        this.formData.images.forEach((image) => {
          if (image.file) {
            formData.append("image", image.file);
          }
        });

        const existingImages = this.formData.images
            .filter((img) => !img.file)
            .map((img) => img.preview);
        formData.append("existingImages", JSON.stringify(existingImages));

        const response = await updateItem(this.id, formData);
        if (response.status === 200) {
          this.togglePopUp();
          this.$router.push(`/items/${this.id}`);
        }
      } catch (error) {
        console.error("Error updating item:", error);
      } finally {
        this.isLoading = false;
      }
    },
  },
};
</script>
