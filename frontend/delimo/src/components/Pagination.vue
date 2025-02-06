<template>
  <div class="flex justify-center items-center space-x-4 my-4">
    <button
        @click="changePage(currentPage - 1)"
        :disabled="currentPage === 0"
        class="px-4 py-2 bg-st3 hover:bg-st4 transition-colors duration-500 rounded disabled:opacity-50"
    >
      <i class="fa-solid fa-chevron-left text-white"></i>
    </button>
    <span class="text-lg text-white font-bold">Strana {{ currentPage + 1 }} od {{ totalPages }}</span>
    <button
        @click="changePage(currentPage + 1)"
        :disabled="currentPage === totalPages - 1"
        class="px-4 py-2 bg-st3 rounded hover:bg-st4 transition-colors duration-500 disabled:opacity-50"
    >
      <i class="fa-solid fa-chevron-right text-white"></i>
    </button>
  </div>
</template>

<script>
export default {
  name: "Pagination",
  props: {
    currentPage: {
      type: Number,
      required: true,
    },
    totalPages: {
      type: Number,
      required: true,
    },
  },
  emits: ["page-changed"],
  methods: {
    changePage(page) {
      if (page >= 0 && page < this.totalPages) {
        this.$emit("page-changed", page);
      }
    },
  },
};
</script>
