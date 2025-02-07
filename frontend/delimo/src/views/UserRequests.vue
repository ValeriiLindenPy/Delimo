<template>
  <div class="md:container">
    <h1 class="text-white pt-3 text-center text-2xl font-bold">Moji Zahtevi</h1>

    <div class="mt-2">
      <Loader v-if="loading" />
      <template v-else>
        <RequestList v-if="requests && requests.length" :is-editable="true" :posts="requests" />
        <div v-else class="text-center mt-5 p-4 bg-gray-800 text-white rounded-lg">
          <p class="text-lg">Nemate još nijedan zahtev.</p>
          <p class="text-md mt-2">Dodajte prvi zahtev klikom na dugme "Kreiraj oglas".</p>
        </div>
      </template>
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
import Loader from "@/components/UI/Loader.vue";

export default {
  name: "UserRequests",
  components: {RequestList, Pagination, Loader},

  data() {
    return {
      requests: [],
      currentPage: 0,
      totalPages: 0,
      loading: false
    };
  },

  async created() {
    await this.loadRequests();
  },

  methods: {
    async loadRequests() {
      this.loading = true;
      try {
        const res = await fetchMyRequests(this.currentPage);
        this.requests = res.data.content;
        this.totalPages = res.data.totalPages;
      } catch (error) {
        console.error("Error fetching requests:", error);
        this.$router.push('/server-error');
      } finally {
        this.loading = false;
      }
    },
    async onPageChanged(page) {
      this.currentPage = page;
      await this.loadRequests();
    },
  },
};
</script>