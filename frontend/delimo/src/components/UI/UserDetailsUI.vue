<template>
  <PopUpModal :is-active="isPopUp" @close="togglePopUp">
    <div class="flex flex-col items-center justify-center gap-2">
      <h1>Obriši nalog?</h1>
      <div class="flex gap-4">
        <button
            class="bg-st3 text-white font-medium py-2 px-4 rounded-md hover:bg-st4 transition"
            @click="confirmDelete"
        >
          Da
        </button>
        <button
            class="bg-st3 text-white font-medium py-2 px-4 rounded-md hover:bg-st4 transition"
            @click="togglePopUp"
        >
          Ne
        </button>
      </div>
    </div>
  </PopUpModal>

  <div class="bg-white md:rounded-md shadow-sm p-6 text-xl font-sans">
    <div class="flex justify-between items-center mb-5">
      <div class="flex gap-2">
        <i class="fa-regular fa-user text-st3 font-extrabold"></i>
        <p class="font-extrabold">Podešavanja naloga</p>
      </div>

      <p class="text-base text-red-500 font-light cursor-pointer" @click="togglePopUp">
        Obrisati nalog
      </p>
    </div>

    <p class="mb-2 border-b-2 border-solid pb-1">Ime: <b>{{ user.name }}</b></p>
    <p class="mb-2 border-b-2 border-solid pb-1">Email: <b>{{ user.email }}</b></p>
    <p v-if="user.phone" class="mb-2 border-b-2 border-solid pb-1">Telefon: <b>{{ user.phone }}</b></p>
    <p v-if="user.viber" class="mb-2 border-b-2 border-solid pb-1">Viber: <b>{{ user.viber }}</b></p>
    <p v-if="user.city" class="mb-2 border-b-2 border-solid pb-1">Grad: <b>{{ user.city }}</b></p>
    <p v-if="user.street" class="mb-2 border-b-2 border-solid pb-1">Ulica: <b>{{ user.street }}</b></p>

    <div class="flex justify-center gap-3">
      <button @click="goEdit" class="p-3 mt-5 bg-st4 hover:bg-st3 transition-colors duration-500 text-white rounded-lg">
        Izmena ličnih podataka
      </button>

      <button @click="goReset" class="p-2 mt-5 bg-red-500 hover:bg-black transition-colors duration-500 text-white rounded-lg">
        Promeni lozinku
      </button>
    </div>
  </div>
</template>

<script>
import { useAuthStore } from "@/stores/auth.js";
import PopUpModal from "@/components/UI/PopUpModal.vue";
import {deleteUser} from "@/services/userService.js";

export default {
  name: 'UserDetailsUI',
  components: { PopUpModal },
  data() {
    const store = useAuthStore();
    return {
      store,
      isPopUp: false,
    };
  },
  props: {
    user: {
      type: Object,
      required: true
    }
  },
  methods: {
    async goReset() {
      await this.$router.push("/reset-password");
    },
    async goEdit() {
      await this.$router.push("/users/" + this.store.profile.id + "/edit");
    },
    togglePopUp() {
      this.isPopUp = !this.isPopUp;
    },
    async confirmDelete() {
      try {
        await deleteUser(this.user.id);
        await this.store.logout();
        await this.$router.push("/login");
      } catch (error) {
        console.error("Ошибка при удалении пользователя:", error);
      }
    }
  }
};
</script>
