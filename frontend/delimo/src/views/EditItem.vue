<template>
  <div class="flex justify-center mt-2 items-center md:container flex-col">
    <div class="flex flex-col w-full bg-st2 p-4 rounded-lg md:w-1/2">
      <h1 class="text-2xl font-bold mb-4">Izmeni stvar</h1>

      <div v-if="post">
        <form @submit.prevent="updatePost">

          <!-- Naslov -->
          <div class="mb-4">
            <label class="block font-semibold mb-1" for="name">Naslov</label>
            <input
                id="name"
                v-model="formData.name"
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
                  required
              />
              <div class="flex items-center gap-1">
                <input
                    class="accent-st5 scale-125"
                    type="checkbox"
                    v-model="formData.isFree"
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
                    :disabled="formData.image.length >= 5"
                />
              </label>
            </div>

            <!-- Preview images -->
            <div class="flex flex-wrap gap-4 mt-2">
              <div
                  v-for="(image, index) in formData.image"
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
            <p v-if="formData.image.length >= 5" class="text-sm text-red-500 mt-2">
              Maksimalno 5 fotografija.
            </p>
          </div>

          <!-- Submit and Cancel buttons -->
          <button
              type="submit"
              class="bg-st4 mr-2 text-white px-3 py-2 px-4 rounded hover:bg-st3 transition duration-500"
          >
            Sačuvaj
          </button>
          <router-link
              :to="`/items/${id}`"
              class="bg-gray-300 text-black px-4 py-2 rounded"
          >
            Odustani
          </router-link>
        </form>
      </div>

      <div v-else>
        <p>Loading...</p>
      </div>
    </div>
  </div>
</template>

<script>
import { items } from "@/assets/items";

export default {
  name: "EditItem",
  props: {
    id: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      post: null,
      formData: {
        name: "",
        description: "",
        pricePerDay: "",
        isFree: false, // New property to toggle "besplatno"
        available: false,
        maxPeriodDays: 1,
        image: [] // Array to store uploaded images with previews
      }
    };
  },
  created() {
    const numericId = Number(this.$route.params.id);
    this.post = items.find((item) => item.id === numericId);

    if (this.post) {
      this.formData = {
        name: this.post.name,
        description: this.post.description,
        pricePerDay: this.post.pricePerDay ?? "", // Handle null pricePerDay
        isFree: this.post.pricePerDay === 0 || this.post.pricePerDay === null, // Pre-select "besplatno" if price is 0 or null
        available: this.post.available || false,
        maxPeriodDays: this.post.maxPeriodDays || 1,
        image: this.post.image.map((url) => ({preview: url})) // Add existing images as previews
      };
    } else {
      console.warn("No item found with ID:", numericId);
    }
  },
  methods: {
    handleFileUpload(event) {
      const files = Array.from(event.target.files);
      const remainingSlots = 5 - this.formData.image.length;

      files.slice(0, remainingSlots).forEach((file) => {
        const reader = new FileReader();
        reader.onload = (e) => {
          this.formData.image.push({preview: e.target.result});
        };
        reader.readAsDataURL(file);
      });

      event.target.value = null;
    },
    removeImage(index) {
      this.formData.image.splice(index, 1);
    },
    updatePost() {
      if (!this.post) return;

      this.post.name = this.formData.name;
      this.post.description = this.formData.description;
      this.post.pricePerDay = this.formData.isFree ? 0 : this.formData.pricePerDay; // Set price to 0 if "besplatno"
      this.post.available = this.formData.available;
      this.post.image = this.formData.image.map((img) => img.preview);

      this.$router.push(`/items/${this.id}`);
    }
  }
};
</script>
