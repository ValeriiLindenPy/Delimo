<template>
  <div class="container py-3 grid grid-cols-1 md:grid-cols-3 gap-4">
    <div
        v-for="post in posts"
        :key="post.id"
    >

      <UserPostCard
          v-if="isEditable"
          :post="post"
      />
      <PostCard
          v-else
          :post="post"
      />
    </div>
  </div>
</template>

<script>
import ItemCard from "./UI/ItemCard.vue";
import UserItemCard from "@/components/UI/UserItemCard.vue";

export default {
  name: "PostList",
  components: {
    UserPostCard: UserItemCard,
    PostCard: ItemCard
  },
  props: {
    posts: {
      type: Array,
      required: true
    },
    isEditable: {
      type: Boolean,
      default: false
    }
  }
};
</script>
