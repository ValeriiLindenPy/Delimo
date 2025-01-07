<template>
  <div class="container flex flex-col justify-center items-center">
    <h1 class="text-white pt-3 text-2xl font-bold">Moje Stvari</h1>

    <div class="mt-2">
      <PostList :is-editable="true" :posts="posts" />
    </div>
  </div>
</template>

<script>
import ItemList from "@/components/ItemList.vue";
import { items } from "@/assets/items.js";

export default {
  name: "UserOglasi",
  components: { PostList: ItemList },

  data() {
    return {
      posts: [],
    }
  },

  created() {
    // Make sure to convert route param to a number if needed
    // e.g. const ownerId = Number(this.$route.params.id);
    // Then filter by that:
    const ownerId = Number(this.$route.params.id);
    this.posts = items.filter(item => item.owner.id === ownerId);

    if (!this.posts.length) {
      console.warn('No posts found for this owner id -' + ownerId);
    }
  },
}
</script>
