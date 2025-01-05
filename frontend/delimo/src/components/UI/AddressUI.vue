<template>
  <div class="flex items-center mb-2 bg-white p-4 rounded-lg mb-2">
    <i class="fa-solid fa-location-dot text-st5"></i>
    <p class="pl-2">{{address}}</p>
  </div>
</template>
<script>
export default {
  name: 'AddressUI',
  props: {
    address: {}
  }
}
</script>