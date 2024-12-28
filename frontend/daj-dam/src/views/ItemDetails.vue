<template>
  <div class="container">

    <div class="grid grid-cols-1 pt-6 md:grid-cols-2">
      <!--Images and contacts-->
      <div class="flex flex-col justify-center">
        <div class="images-carousel mb-2">
          <Carousel :images="image"/>
        </div>
        <ContactsUI v-if="available" :name="name" :telephone="telephone" :viber="viber"/>
      </div>

      <!--INFO-->
      <div>
        <NameUI :name="name"/>

        <DescriptionUI :description="description"/>

        <AvailableUI :available="available"/>

        <MaxPeriodUI :max-period-days="maxPeriodDays"/>

        <AddressUI :address="address"/>
      </div>

    </div>
  </div>

</template>

<script>
import {useRoute} from "vue-router";
import Carousel from "@/components/UI/Carousel.vue";
import NameUI from "@/components/UI/NameUI.vue";
import DescriptionUI from "@/components/UI/DescriptionUI.vue";
import AvailableUI from "@/components/UI/AvailableUI.vue";
import MaxPeriodUI from "@/components/UI/MaxPeriodUI.vue";
import AddressUI from "@/components/UI/AddressUI.vue";
import ContactsUI from "@/components/UI/ContactsUI.vue"; // Adjust path as needed

export default {
  name: "ItemDetails",
  components: {
    ContactsUI,
    AddressUI,
    MaxPeriodUI,
    AvailableUI,
    DescriptionUI,
    NameUI,
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
      viber: "+381658282578",
    };
  },
  setup() {
    const route = useRoute();
    const itemId = route.params.id;

    return {itemId};
  },
};
</script>
