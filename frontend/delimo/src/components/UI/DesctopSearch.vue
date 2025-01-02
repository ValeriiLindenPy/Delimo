<template>
  <div class="grow bg-white rounded-lg h-10">
    <div class="flex h-full">
      <input
          placeholder="Traži ..."
          class="p-2 grow rounded-l-lg h-full"
      />
      <button
          @click="goItems"
          class="w-10 bg-st3 rounded-r-lg hover:text-st3 hover:bg-st5 transition-all duration-300"
      >
        <i class="fa-solid fa-magnifying-glass"></i>
      </button>
    </div>
  </div>
</template>
<script>
export default {
  name: 'DesctopSearch',
  props: {
    goItems: {}
  }
}
</script>