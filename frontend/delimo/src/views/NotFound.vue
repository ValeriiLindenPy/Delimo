<template>
  <!-- Контейнер занимает всю высоту экрана и центрирует содержимое по вертикали и горизонтали -->
  <div class="min-h-screen flex flex-col items-center justify-center">
    <!-- Группа с текстом и иконкой, увеличенный размер шрифта -->
    <div class="flex items-center gap-2 text-8xl font-extrabold">
      <h1 class="text-st5">4</h1>
      <i class="fa-solid fa-arrows-spin text-st3" aria-hidden="true"></i>
      <h1 class="text-st5">4</h1>
    </div>
    <h2 class="text-st5 text-6xlxl">Not Found</h2>
  </div>
</template>

<script>
export default {
  name: 'NotFound',
};
</script>
