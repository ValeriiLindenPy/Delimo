<template>
  <div class="container">
    <!-- Header Image -->
    <div class="flex justify-center mt-3">
      <img class="object-fit rounded-lg h-30 md:h-40" src="@/assets/img.png" alt="home" />
    </div>

    <!-- City Filter -->
    <div class="flex justify-end p-4">
      <div>
        <label for="citySelect" class="text-white font-bold">Mesto:</label>
        <select
            id="citySelect"
            v-model="city"
            @change="onCityChanged"
            class="ml-2 bg-st3 text-white font-bold text-xl rounded-lg p-2"
        >
          <option value="">Svi gradovi</option>
          <option class="hover:bg-st4" v-for="cityItem in cities" :key="cityItem.id" :value="cityItem.name">
            {{ cityItem.name }}
          </option>
        </select>
      </div>
    </div>

    <!-- Items Section -->
    <div class="flex justify-center items-center mt-4">
      <hr class="flex-grow border-white" />
      <p class="mx-4 text-white font-bold">Svi Oglasi</p>
      <hr class="flex-grow border-white" />
    </div>
    <Loader v-if="loadingItems" />
    <div v-else>
      <p v-if="items?.length === 0" class="text-center">Nema oglasa za "{{ text }}".</p>
      <ItemList v-else class="mt-3" :posts="items" />
    </div>

    <!-- Requests Section -->
    <div class="flex justify-center items-center mt-4">
      <hr class="flex-grow border-white" />
      <p class="mx-4 text-white font-bold">Svi Zahtevi</p>
      <hr class="flex-grow border-white" />
    </div>
    <Loader class="mb-96" v-if="loadingRequests" />
    <div v-else>
      <p v-if="requests?.length === 0" class="text-center">Nema zahteva za "{{ text }}".</p>
      <RequestList v-else class="mt-3" :posts="requests" />
    </div>
  </div>
</template>

<script>
import { useRoute } from "vue-router";
import { getItemsSearch } from "@/services/itemService.js";
import { fetchRequestsSearch } from "@/services/requestService.js";
import ItemList from "@/components/ItemList.vue";
import Loader from "@/components/UI/Loader.vue";
import RequestList from "@/components/RequestList.vue";
import { cities } from "@/assets/cities.js";

export default {
  name: "SearchView",
  components: {
    ItemList,
    Loader,
    RequestList,
  },
  data() {
    return {
      cities,
      city: "",
      text: null,
      loadingItems: false,
      items: null,
      loadingRequests: false,
      requests: null,
    };
  },
  mounted() {
    const route = useRoute();
    this.text = route.query.text || "";
    this.fetchItems();
    this.getRequests();
  },
  watch: {
    '$route.query.text'(newValue) {
      this.text = newValue || "";
      this.fetchItems();
      this.getRequests();
    },
    city() {
      this.fetchItems();
      this.getRequests();
    }
  },
  methods: {
    async fetchItems() {
      this.loadingItems = true;
      try {
        const res = await getItemsSearch(this.text, 0, 6, this.city);
        this.items = res.data.content;
      } catch (error) {
        console.error("Error fetching items:", error);
      } finally {
        this.loadingItems = false;
      }
    },
    async getRequests() {
      this.loadingRequests = true;
      try {
        const res = await fetchRequestsSearch(this.text, 0, 6, this.city);
        this.requests = res.data.content;
      } catch (error) {
        console.error("Error fetching requests:", error);
      } finally {
        this.loadingRequests = false;
      }
    },
    async onCityChanged() {
      await this.fetchItems();
      await this.getRequests();
    },
  },
};
</script>