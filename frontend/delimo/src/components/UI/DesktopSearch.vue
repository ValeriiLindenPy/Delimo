<template>
  <div class="grow bg-white rounded-lg h-10">
    <div class="flex h-full">
      <input
          v-model="searchInput"
          placeholder="Traži ..."
          class="p-2 grow rounded-l-lg h-full"
      />
      <button
          @click="triggerSearch"
          class="w-10 bg-st3 rounded-r-lg hover:text-st3 hover:bg-st5 transition-all duration-300"
      >
        <i class="fa-solid fa-magnifying-glass"></i>
      </button>
    </div>
  </div>
</template>

<script>

export default {
  name: "DesktopSearch",
  data() {
    return {
      searchInput: this.$route.query.text || "", // Initialize with query text
    };
  },
  methods: {

    // Trigger search explicitly on button click
    triggerSearch() {
      this.$router.push(`/search?text=${this.searchInput}`);
      this.searchInput = "";
    },
  },
};
</script>
