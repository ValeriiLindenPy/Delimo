<template>
  <!-- Desktop Header -->
  <header class="hidden md:block sticky top-0 bg-st5">
    <nav class="container flex flex-col sm:flex-row items-center gap-4 py-6">

      <div class="flex-none items-center gap-2">
        <b class="text-3xl font-extrabold text-white">Daj</b>
        <i class="fa-solid fa-arrows-spin text-3xl text-st3"></i>
        <b class="text-3xl font-extrabold text-white">Dam</b>
      </div>

      <div class="grow bg-white rounded-lg h-10">
        <div class="flex h-full">
          <input placeholder="Traži ..." class="p-2 grow rounded-lg h-full">
          <button class="w-10 bg-st3 rounded-r-lg hover:text-st3 hover:bg-st5 transition-all duration-300">
            <i class="fa-solid fa-magnifying-glass"></i>
          </button>
        </div>
      </div>

      <AuthorizationItems :authorized="authorized" />

    </nav>
  </header>

  <!-- Mobile Header -->
  <header class="block md:hidden sticky top-0 bg-st1">
    <nav class="container mx-auto flex justify-between items-center py-4" aria-label="Mobile Navigation">
      <!-- Menu Icon -->
      <div class="flex">
        <i
            :class="menuOpen ? 'fa-solid fa-xmark' : 'fa-solid fa-bars'"
            class="text-3xl cursor-pointer"
            aria-hidden="true"
            @click="toggleMenu">
        </i>
      </div>

      <!-- Logo -->
      <div class="flex gap-2">
        <b class="text-3xl font-extrabold text-st5">Daj</b>
        <i class="fa-solid fa-arrows-spin text-3xl text-st3" aria-hidden="true"></i>
        <b class="text-3xl font-extrabold text-st5">Dam</b>
      </div>

      <!-- User Icon -->
      <button class="flex" @click="toggleModal">
        <i class="fa-solid fa-circle-user text-3xl text-st3" aria-hidden="true"></i>
      </button>
    </nav>
    <UserModal :authorized="authorized" :isModalOpen="isModalOpen" @close="isModalOpen = false" />
  </header>

</template>

<script>
import UserModal from "./UI/UserModal.vue";
import AuthorizationItems from "@/components/AuthorizationItems.vue";

export default {
  components: {
    AuthorizationItems,
    UserModal,
  },
  data() {
    return {
      menuOpen: false,
      isModalOpen: false,
      authorized: true,
    };
  },
  methods: {
    toggleMenu() {
      this.menuOpen = !this.menuOpen;
    },
    toggleModal() {
      this.isModalOpen = !this.isModalOpen;
    },
  },
};
</script>
