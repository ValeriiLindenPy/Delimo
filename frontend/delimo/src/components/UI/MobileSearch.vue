<template>
  <div class="block md:hidden sticky top-[68px] w-full py-2 bg-st2 z-[5]">
    <div class="mx-3 rounded-lg bg-white h-10 relative">
      <div class="flex h-full">
        <input
            v-model="searchInput"
            @focus="isInputActive = true"
            @blur="handleBlur"
            placeholder="Traži ..."
            class="p-2 grow h-full rounded-l-lg"
        />
        <button
            @click="triggerSearch()"
            class="w-1/6 bg-st3 rounded-r-lg hover:text-st3 hover:bg-st5 transition-all duration-300"
        >
          <i class="fa-solid fa-magnifying-glass"></i>
        </button>
      </div>

      <!-- Title list -->
      <div
          v-if="isInputActive && items.length > 0"
          class="absolute top-full left-0 w-full bg-white shadow-lg rounded-lg mt-1 z-50"
      >
        <div
            class="px-4 py-2 border-b hover:bg-gray-100 cursor-pointer"
            v-for="item in items"
            :key="item.id"
        >
          <p @click="triggerSearch(item.title)" v-text="item.title"></p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getItemsTitleSearch } from "@/services/itemService.js";
import _ from "lodash";

export default {
  name: "MobileSearch",
  data() {
    return {
      items: [],
      searchInput: this.$route.query.text || "",
      isInputActive: false, // Tracks whether the input field is active
    };
  },
  watch: {
    searchInput: _.debounce(function () {
      this.fetchTitle();
    }, 300),
    "$route.query.text": {
      immediate: true,
      handler(newText) {
        this.searchInput = newText || "";
        this.fetchTitle();
      },
    },
  },
  methods: {
    async fetchTitle() {
      const query = this.searchInput.trim();
      if (!query) {
        this.items = [];
        return;
      }
      try {
        const res = await getItemsTitleSearch(query);
        this.items = res.data;
      } catch (error) {
        console.error("Error fetching items:", error);
      }
    },

    triggerSearch(title) {
      const searchText = title || this.searchInput.trim() || "";
      this.$router.push(`/search?text=${encodeURIComponent(searchText)}`);
    },
    handleBlur() {
      // Delay hiding the list to handle cases like clicking an item
      setTimeout(() => {
        this.isInputActive = false;
      }, 200);
    },
  },
};
</script>