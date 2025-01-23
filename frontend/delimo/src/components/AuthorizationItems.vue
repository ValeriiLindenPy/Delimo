<template>
  <div v-if="authorized" class="flex-1 h-10 flex items-center justify-center gap-4 lg:text-xl md:text-sm">
    <button @click="goUser"
        class="hover:bg-st3 hover:text-st5 flex items-center justify-center border-solid h-full p-5 text-white rounded-lg border-st3 border-2 transition-all duration-300">
      Moj nalog
    </button>
    <button @click="goLogout"
        class="hover:bg-st3 hover:text-st5 flex items-center justify-center border-solid h-full p-5 text-white rounded-lg border-st3 border-2 transition-all duration-300">
      Odjavite se
    </button>
  </div>

  <div v-else class="flex-1 h-10 flex items-center justify-center gap-4 lg:text-xl md:text-sm">
    <button @click="goLogin"
        class="hover:bg-st3 hover:text-st5 flex items-center justify-center border-solid h-full p-5 text-white rounded-lg border-st3 border-2 transition-all duration-300">
      Ulogujte se
    </button>
    <button
        @click="goRegistration"
        class="hover:bg-st3 hover:text-st5 flex items-center justify-center border-solid h-full p-5 text-white rounded-lg border-st3 border-2 transition-all duration-300">
      Registrujte se
    </button>
  </div>
</template>
<script>
import {useAuthStore} from "@/stores/auth.js";

export default {
  name: 'AuthorizationItems',
  data() {
    const store = useAuthStore();
    return {store}
  },
  props: {
    authorized: {
      type: Boolean,
      required: true,
    },
  },
  methods: {
    goUser() {
      const userId = Number(this.store.profile.id);
      this.$router.push("/users/"+ userId);
    },
    goLogin() {
      this.$router.push("/login");
    },
    goRegistration() {
      this.$router.push("/registration");
    },
    goLogout() {
      // window.location.href = "http://localhost:8080/logout"; //todo
      this.store.logout();
      this.$router.push("/");
    }
  }
}
</script>