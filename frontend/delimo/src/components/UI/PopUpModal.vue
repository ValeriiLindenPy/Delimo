

<template>
<Transition>
  <div v-show="isActive" class="flex inset-0 bg-black bg-opacity-50 justify-center items-center z-[9999] h-screen w-screen fixed top-0 left-0">
    <div v-show="isActive" class="flex flex-col bg-white p-5 rounded-lg w-2/3 md:w-1/3">
      <div class="cursor-pointer flex justify-end">
        <i @click="close" class="fa-solid fa-xmark text-2xl"></i>
      </div>

      <slot/>
    </div>
  </div>
</Transition>
</template>

<script>
export default {
  name: "PopUpModal",
  props: {
    isActive: {
      type: Boolean,
      required: true,
    }
  },
  setup(props, { emit }) {
    const close = () => {
      emit('close');
    }
    return {
      close
    }
  }
}
</script>