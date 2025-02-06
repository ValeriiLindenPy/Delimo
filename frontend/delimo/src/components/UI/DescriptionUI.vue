<template>
  <div class="card mt-2 mb-2 bg-white p-4 md:rounded-lg">
    <p class="mb-2">{{ description }}</p>
  </div>
</template>
<script>
export default {
  name: 'DescriptionUI',
  props: {
    description: {}
  }
}
</script>