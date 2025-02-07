<template>
  <div class="flex items-center mb-2 bg-white p-4 md:rounded-lg">
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