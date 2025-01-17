<template>
  <router-link
      :to="`/items/${post.id}`"
      class="block max-w-sm rounded-lg overflow-hidden shadow-lg border bg-white hover:shadow-xl transition-shadow"
  >
  <div class="max-w-sm rounded-lg overflow-hidden shadow-lg border bg-white">
    <!-- Изображение -->
    <img
        :src="post?.images?.[0] || defaultImage"
        :alt="post?.title || 'Default alt text'"
        class="w-full h-48 object-cover"
    />

    <!-- Контент -->
    <div class="p-4">
      <div class="flex justify-between items-center mb-2">
        <h3 class="font-bold text-lg truncate">{{ post.title }}</h3>
        <span
            class="bg-st2 text-st3 text-sm font-bold px-3 py-1 rounded md:text-md whitespace-nowrap"
        >
          {{ post.maxPeriodDays }} dan(a)
        </span>
      </div>
      <p class="text-gray-600 text-sm truncate">
        {{ post.description }}
      </p>
    </div>

    <!-- Футер -->
    <div class="flex items-center justify-between px-4 py-2 border-t">
      <div class="flex items-center text-gray-500 text-sm gap-1">
        <i class="fa-solid fa-wallet text-st4"></i>
        <span v-if="post.pricePerDay">{{ post.pricePerDay }}.00 RSD</span>
        <span v-else>besplatno</span>
      </div>

      <!-- Доступность -->
      <div
          v-if="post.available"
          class="flex items-center gap-3 text-sm text-gray-500"
      >
        <span class="flex items-center">
          <i class="fa-solid fa-hand text-green-500"></i>
          <span class="ml-1">Dostupno</span>
        </span>
      </div>
      <div
          v-else
          class="flex items-center gap-3 text-sm text-gray-500"
      >
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
    }

  },
  props: {
    post: {
      type: Object,
      required: true
    }
  }
};
</script>
