<template>
  <div class="container">
    <!-- Header Image -->
    <div class="flex justify-center mt-3">
      <img class="object-fit rounded-lg h-30 md:h-40" src="@/assets/img.png" alt="home" />
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

export default {
  name: "SearchView",
  components: {
    ItemList,
    Loader,
    RequestList
  },
  data() {
    return {
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
    console.log(this.text)
    this.fetchItems();
    this.getRequests();
  },
  watch: {
    // Следим за изменением query.text:
    '$route.query.text'(newValue) {
      this.text = newValue || "";
      this.fetchItems();
      this.getRequests();
    }
  },
  methods: {
    async fetchItems() {
      this.loadingItems = true;
      try {
        const res = await getItemsSearch(this.text);
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
        const res = await fetchRequestsSearch(this.text);
        this.requests = res.data.content;
        console.log(this.requests.length);
      } catch (error) {
        console.error("Error fetching requests:", error);
      } finally {
        this.loadingRequests = false;
      }
    },
  },
};
</script>



