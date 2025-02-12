<template>
  <router-link
      :to="`/items/${post.id}`"
      class="block max-w-sm rounded-lg overflow-hidden shadow-lg border bg-white hover:shadow-xl transition-shadow"
  >
    <div class="max-w-sm rounded-lg overflow-hidden shadow-lg border bg-white">
      <img
          :src="post?.images?.[0] || defaultImage"
          :alt="post?.title || 'Default alt text'"
          class="w-full h-48 object-cover"
      />

      <div class="p-4">
        <div class="grid grid-cols-[1fr_auto] gap-4 items-center mb-2">
          <div class="min-w-0">
            <h3 class="font-bold text-lg truncate">{{ post.title }}</h3>
            <p class="text-gray-600 text-sm truncate">{{ post.description }}</p>
          </div>

          <div class="flex flex-col gap-2 text-right max-w-[80px]">
            <span
                class="bg-st2 text-st3 text-center text-sm font-bold px-3 py-1 rounded md:text-md whitespace-nowrap"
            >
              {{ post.maxPeriodDays }} dan(a)
            </span>
            <span class="text-sm text-gray-500">{{ post.created }}</span>
          </div>
        </div>
      </div>

      <div class="flex items-center justify-between px-4 py-2 border-t">
        <div class="flex items-center text-gray-500 text-sm gap-1">
          <i class="fa-solid fa-wallet text-st4"></i>
          <span v-if="post.pricePerDay">{{ post.pricePerDay }}.00 RSD</span>
          <span v-else>besplatno</span>
        </div>

        <div v-if="post.available" class="flex items-center gap-3 text-sm text-gray-500">
          <span class="flex items-center">
            <i class="fa-solid fa-hand text-green-500"></i>
            <span class="ml-1">Dostupno</span>
          </span>
        </div>
        <div v-else class="flex items-center gap-3 text-sm text-gray-500">
          <span class="flex items-center">
            <i class="fa-solid fa-hand text-red-500"></i>
            <span class="ml-1">Rezervacija</span>
          </span>
        </div>
      </div>
    </div>
  </router-link>
</template>

<script>
import defaultImage from '@/assets/default-image.jpg';
export default {
  name: "PostCard",
  data() {
    return {
      defaultImage,
    };
  },
  props: {
    post: {
      type: Object,
      required: true
    }
  }
};
</script>

<style scoped>
.truncate {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
