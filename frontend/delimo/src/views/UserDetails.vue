<template>
  <div class="md:container mx-auto">
    <h1 class="text-white pt-6 text-2xl font-bold text-center">Moj Nalog</h1>
    <div class="flex pt-6 gap-3 items-start justify-center">
      <UserDetailsUI v-if="user" class="grow mt-2" :user="user" />

      <div class="hidden bg-white rounded-lg mt-2 flex-col w-1/3 md:flex">
        <div @click="goItems" class="flex p-5 border-b-2 border-solid hover:bg-st4 cursor-pointer hover:text-white rounded-t-lg items-center">
          <div class="flex gap-2 items-center mb-4">
            <i class="fa-solid fa-folder text-st3 font-extrabold"></i>
            <p class="font-extrabold">Moje stvari</p>
          </div>
        </div>

        <div @click="goRequests" class="flex p-5 hover:bg-st4 cursor-pointer hover:text-white rounded-b-lg items-center">
          <div class="flex gap-2 items-center mb-4">
            <i class="fa-solid fa-brush text-st3 font-extrabold"></i>
            <p class="font-extrabold">Moji zahtevi</p>
          </div>
        </div>
      </div>


    </div>
  </div>
</template>

<script>
import UserDetailsUI from "@/components/UI/UserDetailsUI.vue";
import {useAuthStore} from "@/stores/auth.js";


export default {
  name: "UserDetails",
  components: {UserDetailsUI},
  data() {
    const store = useAuthStore();
    return {
      store,
      user: null,
    }
  },

  mounted() {
    if (this.store.profile) {
      this.user = this.store.profile
    }
  },
  methods: {
    goItems() {
      const userId = Number(this.$route.params.id);
      this.$router.push("/users/" + userId + "/items");
    },
    goRequests() {
      const userId = Number(this.$route.params.id);
      this.$router.push("/users/" + userId + "/requests");
    },
  }
}
</script>

