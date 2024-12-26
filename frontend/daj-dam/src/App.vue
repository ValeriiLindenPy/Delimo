<template>
  <div class="flex flex-col min-h-screen font-Inter bg-vanilla">
    <Navigation/>
    <RouterView/>
  </div>
</template>

<script setup>
import {RouterView} from "vue-router";
import Navigation from "@/components/Navigation.vue";
</script>

<style scoped>

</style>
