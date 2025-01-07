<template>
  <div class="flex justify-center mt-2 items-center md:container flex-col">
    <div class="flex flex-col w-full bg-st2 p-4 rounded-lg md:w-1/2">
      <h1 class="text-2xl font-bold mb-4">Izmeni zahtev</h1>

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



          <!-- Submit and Cancel buttons -->
          <button
              type="submit"
              class="bg-st4 mr-2 text-white py-2 px-4 rounded hover:bg-st3 transition duration-500"
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
import {requests} from "@/assets/requests.js";

export default {
  name: "EditRequest",
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
        pricePerDay: "",
        isFree: false, // New property to toggle "besplatno"
        maxPeriodDays: 1,
      }
    };
  },
  created() {
    const numericId = Number(this.$route.params.id);
    this.post = requests.find((item) => item.id === numericId);

    if (this.post) {
      this.formData = {
        name: this.post.name,
        pricePerDay: this.post.pricePerDay ?? "", // Handle null pricePerDay
        isFree: this.post.pricePerDay === 0 || this.post.pricePerDay === null, // Pre-select "besplatno" if price is 0 or null
        maxPeriodDays: this.post.maxPeriodDays || 1,
      };
    } else {
      console.warn("No item found with ID:", numericId);
    }
  },
  methods: {
    updatePost() {
      if (!this.post) return;

      this.post.name = this.formData.name;
      this.post.description = this.formData.description;
      this.post.pricePerDay = this.formData.isFree ? 0 : this.formData.pricePerDay; // Set price to 0 if "besplatno"

      this.$router.push(`/requests/${this.id}`);
    }
  }
};
</script>
