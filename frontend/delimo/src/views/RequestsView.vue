<template>
  <div>
    <div class="flex justify-center items-center mt-4 mb-4">
      <hr class="flex-grow border-white">
      <p class="mx-4 text-white font-bold">Svi Zahtevi</p>
      <hr class="flex-grow border-white">
    </div>

    <!-- Request List -->
    <RequestList :posts="posts" />

    <!-- Pagination -->
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
import {fetchRequests} from "@/services/requestService.js";

export default {
  name: "RequestsView",
  components: {
    RequestList,
    Pagination,
  },
  data() {
    return {
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
        const res = await fetchRequests(this.currentPage); // Pass the current page
        this.posts = res.data.content; // Extract posts
        this.totalPages = res.data.totalPages; // Extract total pages
      } catch (error) {
        console.error("Error fetching requests:", error);
      }
    },
    async onPageChanged(page) {
      this.currentPage = page; // Update the current page
      await this.loadRequests(); // Reload requests for the new page
    },
  },
};
</script>
