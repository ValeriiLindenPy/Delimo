<template>
  <div class="relative w-full max-w-md mx-auto h-64 overflow-hidden">
    <!-- Image -->
    <img
        :src="images[currentIndex] || defaultImage"
        alt="Carousel image"
        class="w-full h-full object-cover rounded shadow"
        @click="emit('image-clicked', images[currentIndex] || defaultImage)"
    />

    <!-- Navigation Buttons -->
    <button
        class="absolute left-0 top-1/2 -translate-y-1/2 bg-black/30 text-white px-3 py-1 rounded-r"
        @click="prevImage"
    >
      &lt;
    </button>
    <button
        class="absolute right-0 top-1/2 -translate-y-1/2 bg-black/30 text-white px-3 py-1 rounded-l"
        @click="nextImage"
    >
      &gt;
    </button>

    <!-- Dots for navigation -->
    <div class="absolute bottom-2 left-1/2 -translate-x-1/2 flex space-x-2">
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

const emit = defineEmits(['image-clicked']);

// Props
const props = defineProps({
  images: {
    type: Array,
    default: () => [],
  },
  defaultImage: {
    type: String,
    required: true,
  },
})

// Reactive state for current index
const currentIndex = ref(0)

// Methods to navigate carousel
function nextImage() {
  if (props.images.length === 0) return // No images to navigate
  currentIndex.value =
      currentIndex.value === props.images.length - 1 ? 0 : currentIndex.value + 1
}

function prevImage() {
  if (props.images.length === 0) return // No images to navigate
  currentIndex.value =
      currentIndex.value === 0 ? props.images.length - 1 : currentIndex.value - 1
}

function goToSlide(index) {
  currentIndex.value = index
}
</script>

<style scoped>
/* Add styles for active/inactive dots */
.bg-st4 {
  background-color: #4a5568; /* Active dot */
}
.bg-st2 {
  background-color: #cbd5e0; /* Inactive dot */
}
</style>
