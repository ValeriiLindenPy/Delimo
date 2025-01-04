<template>
  <div class="flex flex-col min-h-screen bg-st0">
    <Navigation v-if="navigation" :authorized="userStore.authorized" />
    <RouterView class="flex-grow" />
  </div>
</template>

<script>
import { ref, watch, onMounted } from "vue";
import { useRoute, RouterView } from "vue-router";
import Navigation from "@/components/Navigation.vue";
import { useUserStore } from "@/stores/counter.js";

export default {
  name: "App",
  components: {
    Navigation,
    RouterView,
  },
  setup() {
    const userStore = useUserStore();
    const route = useRoute();
    const navigation = ref(true);

    watch(
        () => route.name,
        (newRoute) => {
          navigation.value = newRoute !== "LoginView" && newRoute !== "RegistrationView";
        },
        { immediate: true }
    );

    return {
      userStore,
      navigation,
    };
  },
};
</script>

<style scoped>
/* your styles here */
</style>
