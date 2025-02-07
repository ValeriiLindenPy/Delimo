<template>
  <div class="md:container">
    <div class="flex justify-center items-center">
      <h1 class="text-white pt-3 text-2xl font-bold">Moje stvari</h1>
    </div>

    <div class="mt-2">
      <Loader v-if="loading" />
      <template v-else>
        <PostList v-if="posts && posts.length" :is-editable="true" :posts="posts" />

        <div v-else class="text-center mt-5 p-4 bg-gray-800 text-white rounded-lg">
          <p class="text-lg">Nemate još nijednu stvar.</p>
          <p class="text-md mt-2">Dodajte prvu stvar klikom na dugme "Kreiraj oglas".</p>
        </div>
      </template>

      <Pagination
          v-if="posts && posts.length"
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
import Loader from "@/components/UI/Loader.vue";

export default {
  name: "UserOglasi",
  components: {PostList: ItemList, Pagination, Loader},

  data() {
    return {
      posts: null,
      pagination: {
        page: 0,
        totalPages: 1,
      },
      loading: false
    };
  },
  methods: {
    async fetchItems(page) {
      this.loading = true;
      try {
        const {data} = await fetchMyItems(page);
        this.posts = data.content;
        this.pagination.page = data.page.number;
        this.pagination.totalPages = data.page.totalPages;
      } catch (error) {
        console.log(error);
        this.$router.push("/server-error");
      } finally {
        this.loading = false;
      }
    },
  },

  async created() {
    await this.fetchItems(0);
  },
};
</script>