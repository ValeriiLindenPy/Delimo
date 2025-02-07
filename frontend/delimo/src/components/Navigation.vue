<template>
  <!-- Desktop Header -->
  <header class="hidden md:block sticky top-0 bg-st5 z-10">
    <div class="container flex flex-col sm:flex-row items-center gap-4 py-6">
      <!-- Logo -->
      <router-link to="/">
        <div class="flex-none items-center gap-2">
          <b class="text-3xl font-extrabold text-white">Del</b>
          <i class="fa-solid fa-arrows-spin text-3xl text-st3"></i>
          <b class="text-3xl font-extrabold text-white">imo</b>
        </div>
      </router-link>

      <!-- Search -->
      <DesktopSearch />

      <!-- Authorization Items -->
      <AuthorizationItems :authorized="authorized" />
    </div>

    <!-- DesktopBar (included in sticky header) -->
    <DesktopBar class="bg-st5 border-t border-st4" />
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
      <ModalMenu v-if="menuOpen" :open-menu="menuOpen" @close="close" />
    </transition>
  </header>

  <!-- Mobile Search (sticky) -->
  <MobileSearch />

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
import MobileSearch from "@/components/UI/MobileSearch.vue";
import DesktopBar from "@/components/DesktopBar.vue";
import DesktopSearch from "@/components/UI/DesktopSearch.vue";

export default {
  name: "Navigation",
  components: {
    DesktopBar,
    MobileSearch,
    DesktopSearch,
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
      if (this.menuOpen) {
        document.body.classList.add("overflow-hidden");
      } else {
        document.body.classList.remove("overflow-hidden");
      }
    },
    toggleModal() {
      this.isModalOpen = !this.isModalOpen;
    },
    close() {
      this.menuOpen = false;
      document.body.classList.remove("overflow-hidden");
    },
  },
};
</script>
