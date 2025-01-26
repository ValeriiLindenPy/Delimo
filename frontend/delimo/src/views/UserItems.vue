<template>
  <div class="container">
    <div class="flex justify-center items-center">
      <h1 class="text-white pt-3 text-2xl font-bold">Moje Stvari</h1>
    </div>


    <div class="mt-2">
      <PostList  v-if="posts" :is-editable="true" :posts="posts" />
      <Pagination
          :currentPage="pagination.page"
          :totalPages="pagination.totalPages"
          @page-changed="fetchItems"
      />
    </div>
  </div>
</template>

<script>
import ItemList from "@/components/ItemList.vue";
import {fetchMyItems} from "@/services/itemService.js";
import Pagination from "@/components/Pagination.vue";

export default {
  name: "UserOglasi",
  components: { PostList: ItemList, Pagination },

  data() {
    return {
      posts: null,
      pagination: {
        page: 0,
        totalPages: 1,
      },
    }
  },
  methods: {
    async fetchItems(page) {
      const { data } = await fetchMyItems(page);
      this.posts = data.content;
      this.pagination.page = data.page.number;
      this.pagination.totalPages = data.page.totalPages;
    }
  },

  async created() {
    await this.fetchItems(0);
  },
}
</script>
