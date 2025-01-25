<template>
  <div class="flex flex-col min-h-screen bg-st0">
    <Navigation v-if="navigation" :authorized="authStore.isAuthenticated" />
    <div class="flex-grow">  <RouterView />
    </div>
    <Footer v-if="navigation" />
  </div>
</template>

<script>
import { ref, watch } from "vue";
import { useRoute, RouterView, useRouter } from "vue-router";
import Navigation from "@/components/Navigation.vue";
import { useAuthStore } from "@/stores/auth.js";
import Footer from "@/views/Footer.vue";

export default {
  name: "App",
  components: {
    Footer,
    Navigation,
    RouterView,
  },
  setup() {
    const authStore = useAuthStore();
    const route = useRoute();
    const router = useRouter();

    const navigation = ref(true);

    watch(
        () => route.query.token,
        async (newToken) => {
          if (newToken) {
            await authStore.setToken(newToken);
            await router.push('/');
          }
        },
        { immediate: true }
    );

    watch(
        () => route.name,
        (newRoute) => {
          navigation.value = newRoute !== "LoginView"
              && newRoute !== "RegistrationView"
              && newRoute !== "PasswordForgot"
              && newRoute !== "PasswordReset";
        },
        { immediate: true }
    );

    return {
      authStore,
      navigation,
    };
  },
};
</script>

