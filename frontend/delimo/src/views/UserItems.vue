<template>
  <div class="container">
    <div class="flex justify-center items-center">
      <h1 class="text-white pt-3 text-2xl font-bold">Moje Stvari</h1>
    </div>


    <div class="mt-2">
      <PostList  v-if="posts" :is-editable="true" :posts="posts" />
    </div>
  </div>
</template>

<script>
import ItemList from "@/components/ItemList.vue";
import apiClient from "@/services/api.js";

export default {
  name: "UserOglasi",
  components: { PostList: ItemList },

  data() {
    return {
      posts: null,
    }
  },

  created() {
    apiClient("/my-items").then(res => {
      console.log(res.data.content);
      this.posts = res.data.content;
    })

  },
}
</script>
