<template>
  <div class="container mx-auto">
    <h1 class="text-white pt-6 text-2xl font-bold text-center">Moj Nalog</h1>
    <div class="flex pt-6 gap-3 items-start justify-center">
      <UserDetailsUI class="grow mt-2" :user="user" />

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
import {users} from "@/assets/users.js";
import UserDetailsUI from "@/components/UI/UserDetailsUI.vue";


export default {
  name: "UserDetails",
  components: {UserDetailsUI},
  data() {
    return {
      user: null,
    }
  },

  created() {
    const userId = Number(this.$route.params.id);
    this.user = users.find(user => user.id === userId);

    if (this.user === null) {
      console.warn('No users found with id -' + userId);
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

