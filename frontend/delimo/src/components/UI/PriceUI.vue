<template>
  <div class="flex items-center bg-white p-4 rounded-lg mb-2 gap-2">
    <i class="fa-solid fa-wallet text-st4"></i>
    <span v-if="price">{{ price }}.00 RSD</span>
    <span v-else>besplatno</span>
  </div>
</template>
<script>
export default {
  name: 'PriceUI',
  props: {
    price: {}
  }
}
</script>