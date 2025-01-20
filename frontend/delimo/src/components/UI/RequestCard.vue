<template>
  <router-link
      :to="`/requests/${post.id}`"
      class="block max-w-sm rounded-lg overflow-hidden shadow-lg border bg-white hover:shadow-xl transition-shadow"
  >
  <div class="max-w-sm rounded-lg overflow-hidden shadow-lg border bg-white">


    <!-- Контент -->
    <div class="p-4">
      <div class="flex justify-between items-center mb-2">
        <h3 class="font-bold text-lg truncate">{{ post.title }}</h3>

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

    </div>
  </div>
  </router-link>
</template>

<script>
export default {
  name: "PostCard",
  props: {
    post: {
      type: Object,
      required: true
    }
  }
};
</script>
