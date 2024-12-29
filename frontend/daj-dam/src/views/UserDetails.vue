<template>
  <div class="container">
    <div class="grid grid-cols-1 pt-6">
      <h1 class="text-white pt-3 text-2xl font-bold text-center">Moj Nalog</h1>
      <UserDetailsUI class="mt-4" :user="user" />
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
}
</script>

<style lang="scss" scoped>

</style>