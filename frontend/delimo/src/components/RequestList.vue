<template>
  <div class="container py-3 grid grid-cols-1 md:grid-cols-3 gap-4">
    <div
        v-for="post in posts"
        :key="post.id"
    >

      <UserRequestCard
          v-if="isEditable"
          :post="post"
      />
      <RequestCard
          v-else
          :post="post"
      />
    </div>
  </div>
</template>

<script>
import RequestCard from "@/components/UI/RequestCard.vue";
import UserRequestCard from "@/components/UI/UserRequestCard.vue";

export default {
  name: "PostList",
  components: {
    UserRequestCard: UserRequestCard,
    RequestCard: RequestCard
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
