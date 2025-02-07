<template>
  <div>
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

    <div class="flex justify-center items-center mt-4 mb-4">
      <hr class="flex-grow border-white">
      <p class="mx-4 text-white font-bold">Svi Zahtevi</p>
      <hr class="flex-grow border-white">
    </div>

    <RequestList :posts="posts" />

    <Pagination
        :currentPage="currentPage"
        :totalPages="totalPages"
        @page-changed="onPageChanged"
    />
  </div>
</template>

<script>
import RequestList from "@/components/RequestList.vue";
import Pagination from "@/components/Pagination.vue";
import { fetchRequests } from "@/services/requestService.js";
import { cities } from "@/assets/cities.js";

export default {
  name: "RequestsView",
  components: {
    RequestList,
    Pagination,
  },
  data() {
    return {
      city: "",
      cities,
      posts: [],
      currentPage: 0,
      totalPages: 0,
    };
  },
  async created() {
    await this.loadRequests();
  },
  methods: {
    async loadRequests() {
      try {
        const res = await fetchRequests(this.currentPage, 6, this.city);
        this.posts = res.data.content;
        this.totalPages = res.data.totalPages;
      } catch (error) {
        this.$router.push('/server-error');
        console.error("Error fetching requests:", error);
      }
    },
    async onPageChanged(page) {
      this.currentPage = page;
      await this.loadRequests();
    },
    async onCityChanged() {
      this.currentPage = 0;
      await this.loadRequests();
    },
  },
};
</script>