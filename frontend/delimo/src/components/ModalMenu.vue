<template>
  <div>
    <div v-if="openMenu" class="flex items-center justify-center bg-white">
      <ul class="flex flex-col w-full">
        <li @click="goHome" class="hover:bg-st2 cursor-pointer px-2 py-3 border-st2 border-t-2 border-solid">Početna</li>
        <li @click="goItems" class="hover:bg-st2 cursor-pointer px-2 py-3 border-st2 border-t-2 border-solid">Sve Stvari</li>
        <li @click="goRequests" class="hover:bg-st2 cursor-pointer px-2 py-3 border-st2 border-t-2 border-solid">Svi Zahtevi</li>
        <li @click="goRules" class="hover:bg-st2 cursor-pointer px-2 py-3 border-st2 border-t-2 border-solid">Kako koristiti</li>
        <li @click="toggleOpenRequest" class="py-2 px-4 transition-colors duration-500 hover:bg-st4 cursor-pointer flex items-center gap-1 bg-st3 text-white shadow-inner">
          <i class="fa-solid fa-plus"></i>
          <p>Kreiraj oglas</p>
        </li>
        <li @click="toggleOpenSupport" class="hover:bg-st2 cursor-pointer px-2 py-3 border-st2 border-t-2 border-solid flex items-center gap-1">
          <i class="fa-solid fa-headset"></i>
          <p>Podrška</p>
        </li>
      </ul>
    </div>

    <PopUpModal @close="toggleOpenSupport" :is-active="isOpenSupport">
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
        <button @click="goFeedback" class="p-4 rounded-lg mt-4 bg-st4 text-white hover:bg-st3 hover:text-black transition-colors duration-500">
          Pomoć i kontakt
        </button>
      </div>
    </PopUpModal>

    <PopUpModal @close="toggleOpenRequest" :is-active="isOpenRequest">
      <div class="flex flex-col justify-center items-center">
        <h1 class="text-center font-bold text-2xl">Kreiraj oglas</h1>
        <div class="flex justify-center text-center pt-3 gap-2">
          <button class="py-3 px-8 bg-st4 font-bold rounded-lg text-white transition-colors duration-500 hover:bg-st3 hover:text-black">
            Dodaj stvar
          </button>
          <button class="py-3 px-8 bg-st4 font-bold rounded-lg text-white transition-colors duration-500 hover:bg-st3 hover:text-black">
            Kreiraj zahtev
          </button>
        </div>
      </div>
    </PopUpModal>
  </div>
</template>


<script>
import PopUpModal from "@/components/UI/PopUpModal.vue";

export default {
  name: 'ModalMenu',
  emits: ['close'],
  components: {PopUpModal},
  data() {
    return {
      isOpenSupport: false,
      isOpenRequest: false,
    }
  },
  props: {
    openMenu: {
      type: Boolean,
      required: true,
    }
  },
  methods: {
    toggleOpenSupport() {
      this.isOpenSupport = !this.isOpenSupport;
    },
    toggleOpenRequest() {
      this.isOpenRequest = !this.isOpenRequest;
    },
    goFeedback() {
      this.toggleOpenSupport()
      this.$emit('close')
      this.$router.push("/feedback");
    },
    goHome() {
      this.$emit('close')
      this.$router.push("/");
    },
    goRules() {
      this.$emit('close')
      this.$router.push("/rules");
    },
    goItems() {
      this.$emit('close')
      this.$router.push("/items");
    },
    goRequests() {
      this.$emit('close')
      this.$router.push("/requests");
    }
  },


}
</script>
