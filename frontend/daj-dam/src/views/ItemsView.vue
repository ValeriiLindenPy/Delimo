<template>
  <PostList :posts="posts" />
</template>

<script>
import PostList from '../components/PostList.vue';
export default {
  name: 'ItemsView',
  components: {PostList},
  data() {
    return {
      posts: [
        {
          id: 1,
          name: "Električna bušilica stayer",
          description: "Električna bušilica sa ergonomskim dizajnom — idealan alat za bušenje drveta, metala i " +
              "drugih materijala. Pogodna za kućne poslove i lakše profesionalne zadatke. " +
              "Radi na struju, opremljena je podesivom brzinom okretanja i funkcijom reverza.",
          available: true,
          maxPeriodDays: 3,
          image: "https://i.ibb.co/zHNgfhB/44819-750x0.jpg",
          pricePerDay: 100,
        },
        {
          id: 2,
          name: "Električna bušilica stayer",
          description: "Električna bušilica sa ergonomskim dizajnom — idealan alat za bušenje drveta, metala i " +
              "drugih materijala. Pogodna za kućne poslove i lakše profesionalne zadatke. " +
              "Radi na struju, opremljena je podesivom brzinom okretanja i funkcijom reverza.",
          available: true,
          maxPeriodDays: 2,
          image: "https://i.ibb.co/zHNgfhB/44819-750x0.jpg",
          pricePerDay: 200,
        },
        {
          id: 3,
          name: "Električna bušilica stayer",
          description: "Električna bušilica sa ergonomskim dizajnom — idealan alat za bušenje drveta, metala i " +
              "drugih materijala. Pogodna za kućne poslove i lakše profesionalne zadatke. " +
              "Radi na struju, opremljena je podesivom brzinom okretanja i funkcijom reverza.",
          available: true,
          maxPeriodDays: 1,
          image: "https://i.ibb.co/zHNgfhB/44819-750x0.jpg",
          pricePerDay: null,
        },
        {
          id: 4,
          name: "Električna bušilica stayer",
          description: "Električna bušilica sa ergonomskim dizajnom — idealan alat za bušenje drveta, metala i " +
              "drugih materijala. Pogodna za kućne poslove i lakše profesionalne zadatke. " +
              "Radi na struju, opremljena je podesivom brzinom okretanja i funkcijom reverza.",
          available: false,
          maxPeriodDays: 1,
          image: "https://i.ibb.co/zHNgfhB/44819-750x0.jpg",
          pricePerDay: null,
        }
      ]
    }
  },
  setup() {
    return {}
  }
}
</script>

<style lang="scss" scoped>

</style>