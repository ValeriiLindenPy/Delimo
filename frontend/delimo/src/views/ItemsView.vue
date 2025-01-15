<template>
  <div class="flex justify-center items-center mt-4 mb-4">
    <hr class="flex-grow border-white">
    <p class="mx-4 text-white font-bold">Svi Oglasi</p>
    <hr class="flex-grow border-white">
  </div>
  <PostList :posts="posts" />
</template>

<script>
import { ref } from 'vue';
import ItemList from '../components/ItemList.vue';


export default {
  name: 'ItemsView',
  components: {PostList: ItemList},
  setup() {
    const posts = null; // todo
    return {posts}; // Return posts so it's accessible in the template
  }
};
</script>

<style lang="scss" scoped>
</style>
