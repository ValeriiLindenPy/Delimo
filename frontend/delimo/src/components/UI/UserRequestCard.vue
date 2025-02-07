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
    <div @click="goRequest" class="cursor-pointer">
      <div>
        <!-- Контент -->
        <div class="p-4">
          <div class="flex justify-between items-center mb-2">
            <h3 class="font-bold text-lg truncate">{{ post.title }}</h3>

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
import PopUpModal from "@/components/UI/PopUpModal.vue";
import {deleteRequestById} from "@/services/requestService.js";
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
      store,
      isPopUp: false,
      requestDeleteId: null,
    };
  },
  methods: {
    togglePopUp() {
      this.isPopUp = !this.isPopUp;
    },
    onEdit() {
      this.$router.push(`/requests/${this.post.id}/edit`);
    },
    goRequest() {
      this.$router.push(`/requests/${this.post.id}`);
    },
    goProfile() {
      this.$router.push(`/users/${this.store.profile.id}`);
    },
    async handleDeleteItem(id) {
      this.togglePopUp();
      // For debugging; remove alert in production.

      this.requestDeleteId = Number(id);
    },
    async confirmDelete() {
      try {
        await deleteRequestById(this.requestDeleteId);
        this.togglePopUp();
        this.requestDeleteId = null;
        this.goProfile();
      } catch (err) {
        alert(err);
      }
    }
  }
};
</script>
