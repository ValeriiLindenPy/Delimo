<template>

  <div class="bg-white rounded-md shadow-sm p-3 text-2xl font-sans">
    <div class="flex gap-2 items-center mb-4">
      <i class="fa-regular fa-user text-st3 font-extrabold"></i>
      <p class="font-extrabold">Podešavanja naloga</p>
    </div>
    <p class="mb-2 border-b-2 border-solid pb-1">Ime: <b>{{ user.name }}</b></p>
    <p class="mb-2 border-b-2 border-solid pb-1">Email: <b>{{ user.email }}</b></p>
    <p v-if="user.telephone" class="mb-2 border-b-2 border-solid pb-1">Telefon: <b>{{ user.telephone }}</b></p>
    <p v-if="user.viber" class="mb-2 border-b-2 border-solid pb-1">Viber: <b>{{ user.viber }}</b></p>
    <p v-if="user.cityID" class="mb-2 border-b-2 border-solid pb-1">Grad: <b>{{ city }}</b></p>
    <p v-if="user.address" class="mb-2 border-b-2 border-solid pb-1">Adresa: <b>{{ user.address }}</b></p>

    <div class="flex justify-center">
      <button class="p-3 mt-5 bg-st4 text-white rounded-lg">
        Izmena ličnih podataka
      </button>
    </div>


  </div>

</template>

<script>
import {cities} from "@/assets/cities.js";

export default {
  name: 'UserDetailsUI',
  props: {
    user: {
      type: Object,
      required: true
    }
  },
  computed: {
    city() {
      const cityID = this.user.cityID;
      return cities.find(c => c.id === cityID)?.name;
    }
  }
}


</script>
