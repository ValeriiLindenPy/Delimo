<template>
  <div class="container">

    <h1 class="text-white pt-3 text-center text-2xl font-bold">Moji Zahtevi</h1>

    <div class="mt-2">
      <RequestList :is-editable="true" :posts="requests" />
      <Pagination
          :currentPage="currentPage"
          :totalPages="totalPages"
          @page-changed="onPageChanged"
      />
    </div>
  </div>
</template>

<script>
import RequestList from "@/components/RequestList.vue";
import {fetchMyRequests} from "@/services/requestService.js";
import Pagination from "@/components/Pagination.vue";

export default {
  name: "UserRequests",
  components: { RequestList: RequestList, Pagination: Pagination },

  data() {
    return {
      requests: [],
      currentPage: 0,
      totalPages: 0
    }
  },

  async created() {
    await this.loadRequests();
  },
  methods: {
    async loadRequests() {
      try {
        const res = await fetchMyRequests(this.currentPage);
        this.requests = res.data.content;
        this.totalPages = res.data.totalPages;
      } catch (error) {
        console.error("Error fetching requests:", error);
      }
    },
    async onPageChanged(page) {
      this.currentPage = page; // Update the current page
      await this.loadRequests(); // Reload requests for the new page
    },
  },
}
</script>
