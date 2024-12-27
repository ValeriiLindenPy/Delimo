<template>
  <!-- Desktop Header -->
  <header class="hidden md:block sticky top-0 bg-st5 z-10">
    <nav class="container flex flex-col sm:flex-row items-center gap-4 py-6">
      <!-- Logo -->
      <router-link to="/">
        <div class="flex-none items-center gap-2">
          <b class="text-3xl font-extrabold text-white">Daj</b>
          <i class="fa-solid fa-arrows-spin text-3xl text-st3"></i>
          <b class="text-3xl font-extrabold text-white">Dam</b>
        </div>
      </router-link>

      <!-- Search -->
      <div class="grow bg-white rounded-lg h-10">
        <div class="flex h-full">
          <input
              placeholder="Traži ..."
              class="p-2 grow rounded-l-lg h-full"
          />
          <button
              @click="goItems"
              class="w-10 bg-st3 rounded-r-lg hover:text-st3 hover:bg-st5 transition-all duration-300"
          >
            <i class="fa-solid fa-magnifying-glass"></i>
          </button>
        </div>
      </div>

      <!-- Authorization Items -->
      <AuthorizationItems :authorized="authorized" />
    </nav>
  </header>

  <!-- Mobile Header -->
  <header class="block md:hidden sticky top-0 bg-st1 z-10">
    <nav class="container mx-auto flex justify-between items-center py-4" aria-label="Mobile Navigation">
      <!-- Menu Icon -->
      <div class="flex">
        <i
            :class="menuOpen ? 'fa-solid fa-xmark' : 'fa-solid fa-bars'"
            class="text-3xl cursor-pointer"
            aria-hidden="true"
            @click="toggleMenu"
        ></i>
      </div>

      <!-- Logo -->
      <router-link to="/">
        <div class="flex gap-2">
          <b class="text-3xl font-extrabold text-st5">Daj</b>
          <i class="fa-solid fa-arrows-spin text-3xl text-st3" aria-hidden="true"></i>
          <b class="text-3xl font-extrabold text-st5">Dam</b>
        </div>
      </router-link>

      <!-- User Icon -->
      <button class="flex" @click="toggleModal">
        <i class="fa-solid fa-circle-user text-3xl text-st3 hover:text-st4" aria-hidden="true"></i>
      </button>
    </nav>

    <!-- Slide-out mobile menu -->
    <transition>
      <ModalMenu v-if="menuOpen" :open-menu="menuOpen" />
    </transition>
  </header>

  <!-- Mobile Search (sticky) -->
  <div class="block md:hidden sticky top-[68px] w-full py-2 bg-st2 z-20">
    <div class="mx-3 rounded-lg bg-white h-10">
      <div class="flex h-full">
        <input
            placeholder="Traži ..."
            class="p-2 grow h-full rounded-l-lg"
        />
        <button
            @click="goItems"
            class="w-1/6 bg-st3 rounded-r-lg hover:text-st3 hover:bg-st5 transition-all duration-300"
        >
          <i class="fa-solid fa-magnifying-glass"></i>
        </button>
      </div>
    </div>
  </div>

  <!-- User Modal (moved outside the header, higher z-index) -->
  <transition>
    <UserModal
        v-if="isModalOpen"
        :authorized="authorized"
        :isModalOpen="isModalOpen"
        class="z-[9999]"
        @close="isModalOpen = false"
    />
  </transition>
</template>

<script>
import UserModal from "@/components/UI/UserModal.vue";
import ModalMenu from "@/components/ModalMenu.vue";
import AuthorizationItems from "@/components/AuthorizationItems.vue";

export default {
  name: "Navigation",
  components: {
    ModalMenu,
    AuthorizationItems,
    UserModal,
  },
  props: {
    authorized: {
      type: Boolean,
      required: true,
    },
  },
  data() {
    return {
      menuOpen: false,
      isModalOpen: false,
    };
  },
  methods: {
    toggleMenu() {
      this.menuOpen = !this.menuOpen;
    },
    toggleModal() {
      this.isModalOpen = !this.isModalOpen;
    },
    goItems() {
      this.$router.push("/items");
    },
  },
};
</script>

<style scoped>
/* You can further adjust your z-index values if needed. */
</style>
