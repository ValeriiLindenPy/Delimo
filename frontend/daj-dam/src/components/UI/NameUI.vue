<template>
  <div class="flex items-center text-center mt-2 mb-2 bg-white p-2 rounded-lg">
    <h1 class="text-xl font-bold">{{ name }}</h1>
  </div>
</template>
<script>
export default {
  name: 'NameUI',
  props: {
    name: {}
  }
}
</script>