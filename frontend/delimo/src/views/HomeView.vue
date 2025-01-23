<template>
  <div class="container">
    <div class="flex justify-center mt-3">
      <img class="object-fit rounded-lg h-30 md:h-40" src="@/assets/img.png" alt="home">
    </div>
  </div>

  <div class="flex justify-center items-center mt-4">
    <hr class="flex-grow border-white">
    <p class="mx-4 text-white font-bold">Svi Oglasi</p>
    <hr class="flex-grow border-white">
  </div>

  <PostList class="mt-3" :posts="items"/>

  <div class="flex justify-center items-center mt-4">
    <hr class="flex-grow border-white">
    <p class="mx-4 text-white font-bold">Svi Zahtevi</p>
    <hr class="flex-grow border-white">
  </div>

  <RequestList :posts="requests"/>


</template>

<script>

import ItemList from "@/components/ItemList.vue";
import RequestList from "@/components/RequestList.vue";
import {getItems} from "@/services/itemService.js";
import {fetchRequests} from "@/services/requestService.js";

export default {
  components: {
    PostList: ItemList,
    RequestList: RequestList,
  },
  data() {
    return {
      items: null,
      requests: null,
    }
  },
  methods: {
    async fetchItems() {
      const res = await getItems();
      this.items = res.data.content;
    },
    async getRequests() {
      const res = await fetchRequests();
      this.requests = res.data.content;
    }
  },
  async mounted() {
    await this.fetchItems();
    await this.getRequests();
  }
}

</script>
