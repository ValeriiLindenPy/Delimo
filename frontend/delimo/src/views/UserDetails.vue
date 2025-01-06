<template>
  <div class="container mx-auto">
    <h1 class="text-white pt-6 text-2xl font-bold text-center">Moj Nalog</h1>
    <div class="flex pt-6 gap-3 items-start justify-center">
      <UserDetailsUI v-if="user" class="grow mt-2" :user="user" />

      <div class="hidden bg-white rounded-lg mt-2 flex-col w-1/3 md:flex">
        <div @click="goOglasi" class="flex p-5 hover:bg-st4 cursor-pointer hover:text-white rounded-t-lg items-center">
          <div class="flex gap-2 items-center mb-4">
            <i class="fa-solid fa-folder text-st3 font-extrabold"></i>
            <p class="font-extrabold">Moji Oglasi</p>
          </div>
        </div>

        <div class="flex p-5 hover:bg-st4 cursor-pointer hover:text-white rounded-b-lg items-center">
          <div class="flex gap-2 items-center mb-4">
            <i class="fa-solid fa-brush text-st3 font-extrabold"></i>
            <p class="font-extrabold">Moje Objave</p>
          </div>
        </div>
      </div>


    </div>
  </div>
</template>

<script>
import UserDetailsUI from "@/components/UI/UserDetailsUI.vue";
import {useUserStore} from "@/stores/counter.js";


export default {
  name: "UserDetails",
  components: {UserDetailsUI},
  data() {
    return {
      user: null,
    }
  },

  computed: {
    userProfile() {
      return useUserStore().profile;
    }
  },
  mounted() {
    if (this.userProfile) {
      this.user = this.userProfile
    }
  },
  methods: {
    goOglasi() {
      const userId = Number(this.$route.params.id);
      this.$router.push("/users/" + userId + "/oglasi");
    },
  }
}
</script>

