<template>
  <div class="flex flex-col min-h-screen bg-st0">
    <Navigation v-if="navigation" :authorized="userStore.authorized" />
    <RouterView class="flex-grow" />
  </div>
</template>

<script>
import { RouterView } from "vue-router";
import Navigation from "@/components/Navigation.vue";
import { useUserStore } from "@/stores/counter.js";
import { ref, watch } from "vue";
import { useRoute } from "vue-router";

export default {
  components: {
    Navigation,
    RouterView,
  },
  setup() {
    const userStore = useUserStore();
    const navigation = ref(true);
    const route = useRoute();

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
