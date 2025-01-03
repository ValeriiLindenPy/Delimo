<template>
  <!-- Desktop Header -->
  <header class="hidden md:block sticky top-0 bg-st5 z-10">
    <nav class="container flex flex-col sm:flex-row items-center gap-4 py-6">
      <!-- Logo -->
      <router-link to="/">
        <div class="flex-none items-center gap-2">
          <b class="text-3xl font-extrabold text-white">Del</b>
          <i class="fa-solid fa-arrows-spin text-3xl text-st3"></i>
          <b class="text-3xl font-extrabold text-white">imo</b>
        </div>
      </router-link>

      <!-- Search -->
      <DesctopSearch :go-items="goItems"/>

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
          <b class="text-3xl font-extrabold text-st5">Del</b>
          <i class="fa-solid fa-arrows-spin text-3xl text-st3" aria-hidden="true"></i>
          <b class="text-3xl font-extrabold text-st5">imo</b>
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
  <MobileSearch :go-items="goItems"/>

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
import DesctopSearch from "@/components/UI/DesctopSearch.vue";
import MobileSearch from "@/components/UI/MobileSearch.vue";

export default {
  name: "Navigation",
  components: {
    MobileSearch,
    DesctopSearch,
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

