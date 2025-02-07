<template>
  <PopUpModal :is-active="isPopUp" @close="togglePopUp">
    <div class="flex flex-col items-center justify-center gap-2">
      <h1>Odbrisi stvar?</h1>
      <div class="flex gap-4">
        <button
            class="bg-st3 text-white font-medium py-2 px-4 rounded-md hover:bg-st4 transition"
            @click="confirmDelete"
        >
          Da
        </button>
        <button
            class="bg-st3 text-white font-medium py-2 px-4 rounded-md hover:bg-st4 transition"
            @click="togglePopUp"
        >
          Ne
        </button>
      </div>
    </div>
  </PopUpModal>

  <!-- Main container for the post card -->
  <div class="max-w-sm rounded-lg overflow-hidden shadow-lg border bg-white">
    <!-- Clickable area leading to detailed page -->
    <div @click="goItem" class="cursor-pointer">
      <div>
        <!-- Изображение -->
        <img
            :src="post?.images?.[0] || defaultImage"
            :alt="post?.title || 'Default alt text'"
            class="w-full h-48 object-cover"
        />
        <!-- Контент -->
        <div class="p-4">
          <div class="flex justify-between items-center mb-2">
            <h3 class="font-bold text-lg truncate">{{ post.title }}</h3>
            <span
                class="bg-st2 text-st3 text-sm font-bold px-3 py-1 rounded
                     md:text-md whitespace-nowrap"
            >
              {{ post.maxPeriodDays }} dan(a)
            </span>
          </div>
          <p class="text-gray-600 text-sm truncate">
            {{ post.description }}
          </p>
        </div>
      </div>
    </div>

    <!-- Footer: availability, price... -->
    <div class="flex items-center justify-between px-4 py-2 border-t">
      <div class="flex items-center text-gray-500 text-sm gap-1">
        <i class="fa-solid fa-wallet text-st4"></i>
        <span v-if="post.pricePerDay">{{ post.pricePerDay }}.00 RSD</span>
        <span v-else>besplatno</span>
      </div>

      <div v-if="post.available" class="flex items-center gap-3 text-sm text-gray-500">
        <span class="flex items-center">
          <i class="fa-solid fa-hand text-green-500"></i>
          <span class="ml-1">Dostupno</span>
        </span>
      </div>
      <div v-else class="flex items-center gap-3 text-sm text-gray-500">
        <span class="flex items-center">
          <i class="fa-solid fa-hand text-red-500"></i>
          <span class="ml-1">Rezervacija</span>
        </span>
      </div>
    </div>

    <!-- Buttons "Delete" and "Edit" -->
    <div class="flex justify-end p-2 border-t gap-3">
      <button
          @click="handleDeleteItem(post.id)"
          class="px-2 bg-red-500 transition-colors duration-500 hover:bg-black text-white rounded-lg"
      >
        <i class="fa-regular fa-trash-can"></i>
      </button>
      <button
          class="bg-st4 text-white px-3 py-1 rounded hover:bg-st3 transition duration-500"
          @click="onEdit"
      >
        Izmeni
      </button>
    </div>
  </div>
</template>

<script>
import defaultImage from "@/assets/default-image.jpg";
import PopUpModal from "@/components/UI/PopUpModal.vue";
import { deleteItemById  } from "@/services/itemService";
import {useAuthStore} from "@/stores/auth.js";

export default {
  name: "UserPostCard",
  components: { PopUpModal },
  props: {
    post: {
      type: Object,
      required: true
    }
  },
  data() {
    const store = useAuthStore();
    return {
      defaultImage,
      store,
      isPopUp: false,
      itemDeleteId: null,
    };
  },
  methods: {
    togglePopUp() {
      this.isPopUp = !this.isPopUp;
    },
    onEdit() {
      console.log("Edit", this.post.id);
      this.$router.push(`/items/${this.post.id}/edit`);
    },
    goItem() {
      this.$router.push(`/items/${this.post.id}`);
    },
    goProfile() {
      this.$router.push(`/users/${this.store.profile.id}`);
    },
    async handleDeleteItem(id) {
      this.togglePopUp();
      // For debugging; remove alert in production.

      this.itemDeleteId = Number(id);
    },
    async confirmDelete() {
      try {
        await deleteItemById(this.itemDeleteId);
        this.togglePopUp();
        this.itemDeleteId = null;
        this.goProfile();
      } catch (err) {
        console.log(err.message);
      }
    }
  }
};
</script>
