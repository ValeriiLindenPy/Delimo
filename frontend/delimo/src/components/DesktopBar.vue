<template>
  <div class="container bg-st2 hidden md:block">
    <nav class="flex">
      <ul class="flex items-center justify-center text-center font-bold w-full gap-7">
        <li @click="goHome" class="p-2 cursor-pointer hover:bg-st4 hover:text-white">Početna</li>
        <li @click="goItems" class="p-2 cursor-pointer hover:bg-st4 hover:text-white">Sve Stvari</li>
        <li @click="goRequests" class="p-2 cursor-pointer hover:bg-st4 hover:text-white">Svi Zahtevi</li>
        <li class="p-2 cursor-pointer hover:bg-st4 hover:text-white">Kako koristiti</li>
        <li class="p-2 cursor-pointer hover:bg-st4 hover:text-white">O nama</li>
        <li @click="toggleModal('support')" class="p-2 cursor-pointer hover:bg-st4 hover:text-white flex items-center gap-1">
          <i class="fa-solid fa-headset"></i>
          <p>Podršка</p>
        </li>
        <li @click="toggleModal('request')" class="py-2 px-4 ml-3 transition-colors duration-500 hover:bg-st4 cursor-pointer flex items-center gap-1 bg-st3 text-white shadow-inner">
          <i class="fa-solid fa-plus"></i>
          <p>Kreiraj oglas</p>
        </li>
      </ul>
    </nav>
  </div>

  <!-- Модальное окно "Поддержка" -->
  <PopUpModal v-if="activeModal === 'support'" @close="toggleModal(null)" :is-active="true">
    <div class="flex flex-col justify-center">
      <h1 class="text-center font-bold text-2xl mb-2">Podrška</h1>
      <ul class="flex-col">
        <li class="flex gap-2 items-center mb-2">
          <i class="fa-regular fa-paper-plane text-st3"></i>
          <span>@delimoservice</span>
        </li>
        <li class="flex gap-2 items-center mb-2">
          <i class="fa-solid fa-at text-st3"></i>
          <span>delimoservice@gmail.com</span>
        </li>
        <li class="flex gap-2 items-center mb-2">
          <i class="fa-solid fa-phone text-st3"></i>
          <span>+381621110623</span>
        </li>
      </ul>
      <button @click="goFeedback" class="p-4 rounded-lg mt-4 bg-st4 text-white hover:bg-st3 hover:text-black transition-colors duration-500">Pomoć i kontakt</button>
    </div>
  </PopUpModal>

  <!-- Модальное окно "Создать объявление" -->
  <PopUpModal v-if="activeModal === 'request'" @close="toggleModal(null)" :is-active="true">
    <div class="flex flex-col justify-center items-center"></div>
    <h1 class="text-center font-bold text-2xl">Kreiraj oglas</h1>
    <div class="flex justify-center text-center pt-3 gap-2">
      <button @click="goAddItem" class="py-3 px-8 bg-st4 font-bold rounded-lg text-white transition-colors duration-500 hover:bg-st3 hover:text-black">Dodaj stvar</button>
      <button @click="goAddRequest" class="py-3 px-8 bg-st4 font-bold rounded-lg text-white transition-colors duration-500 hover:bg-st3 hover:text-black">Kreiraj zahtev</button>
    </div>
  </PopUpModal>
</template>


<script>
import PopUpModal from "@/components/UI/PopUpModal.vue";
import {useUserStore} from "@/stores/counter.js";

export default {
  name: "DesktopBar",
  data() {
    return {
      activeModal: null,
      store: useUserStore()
    };
  },
  methods: {
    toggleModal(modalName) {
      this.activeModal = this.activeModal === modalName ? null : modalName;
    },
    goAddItem() {
      this.toggleModal('request')
      this.$router.push("/items/adding-item");
    },
    goAddRequest() {
      this.toggleModal('request')
      this.$router.push("/items/adding-request");
    },
    goHome() {
      this.$router.push("/");
    },
    goFeedback() {
      this.toggleModal('support')
      this.$router.push("/feedback");
    },
    goItems() {
      this.$router.push("/items");
    },
    goRequests() {
      this.$router.push("/requests");
    }
  },
  components: { PopUpModal },
};
</script>
