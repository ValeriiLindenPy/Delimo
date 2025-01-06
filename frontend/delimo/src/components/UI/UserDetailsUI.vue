<template>

  <div class="bg-white rounded-md shadow-sm p-3 text-xl font-sans">
    <div class="flex gap-2 items-center mb-4">
      <i class="fa-regular fa-user text-st3 font-extrabold"></i>
      <p class="font-extrabold">Podešavanja naloga</p>
    </div>
    <p class="mb-2 border-b-2 border-solid pb-1">Ime: <b>{{ user.name }}</b></p>
    <p class="mb-2 border-b-2 border-solid pb-1">Email: <b>{{ user.email }}</b></p>
    <p v-if="user.phone" class="mb-2 border-b-2 border-solid pb-1">Telefon: <b>{{ user.phone }}</b></p>
    <p v-if="user.viber" class="mb-2 border-b-2 border-solid pb-1">Viber: <b>{{ user.viber }}</b></p>
    <p v-if="user.grad" class="mb-2 border-b-2 border-solid pb-1">Grad: <b>{{ user.grad }}</b></p>
    <p v-if="user.street" class="mb-2 border-b-2 border-solid pb-1">Ulica: <b>{{ user.street }}</b></p>

    <div class="flex justify-center">
      <button @click="goEdit" class="p-3 mt-5 bg-st4 text-white rounded-lg">
        Izmena ličnih podataka
      </button>
    </div>


  </div>

</template>

<script>

import {useUserStore} from "@/stores/counter.js";

export default {
  name: 'UserDetailsUI',
  props: {
    user: {
      type: Object,
      required: true
    }
  },
  methods: {
     async goEdit() {
      await this.$router.push("/users/" + useUserStore().userId + "/edit");
    }
  },
}


</script>
