<template>
  <div>
    <!-- Фильтр по городам -->
    <div class="flex justify-end p-4">
      <div>
        <label for="citySelect" class="text-white font-bold">Mesto:</label>
        <select id="citySelect" v-model="city" @change="fetchItems(0)" class="ml-2 bg-st3 text-white font-bold text-xl rounded-lg p-2">
          <!-- Опция для всех городов -->
          <option value="">Svi gradovi</option>
          <!-- Опции, полученные из массива cities -->
          <option class="hover:bg-st4" v-for="cityItem in cities" :key="cityItem.id" :value="cityItem.name">
            {{ cityItem.name }}
          </option>
        </select>
      </div>
    </div>

    <div class="flex justify-center items-center mt-4 mb-4">
      <hr class="flex-grow border-white">
      <p class="mx-4 text-white font-bold">Svi Oglasi</p>
      <hr class="flex-grow border-white">
    </div>
    <PostList :posts="posts"/>

    <Pagination
        :currentPage="pagination.page"
        :totalPages="pagination.totalPages"
        @page-changed="fetchItems"
    />
  </div>
</template>

<script>
import ItemList from '../components/ItemList.vue';
import {getItemsSearch} from "@/services/itemService.js";
import Pagination from "@/components/Pagination.vue";
import {cities} from "@/assets/cities.js";

export default {
  name: 'ItemsView',
  components: {PostList: ItemList, Pagination},
  data() {
    return {
      posts: [],
      // По умолчанию город не выбран
      city: "",
      // Добавляем список городов для селекта
      cities,
      pagination: {
        page: 0,
        totalPages: 1,
      },
    }
  },
  async created() {
    await this.fetchItems(0);
  },
  methods: {
    async fetchItems(page) {
      // Передаём выбранный город в запрос
      const {data} = await getItemsSearch({
        city: this.city,
        page: page
      });
      this.posts = data.content;
      this.pagination.page = data.page.number;
      this.pagination.totalPages = data.page.totalPages;
    },
  },
};
</script>


