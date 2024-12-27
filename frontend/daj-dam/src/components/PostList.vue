<template>
  <div class="container py-3 grid grid-cols-1 md:grid-cols-3 gap-4">
    <PostCard
        v-for="post in posts"
        :key="post.id"
        :post="post"
    />
  </div>
</template>

<script>
import PostCard from "./UI/PostCard.vue";

export default {
  name: "PostList",
  components: {
    PostCard
  },
  props: {
    posts: {
      type: Array,
      required: true
    }
  }
};
</script>
