<template>
  <div class="flex items-center justify-center mb-2 bg-white p-4 rounded-lg">
            <span
                class="bg-st2 w-[140px] font-bold text-center p-3 text-st3 text-[12px] rounded md:text-[12px]"
            >
          {{ maxPeriodDays }} dan(a)
        </span>
    <p class="text-sm text-gray-500 pt-2 pl-2">
      (Ovo je maksimalni period na koji vam vlasnik može pozajmiti ovu stvar)
    </p>
  </div>
</template>
<script>
export default {
  name: 'MaxPeriodUI',
  props: {
    maxPeriodDays: {}
  }
}
</script>