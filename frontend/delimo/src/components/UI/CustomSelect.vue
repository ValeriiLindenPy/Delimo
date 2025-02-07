<template>
  <div class="custom-select-wrapper relative inline-block">
    <div
        class="custom-select-display flex justify-center items-center gap-2 bg-st3 text-white font-bold text-xl rounded-lg p-2 cursor-pointer"
        @click="toggleDropdown"
    >
      <p>{{ selectedCityLabel }}</p>
      <i class="fa-solid fa-chevron-down"></i>
    </div>
    <ul
        v-if="dropdownOpen"
        class="custom-select-options absolute z-10 bg-st3 text-white font-bold text-xl rounded-lg mt-1 max-h-60 overflow-auto"
    >
      <li
          class="p-2 hover:bg-st4 cursor-pointer"
          @click="selectCity('')"
      >
        Svi gradovi
      </li>
      <li
          v-for="cityItem in cities"
          :key="cityItem.id"
          class="p-2 hover:bg-st4 cursor-pointer"
          @click="selectCity(cityItem.name)"
      >
        {{ cityItem.name }}
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  name: 'CustomSelect',
  props: {
    cities: {
      type: Array,
      required: true,
    },
    modelValue: { // Vue 3 `v-model`
      type: String,
      default: '',
    },
  },
  data() {
    return {
      dropdownOpen: false,
    };
  },
  computed: {
    selectedCityLabel() {
      return this.modelValue || 'Svi gradovi';
    },
  },
  methods: {
    toggleDropdown() {
      this.dropdownOpen = !this.dropdownOpen;
    },
    selectCity(city) {
      this.$emit('update:modelValue', city); // Vue 3 `v-model`
      this.dropdownOpen = false;
    },
  },
};
</script>
