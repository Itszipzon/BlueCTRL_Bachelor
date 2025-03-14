<script setup>
import Header from "./components/Header.vue";
</script>

<template>
  <div id="app" class="app">
    <Header />
    <div class="mainPage">
      <router-view />
    </div>
  </div>
</template>

<style scoped>
#app {
  padding: 0;
  margin: 0;
}

.mainPage {
  /* height: calc(100dvh - 75px); */
  height: 100dvh;
  width: calc(100dvw - 75px);
  /* margin-top: 75px; */
  margin-left: 75px;
  background-color: #e8eff2;
  overflow-y: auto;
}

@media (min-width: 1024px) {
}
</style>
