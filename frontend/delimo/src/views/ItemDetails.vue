<template>
  <div class="container">
    <div class="grid grid-cols-1 pt-6 md:grid-cols-2 gap-1">
      <!--Images and contacts-->
      <div class="flex flex-col justify-center">
        <div class="images-carousel mb-2">
          <Carousel v-if="item" :images="item.image" />
        </div>
        <ContactsUI v-if="item && item.available" :name="item.name" :telephone="item.owner.telephone" :viber="item.owner.viber" />
      </div>

      <!--INFO-->
      <div>
        <NameUI v-if="item" :name="item.name" />
        <DescriptionUI v-if="item" :description="item.description" />
        <AvailableUI v-if="item" :available="item.available" />
        <MaxPeriodUI v-if="item" :max-period-days="item.maxPeriodDays" />
        <AddressUI v-if="item" :address="address" />
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed } from "vue";
import { useRoute } from "vue-router";
import Carousel from "@/components/UI/Carousel.vue";
import NameUI from "@/components/UI/NameUI.vue";
import DescriptionUI from "@/components/UI/DescriptionUI.vue";
import AvailableUI from "@/components/UI/AvailableUI.vue";
import MaxPeriodUI from "@/components/UI/MaxPeriodUI.vue";
import AddressUI from "@/components/UI/AddressUI.vue";
import ContactsUI from "@/components/UI/ContactsUI.vue";


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
  setup() {
    const route = useRoute();
    const itemId = parseInt(route.params.id, 10); // Ensure ID is a number
    const item = null; //todo

    const address = computed(() => {
      if (item.value && item.value.owner) {
        return `${item.value.owner.city}, ${item.value.owner.address}`;
      }
      return "";
    });

    return { item, address };
  },
};
</script>
