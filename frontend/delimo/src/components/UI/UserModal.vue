<template>
  <div
      v-if="isModalOpen"
      class="absolute top-[65px] right-3 bg-white border border-gray-300
           rounded-lg shadow-lg p-4 w-64 z-[9999]"
  >
    <ul v-if="authorized">
      <li>
        <button @click="goProfile" class="block w-full text-left py-2 px-4 hover:bg-st2 rounded-md">
          Moj nalog
        </button>
      </li>
      <li>
        <button @click="goOglasi" class="block w-full text-left py-2 px-4 hover:bg-st2 rounded-md">
          Moji oglasi
        </button>
      </li>
      <li>
        <button @click="goLogout" class="block w-full text-left py-2 px-4 hover:bg-st2 rounded-md">
          Odjavite se
        </button>
      </li>
    </ul>
    <ul v-else>
      <li>
        <button @click="goLogin" class="block w-full text-left py-2 px-4 hover:bg-st2 rounded-md">
          Ulogujte se
        </button>
      </li>
      <li>
        <button @click="goRegistration" class="block w-full text-left py-2 px-4 hover:bg-st2 rounded-md">
          Registrujte se
        </button>
      </li>
    </ul>
  </div>
</template>

<script>
import {useUserStore} from "@/stores/counter.js";

export default {
  name: "UserModal",
  data() {
    return {
      useUserStore: false,
    }
  },
  created() {
    this.useUserStore = useUserStore();
  },
  props: {
    isModalOpen: {
      type: Boolean,
      required: true,
    },
    authorized: {
      type: Boolean,
      required: true,
    },
  },
  methods: {
    closeModal() {
      this.$emit("close");
    },
    goProfile() {
      this.$router.push("/users/" + this.useUserStore.userId);
      this.$emit("close");
    },
    goOglasi() {
      this.$router.push("/users/" + this.useUserStore.userId + "/oglasi");
      this.$emit("close");
    },
    goLogin() {
      this.$router.push("/login");
      this.$emit("close");
    },
    goRegistration() {
      this.$router.push("/registration");
      this.$emit("close");
    },
  goLogout() {
    window.location.href = "http://localhost:8080/logout";
  }
  },
};
</script>

<style scoped>
/* Adjust if needed, but make sure it stays higher than any other z-index. */
</style>
