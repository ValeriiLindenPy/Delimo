<template>
  <div class="container flex justify-center">
    <div class="pt-6 flex flex-col w-full md:w-2/3">
      <!--Images and contacts-->
      <div class="flex flex-col justify-center">
        <ContactsUI v-if="item" :name="item.name" :telephone="item.owner.telephone" :viber="item.owner.viber" />
      </div>

      <!--INFO-->
      <div>
        <NameUI v-if="item" :name="item.name" />
        <MaxPeriodUI v-if="item" :is-request="true" :max-period-days="item.maxPeriodDays" />
        <AddressUI v-if="item" :address="address" />
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed } from "vue";
import { useRoute } from "vue-router";
import NameUI from "@/components/UI/NameUI.vue";
import MaxPeriodUI from "@/components/UI/MaxPeriodUI.vue";
import AddressUI from "@/components/UI/AddressUI.vue";
import ContactsUI from "@/components/UI/ContactsUI.vue";
import {requests} from "@/assets/requests.js";

export default {
  name: "RequestDetails",
  components: {
    ContactsUI,
    AddressUI,
    MaxPeriodUI,
    NameUI,
  },
  setup() {
    const route = useRoute();
    const itemId = parseInt(route.params.id, 10); // Ensure ID is a number
    const item = ref(requests.find((item) => item.id === itemId));

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
