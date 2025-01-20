<template>
  <div class="flex justify-center items-center mt-4 mb-4">
    <hr class="flex-grow border-white">
    <p class="mx-4 text-white font-bold">Svi Zahtevi</p>
    <hr class="flex-grow border-white">
  </div>
  <RequestList :posts="posts" />
</template>

<script>

import RequestList from "@/components/RequestList.vue";
import {fetchRequests} from "@/services/requestService.js";



export default {
  name: 'RequestsView',
  components: {RequestList: RequestList},
  data() {
    return {
      posts: [],
    }
  },
  async created() {
    const res = await fetchRequests();
     this.posts = res.data.content;
  }
};
</script>

