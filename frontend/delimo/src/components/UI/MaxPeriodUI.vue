<template>
  <div class="flex items-center mb-2 bg-white p-4 md:rounded-lg">
            <span
                class="bg-st2 text-st3 text-sm font-bold px-3 py-3 rounded md:text-md whitespace-nowrap"
            >
          {{ maxPeriodDays }} dan(a)
        </span>
    <p v-if="isRequest" class="flex items-center justify-center pl-2 text-sm text-gray-500">
      (Ovo je period na koji autor zahteva želi da iznajmi stvar)
    </p>
    <p v-else class="flex items-center justify-center pl-2 text-sm text-gray-500">
      (Ovo je maksimalni period na koji vam vlasnik može pozajmiti ovu stvar)
    </p>
  </div>
</template>
<script>
export default {
  name: 'MaxPeriodUI',
  props: {
    maxPeriodDays: {},
    isRequest: {
      type: Boolean,
      required: false
    }
  }
}
</script>