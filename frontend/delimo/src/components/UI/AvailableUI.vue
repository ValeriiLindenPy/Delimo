<template>
  <div v-if="available" class="flex items-center bg-white p-4 rounded-lg mb-2">
    <i class="fa-solid fa-hand text-green-500"></i>
    <span class="ml-1 font-bold">Dostupno</span>
    <p class="text-sm text-gray-500 pl-2">
      (To znači da je ova stvar trenutno dostupna za pozajmljivanje)
    </p>
  </div>
  <div v-else class="flex items-center bg-white p-4 rounded-lg mb-2">
    <i class="fa-solid fa-hand text-red-500"></i>
    <span class="ml-1 font-bold">Rezervacija</span>
    <p class="text-sm text-gray-500 pl-2">
      (To znači da je ova stvar trenutno u upotrebi kod drugog korisnika ili da je vlasnik trenutno ne izdaje. Pokušajte
      kasnije)
    </p>
  </div>
</template>
<script>
export default {
  name: 'AvailableUI',
  props: {
    available: {}
  }
}
</script>