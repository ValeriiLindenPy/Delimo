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
import {fetchMyItems} from "@/services/itemService.js";

export default {
  name: "UserOglasi",
  components: { PostList: ItemList },

  data() {
    return {
      posts: null,
    }
  },

  async created() {
    await fetchMyItems().then(res => this.posts =  res.data.content)
        .catch(err => console.error(err));

  },
}
</script>
