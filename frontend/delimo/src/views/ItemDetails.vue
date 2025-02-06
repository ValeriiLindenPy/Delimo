<template>
  <div class="md:container">
    <div class="grid grid-cols-1 pt-6 md:grid-cols-2 gap-1">
      <!-- Images and contacts -->
      <div class="flex flex-col justify-center">
        <div class="images-carousel m-3">
          <Carousel v-if="item" :images="item.images" :defaultImage="defaultImage" />
        </div>
        <NameUI v-if="item" :name="item.title" />
        <PriceUI v-if="item" :price="item.pricePerDay" />
      </div>

      <!-- Info -->
      <div>

        <DescriptionUI v-if="item" :description="item.description" />
        <AvailableUI v-if="item" :available="item.available" />
        <MaxPeriodUI v-if="item" :max-period-days="item.maxPeriodDays" />
        <AddressUI v-if="item" :address="getAddress" />
        <ContactsUI
            v-if="item && item.available"
            :name="item.title"
            :telephone="item.owner?.phone"
            :viber="item.owner?.viber"
        />
      </div>
    </div>
  </div>
</template>

<script>
import { useRoute } from 'vue-router'
import Carousel from '@/components/UI/Carousel.vue'
import NameUI from '@/components/UI/NameUI.vue'
import DescriptionUI from '@/components/UI/DescriptionUI.vue'
import AvailableUI from '@/components/UI/AvailableUI.vue'
import MaxPeriodUI from '@/components/UI/MaxPeriodUI.vue'
import AddressUI from '@/components/UI/AddressUI.vue'
import ContactsUI from '@/components/UI/ContactsUI.vue'
import defaultImage from '@/assets/default-image.jpg'
import {getItem} from "@/services/itemService.js";
import PriceUI from "@/components/UI/PriceUI.vue";

export default {
  name: 'ItemDetails',
  components: {
    ContactsUI,
    AddressUI,
    MaxPeriodUI,
    AvailableUI,
    DescriptionUI,
    NameUI,
    Carousel,
    PriceUI,
  },
  data() {
    return {
      defaultImage, // Path to the default image
      item: null, // Item details
    }
  },
  computed: {
    getAddress() {
      if (this.item && this.item.owner) {
        return `${this.item.owner.city + ', ' || ''} ${this.item.owner.street || ''}`.trim()
      }
      return null
    },
  },
  async created() {
    const route = useRoute()
    const itemId = Number(route.params.id)

    try {
      this.item = (await getItem(itemId)).data
    } catch (error) {
      console.error(error);
      this.$router.push('/server-error')
    }

  },
}
</script>
