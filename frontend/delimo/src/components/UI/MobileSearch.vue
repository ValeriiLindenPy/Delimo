<template>
  <div class="block md:hidden sticky top-[68px] w-full py-2 bg-st2 z-20">
    <div class="mx-3 rounded-lg bg-white h-10">
      <div class="flex h-full">
        <input
            placeholder="Traži ..."
            class="p-2 grow h-full rounded-l-lg"
        />
        <button
            @click="goItems"
            class="w-1/6 bg-st3 rounded-r-lg hover:text-st3 hover:bg-st5 transition-all duration-300"
        >
          <i class="fa-solid fa-magnifying-glass"></i>
        </button>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: 'MobileSearch',
  props: {
    goItems: {}
  }
}
</script>