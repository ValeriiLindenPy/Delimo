<template>
  <div class="md:container flex justify-center items-center mt-16">
    <div class="flex flex-col w-full md:w-2/3">
      <div class="flex flex-col justify-center">
        <!-- Only show if `item` is loaded -->
        <NameUI v-if="item" :name="item.title" />
        <PriceUI v-if="item" :price="item.pricePerDay" />
      </div>

      <!-- INFO -->
      <div>
        <DescriptionUI v-if="item" :description="item.description" />
        <AddressUI v-if="item" :address="address" />
        <MaxPeriodUI v-if="item" :is-request="true" :max-period-days="item.maxPeriodDays" />
        <ContactsUI
            v-if="item"
            :name="item.title"
            :telephone="item.requester.phone"
            :viber="item.requester.viber"
        />
      </div>
    </div>

    <!--Images and contacts-->

  </div>
</template>

<script>
import {ref, computed, onMounted} from "vue";
import {useRoute} from "vue-router";
import {fetchRequest} from "@/services/requestService.js";

// Importing UI components
import NameUI from "@/components/UI/NameUI.vue";
import AddressUI from "@/components/UI/AddressUI.vue";
import ContactsUI from "@/components/UI/ContactsUI.vue";
import DescriptionUI from "@/components/UI/DescriptionUI.vue";
import PriceUI from "@/components/UI/PriceUI.vue";
import MaxPeriodUI from "@/components/UI/MaxPeriodUI.vue";


export default {
  name: "RequestDetails",
  components: {
    MaxPeriodUI,
    DescriptionUI,
    NameUI,
    AddressUI,
    ContactsUI,
    PriceUI
  },
  setup() {
    const route = useRoute();
    const itemId = parseInt(route.params.id, 10);
    const item = ref(null);

    onMounted(async () => {
      try {
        const res = await fetchRequest(itemId);
        item.value = res.data;
      } catch (error) {
        this.$router.push('/server-error');
        console.error("Error fetching request data:", error);
      }
    });

    // Dynamically build address using `computed`
    const address = computed(() => {
      // Make sure `item` and `requester` exist
      if (item.value && item.value.requester) {
        return `${item.value.requester.city}, ${item.value.requester.street}`;
      }
      return "";
    });

    return {
      item,
      address,
    };
  },
};
</script>
