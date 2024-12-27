<template>
  <div class="container">
    <div class="grid grid-cols-1 pt-6 md:grid-cols-2">
      <div>
        <div class="images-carousel mb-2">
          <Carousel :images="image"/>
        </div>
        <div class="flex p-2 flex-col text-center items-center justify-center pt-3 bg-st2 rounded-lg">
          <button class="p-2 mb-2 bg-green-400 rounded-lg">Telefone {{maskedTelephone}} Prikazi</button>
          <button class="p-2 bg-viber rounded-lg text-white">Viber {{maskedViber}} Prikazi</button>
        </div>
      </div>

      <div>
        <div class="card mt-2 mb-2 bg-white p-4 rounded-lg">
          <h1 class="text-xl font-bold mb-4">{{ name }}</h1>
          <p class="mb-2">{{ description }}</p>

        </div>

        <div v-if="available" class="flex items-center justify-center mb-2 bg-white p-4 rounded-lg mb-2">
            <i class="fa-solid fa-hand text-green-500"></i>
          <span class="ml-1 font-bold">Dostupno</span>
          <p class="text-sm text-gray-500 pl-2">
            (To znači da je ova stvar trenutno dostupna za pozajmljivanje)
          </p>
          </div>

        <div v-else class="flex items-center justify-center mb-2 bg-white p-4 rounded-lg mb-2">
          <i class="fa-solid fa-hand text-red-500"></i>
          <span class="ml-1 font-bold">Rezervacija</span>
          <p class="text-sm text-gray-500 pl-2">
            (To znači da je ova stvar trenutno u upotrebi kod drugog korisnika ili da je vlasnik trenutno ne izdaje. Pokušajte kasnije)
          </p>
        </div>

        <div class="flex items-center justify-center mb-2 bg-white p-4 rounded-lg">
            <span
                class="bg-st2 w-[140px] font-bold text-center p-3 text-st3 text-[12px] rounded md:text-[12px]"
            >
          {{ maxPeriodDays }} dan(a)
        </span>
          <p class="text-sm text-gray-500 pt-2 pl-2">
            (Ovo je maksimalni period na koji vam vlasnik može pozajmiti ovu stvar)
          </p>
        </div>

        <div class="flex items-center mb-2 bg-white p-4 rounded-lg mb-2">
          <i class="fa-solid fa-location-dot text-st5"></i>
          <p class="pl-2">{{address}}</p>
        </div>


      </div>

    </div>

  </div>

</template>

<script>
import {useRoute} from "vue-router";
import Carousel from "@/components/UI/Carousel.vue"; // Adjust path as needed

export default {
  name: "ItemDetails",
  components: {
    Carousel,
  },
  data() {
    return {
      name: "Električna bušilica stayer",
      description:
          "Električna bušilica sa ergonomskim dizajnom — idealan alat za bušenje drveta, metala i " +
          "drugih materijala. Pogodna za kućne poslove i lakše profesionalne zadatke. " +
          "Radi na struju, opremljena je podesivom brzinom okretanja i funkcijom reverza.",
      available: false,
      maxPeriodDays: 3,
      image: [
        "https://i.ibb.co/zHNgfhB/44819-750x0.jpg",
        "https://i.ibb.co/DL5VbSY/27504-750x0.jpg",
        "https://i.ibb.co/zHNgfhB/44819-750x0.jpg",
      ],
      pricePerDay: 100,
      address: "Novi Sad, Omladinskog pokreta 2",
      telephone: "+381621110623",
      viber: "+381621110623",
    };
  },
  computed: {
    maskedTelephone() {
      return this.telephone.replace(/(\d{3})(\d{3})(\d{2})\d{2}$/, "$1 $2 $3****");
    },
    maskedViber() {
      return this.viber.replace(/(\d{3})(\d{3})(\d{2})\d{2}$/, "$1 $2 $3****");
    },
  },
  setup() {
    const route = useRoute();
    const itemId = route.params.id;

    return {itemId};
  },
};
</script>
