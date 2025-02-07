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
        <button @click="goItems" class="block w-full text-left py-2 px-4 hover:bg-st2 rounded-md">
          Moji stvari
        </button>
      </li>
      <li>
        <button @click="goRequests" class="block w-full text-left py-2 px-4 hover:bg-st2 rounded-md">
          Moji zahtevi
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
import {useAuthStore} from "@/stores/auth.js";

export default {
  name: "UserModal",
  data() {
    const store = useAuthStore();
    return {
      store,
    }
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
      this.$router.push("/users/" + this.store.profile.id);
      this.$emit("close");
    },
    goItems() {
      this.$router.push("/users/" + this.store.profile.id + "/items");
      this.$emit("close");
    },
    goRequests() {
      this.$router.push("/users/" + this.store.profile.id + "/requests");
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

