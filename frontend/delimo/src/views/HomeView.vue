

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

  <PostList class="mt-3" :posts="items" />

  <div class="flex justify-center items-center mt-4">
    <hr class="flex-grow border-white">
    <p class="mx-4 text-white font-bold">Svi Zahtevi</p>
    <hr class="flex-grow border-white">
  </div>

  <RequestList :posts="requests"/>



</template>

<script>

import ItemList from "@/components/ItemList.vue";
import {useUserStore} from "@/stores/counter.js";
import apiClient from "@/services/api.js";
import RequestList from "@/components/RequestList.vue";




export default {
  components: {
    PostList: ItemList,
    RequestList: RequestList,
  },
  data() {
    return {
      items: null,
      requests: null,
      store: useUserStore()
    }
  },
  async mounted() {
    if (!this.store.authorized) {
      await apiClient.get("/users/user-data", {withCredentials: true})
          .then((res) => {
            this.store.authorized = true;
            this.store.setUserInfo(res.data);
            console.log(res.data);
          }).catch((err) => {
            this.store.authorized = false;
          })

    }
    console.log(this.store.authorized);
  }
}
</script>
