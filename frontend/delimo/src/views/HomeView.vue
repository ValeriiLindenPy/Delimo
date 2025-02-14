<template>

  <div class="container">
    <div class="flex justify-center mt-3">
      <img class="object-cover rounded-lg w-[300px] h-[200px] md:w-[400px] md:h-[250px]"
           src="@/assets/img.webp"
           alt="home">
    </div>
  </div>

  <div class="flex justify-center items-center mt-4">
    <hr class="flex-grow border-white">
    <p class="mx-4 text-white font-bold">Stvari</p>
    <hr class="flex-grow border-white">
  </div>
  <Loader v-if="loadingItems" />
  <PostList v-else class="mt-3" :posts="items"/>

  <div class="flex justify-center items-center mt-4">
    <button @click="goItems" class="p-3 bg-st3 font-bold rounded-lg hover:bg-st4 hover:text-white">Pogledaj sve stvari po gradovima</button>
  </div>

  <div class="flex justify-center items-center mt-4">
    <hr class="flex-grow border-white">
    <p class="mx-4 text-white font-bold">Zahtevi</p>
    <hr class="flex-grow border-white">
  </div>
  <Loader class="mb-96" v-if="loadingRequests"/>
  <RequestList v-else :posts="requests"/>

  <div class="flex justify-center items-center mt-4">
    <button @click="goRequests" class="p-3 bg-st3 font-bold rounded-lg hover:bg-st4 hover:text-white">Pogledaj svi zahtevi po gradovima</button>
  </div>
</template>

<script>

import ItemList from "@/components/ItemList.vue";
import RequestList from "@/components/RequestList.vue";
import {getItems} from "@/services/itemService.js";
import {fetchRequests} from "@/services/requestService.js";
import Loader from "@/components/UI/Loader.vue";

export default {
  components: {
    Loader,
    PostList: ItemList,
    RequestList: RequestList,
  },
  data() {
    return {
      loadingItems: false,
      loadingRequests: false,
      items: [],
      requests: [],
    }
  },
  methods: {
    goItems() {
      this.$router.push("/items");
    },
    goRequests() {
      this.$router.push("/requests");
    },
    async fetchItems() {
      this.loadingItems = true;
      try {
        const res = await getItems();
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
        const res = await fetchRequests();
        this.requests = res.data.content;
      } catch (error) {
        console.error("Error fetching requests:", error);
      } finally {
        this.loadingRequests = false;
      }
    }
  },
  async mounted() {
    await this.fetchItems();
    await this.getRequests();
  }
}

</script>