<template>
  <div class="flex flex-col min-h-screen font-Inter bg-st0">
    <Navigation :authorized="userStore.authorized" />
    <RouterView/>
  </div>
</template>

<script>
import {RouterView} from "vue-router";
import Navigation from "@/components/Navigation.vue";
import {useUserStore} from "@/stores/counter.js";
export default {
  components: {
    Navigation,
    RouterView,
    useUserStore,
  },

  data() {
    return {
      userStore: useUserStore(),
    }
  }

}
</script>

<style scoped>

</style>
