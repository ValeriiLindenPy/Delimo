<template>


  <!-- 1) Оборачиваем контент, который должен вести на детальную страницу, в router-link -->

  <div
      :to="`/items/${post.id}`"
      class="max-w-sm rounded-lg overflow-hidden shadow-lg border bg-white"
  >
    <div @click="goItem" class="cursor-pointer">
      <div>
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
                class="bg-st2 text-st3 text-sm font-bold px-3 py-1 rounded
                     md:text-md whitespace-nowrap"
            >
              {{ post.maxPeriodDays }} dan(a)
            </span>
          </div>
          <p class="text-gray-600 text-sm truncate">
            {{ post.description }}
          </p>
        </div>
      </div>


      <!-- 2) Футер: доступность, цена... -->
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


    <!-- 3) Кнопка "Edit" - вынесена за пределы router-link -->
    <div class="flex justify-end p-2 border-t">
      <button
          class="bg-st4 text-white px-3 py-1 rounded hover:bg-st3 transition duration-500"
          @click="onEdit"
      >
        Izmeni
      </button>
    </div>
  </div>
</template>

<script>
import defaultImage from "@/assets/default-image.jpg"

export default {
  name: "UserPostCard",
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
  },
  methods: {
    onEdit() {
      // Либо открыть модалку, либо перейти на страницу редактирования
      // Пример с роутером:
      this.$router.push(`/items/${this.post.id}/edit`);
    },
    goItem() {
      this.$router.push(`/items/${this.post.id}`);
    }
  }
};
</script>
