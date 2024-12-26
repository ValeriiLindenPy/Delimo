<template>
  <div
      v-if="isModalOpen"
      class="absolute top-[65px] right-3 bg-white border border-gray-300 rounded-lg shadow-lg p-4 w-64 z-50"
  >
    <ul v-if="authorized">
      <li>
        <button class="block w-full text-left py-2 px-4 hover:bg-gray-100 rounded-md">
          Moj nalog
        </button>
      </li>
      <li>
        <button class="block w-full text-left py-2 px-4 hover:bg-gray-100 rounded-md">
          Odjavite se
        </button>
      </li>
    </ul>

    <ul v-else>
      <li>
        <button class="block w-full text-left py-2 px-4 hover:bg-gray-100 rounded-md">
          Ulogujte se
        </button>
      </li>
      <li>
        <button class="block w-full text-left py-2 px-4 hover:bg-gray-100 rounded-md">
          Registrujte se
        </button>
      </li>
    </ul>

  </div>
</template>

<script>
export default {
  props: {
    isModalOpen: {
      type: Boolean,
      required: true,
    },
    authorized: {
      type: Boolean,
      required: true,
    },
  },
  methods: {
    closeModal() {
      this.$emit("close");
    },
  },
};
</script>
