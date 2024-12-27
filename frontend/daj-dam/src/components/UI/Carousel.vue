<template>
  <div class="relative w-full max-w-md mx-auto h-64 overflow-hidden">
    <!-- Image -->
    <img
        :src="images[currentIndex]"
        alt="Carousel image"
        class="w-full h-full object-cover rounded shadow"
    />

    <!-- Navigation Buttons -->
    <button
        class="absolute left-0 top-1/2 -translate-y-1/2 bg-black/30 text-white px-3 py-1 rounded-r"
        @click="prevImage"
    >
      <
    </button>
    <button
        class="absolute right-0 top-1/2 -translate-y-1/2 bg-black/30 text-white px-3 py-1 rounded-l"
        @click="nextImage"
    >
      >
    </button>

    <!-- Dots for navigation -->
    <div
        class="absolute bottom-2 left-1/2 -translate-x-1/2 flex space-x-2"
    >
    <span
        v-for="(img, index) in images"
        :key="index"
        class="w-3 h-3 rounded-full cursor-pointer"
        :class="index === currentIndex ? 'bg-st4' : 'bg-st2'"
        @click="goToSlide(index)"
    ></span>
    </div>
  </div>

</template>

<script setup>
import { ref } from 'vue'

/**
 * Accept images as a prop from the parent (ItemDetails).
 * Make sure to handle the case where images might be empty or undefined.
 */
const props = defineProps({
  images: {
    type: Array,
    default: () => [],
  },
})

// Reactive state for the current slide index
const currentIndex = ref(0)

function nextImage() {
  currentIndex.value =
      currentIndex.value === props.images.length - 1 ? 0 : currentIndex.value + 1
}

function prevImage() {
  currentIndex.value =
      currentIndex.value === 0 ? props.images.length - 1 : currentIndex.value - 1
}

function goToSlide(index) {
  currentIndex.value = index
}
</script>

<style scoped>
/* Basic styling for demonstration. Feel free to customize. */
</style>
