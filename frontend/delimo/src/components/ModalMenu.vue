<template>
  <div v-if="openMenu" class="flex items-center justify-center bg-white">
    <ul class="flex flex-col w-full">
      <li v-for="item in menu" :key="item.id" class="hover:bg-st2 cursor-pointer px-2 py-3 border-st2 border-t-2 border-solid">{{ item.name }}</li>
    </ul>
  </div>
</template>

<script>
export default {
  name: 'ModalMenu',
  data() {
    return {
      menu: [
        {
          id: 1,
          name: 'Daca'
        },
        {
          id: 2,
          name: 'Odrasli'
        },
        {
          id: 3,
          name: 'Kuca'
        },
      ],
    }
  },
  props: {
    openMenu: {
      type: Boolean,
      required: true,
    }
  },
}
</script>
