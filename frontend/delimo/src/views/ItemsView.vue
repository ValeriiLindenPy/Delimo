<template>
  <div class="flex justify-center items-center mt-4 mb-4">
    <hr class="flex-grow border-white">
    <p class="mx-4 text-white font-bold">Svi Oglasi</p>
    <hr class="flex-grow border-white">
  </div>
  <PostList :posts="posts" />

  <Pagination
      :currentPage="pagination.page"
      :totalPages="pagination.totalPages"
      @page-changed="fetchItems"
  />


</template>

<script>

import ItemList from '../components/ItemList.vue';
import {getItems} from "@/services/itemService.js";
import Pagination from "@/components/Pagination.vue";


export default {
  name: 'ItemsView',
  components: {PostList: ItemList, Pagination},
  data() {
    return {
      posts: [],
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
      const { data } = await getItems(page);
      this.posts = data.content;
      this.pagination.page = data.page.number;
      this.pagination.totalPages = data.page.totalPages;
    },
  },
};

</script>

<style lang="scss" scoped>
</style>
