<template>
  <PopUpModal :is-active="isPopUp" @close="togglePopUp">
    <div class="flex flex-col items-center justify-center gap-2">
      <h1>Zahtev je promenjen uspešno!</h1>
      <button class="bg-st3 text-white font-medium py-2 px-4 rounded-md hover:bg-st4 transition" @click="togglePopUp">
        Hvala!
      </button>
    </div>
  </PopUpModal>

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

          <!-- Submit i Odustani dugmad -->
          <button
              type="submit"
              class="bg-st4 mr-2 text-white py-2 px-4 rounded hover:bg-st3 mt-3 transition duration-500"
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
import {fetchMyRequest, updateRequest} from "@/services/requestService.js";
import PopUpModal from "@/components/UI/PopUpModal.vue";
import { cities } from "@/assets/cities.js";

export default {
  name: "EditItem",
  components: { PopUpModal },

  data() {
    return {
      cities,
      id: Number(this.$route.params.id),
      isPopUp: false,
      post: null,
      formData: {
        title: "",
        description: "",
        pricePerDay: "",
        maxPeriodDays: "",
        isFree: false,
        phone: "",
        viber: "",
        city: null,
      },
    };
  },
  computed: {
    cancelUrl() {
      return `/requests/${this.id}`;
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
      const { data } = await fetchMyRequest(this.id);
      this.post = data;
      this.formData = this.mapPostToFormData(data);
    } catch (error) {
      console.error("Error fetching item:", error);
    }
  },
  methods: {
    togglePopUp() {
      this.isPopUp = !this.isPopUp;
    },

    mapPostToFormData(post) {
      return {
        phone: post.requester.phone || "",
        viber: post.requester.viber || "",
        city: post.requester.city || "",
        title: post.title || "",
        description: post.description || "",
        pricePerDay: post.pricePerDay ?? "",
        maxPeriodDays: post.maxPeriodDays ?? "",
        isFree: post.pricePerDay === 0 || post.pricePerDay === null,
      };
    },
    async updatePost() {
      try {
        const formData = new FormData();

        // Добавление других полей формы...
        formData.append("title", this.formData.title || "");
        formData.append("description", this.formData.description || "");
        formData.append("pricePerDay", this.formData.isFree ? "0" : this.formData.pricePerDay || "");
        formData.append("phone", this.formData.phone || "");
        formData.append("maxPeriodDays", this.formData.maxPeriodDays);
        formData.append("viber", this.formData.viber || "");
        formData.append("city", this.formData.city || "");


        const response = await updateRequest(this.id, formData);
        if (response.status === 200) {
          this.togglePopUp();
          this.$router.push(`/requests/${this.id}`);
        }
      } catch (error) {
        console.error("Error updating item:", error);
      }
    }
    ,
  },
};
</script>
