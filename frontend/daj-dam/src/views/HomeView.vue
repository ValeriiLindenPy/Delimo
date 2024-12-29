

<template>
  <div class="container">
    <div class="flex justify-center mt-3">
      <img class="object-fit rounded-lg h-30 md:h-40" src="@/assets/img.png" alt="home">
    </div>
  </div>

  <div class="flex justify-center items-center mt-4">
    <hr class="flex-grow border-white">
    <p class="mx-4 text-white font-bold">Svi Oglasi</p>
    <hr class="flex-grow border-white">
  </div>

  <PostList class="mt-3" :posts="items" />


</template>

<script setup>

import PostList from "@/components/PostList.vue";
import {items} from "@/assets/items.js";
</script>
