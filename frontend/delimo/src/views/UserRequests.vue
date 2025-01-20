<template>
  <div class="container">

    <h1 class="text-white pt-3 text-center text-2xl font-bold">Moji Zahtevi</h1>

    <div class="mt-2">
      <RequestList :is-editable="true" :posts="requests" />
    </div>
  </div>
</template>

<script>
import RequestList from "@/components/RequestList.vue";
import {fetchMyRequests} from "@/services/requestService.js";

export default {
  name: "UserRequests",
  components: { RequestList: RequestList },

  data() {
    return {
      requests: [],
    }
  },

  async created() {
    // Make sure to convert route param to a number if needed
    // e.g. const ownerId = Number(this.$route.params.id);
    // Then filter by that:
    const ownerId = Number(this.$route.params.id);
    const res = await fetchMyRequests();
    this.requests = res.data.content;

    if (!this.requests.length) {
      console.warn('No posts found for this owner id -' + ownerId);
    }
  },
}
</script>
