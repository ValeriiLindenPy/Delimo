<template>
  <PostList :posts="posts" />
</template>

<script>
import { ref } from 'vue';
import ItemList from '../components/ItemList.vue';
import { items } from "@/assets/items.js";

export default {
  name: 'ItemsView',
  components: {PostList: ItemList},
  setup() {
    const posts = ref(items); // Use ref to create a reactive variable
    return {posts}; // Return posts so it's accessible in the template
  }
};
</script>

<style lang="scss" scoped>
</style>
