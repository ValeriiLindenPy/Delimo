<template>
  <div v-if="authorized" class="flex-1 h-10 flex items-center justify-center gap-4 lg:text-xl md:text-sm">
    <button
        class="hover:bg-st3 hover:text-st5 flex items-center justify-center border-solid h-full p-5 text-white rounded-lg border-st3 border-2 transition-all duration-300">
      Moj nalog
    </button>
    <button
        class="hover:bg-st3 hover:text-st5 flex items-center justify-center border-solid h-full p-5 text-white rounded-lg border-st3 border-2 transition-all duration-300">
      Odjavite se
    </button>
  </div>

  <div v-else class="flex-1 h-10 flex items-center justify-center gap-4 lg:text-xl md:text-sm">
    <button
        class="hover:bg-st3 hover:text-st5 flex items-center justify-center border-solid h-full p-5 text-white rounded-lg border-st3 border-2 transition-all duration-300">
      Ulogujte se
    </button>
    <button
        class="hover:bg-st3 hover:text-st5 flex items-center justify-center border-solid h-full p-5 text-white rounded-lg border-st3 border-2 transition-all duration-300">
      Registrujte se
    </button>
  </div>
</template>
<script>
export default {
  name: 'AuthorizationItems',
  props: {
    authorized: {
      type: Boolean,
      required: true,
    },
  }
}
</script>