<template>
  <!-- Весь контейнер карточки -->
  <div
      class="block max-w-sm rounded-lg overflow-hidden shadow-lg border bg-white
           hover:shadow-xl transition-shadow"
  >
    <!-- 1) Оборачиваем контент, который должен вести на детальную страницу, в router-link -->
    <router-link :to="`/requests/${post.id}`" class="block">
      <div>


        <!-- Контент -->
        <div class="p-4">
          <div class="flex justify-between gap-1 items-center mb-2">
            <h3 class="font-bold text-lg truncate">{{ post.name }}</h3>
            <span
                class="bg-st2 text-st3 text-sm font-bold px-3 py-1 rounded
                     md:text-md whitespace-nowrap"
            >
              {{ post.maxPeriodDays }} dan(a)
            </span>
          </div>
        </div>
      </div>
    </router-link>

    <!-- 2) Футер: доступность, цена... -->
    <div class="flex items-center justify-between px-4 py-2 border-t">
      <div class="flex items-center text-gray-500 text-sm gap-1">
        <i class="fa-solid fa-wallet text-st4"></i>
        <span v-if="post.pricePerDay">{{ post.pricePerDay }}.00 RSD</span>
        <span v-else>besplatno</span>
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
export default {
  name: "UserRequestCard",
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
      this.$router.push(`/requests/${this.post.id}/edit`);
    }
  }
};
</script>
