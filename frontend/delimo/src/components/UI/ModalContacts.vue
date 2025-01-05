<template>
  <div class="fixed inset-0 bg-gray-900 bg-opacity-50 flex items-center justify-center z-50">
    <div class="bg-white p-4 rounded-lg shadow-lg w-11/12 max-w-lg relative">
      <button
          class="absolute top-2 right-2 text-gray-600 hover:text-gray-900"
          @click="$emit('close')"
      >
        &times;
      </button>
      <slot name="content"></slot>
    </div>
  </div>
</template>

<script>
export default {
  name: "ModalContacts",
};
</script>

<style scoped>
body {
  overflow: hidden; /* Prevent scrolling when modal is open */
}
</style>
