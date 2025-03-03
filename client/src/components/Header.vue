<template>
  <div class="header-container">
    <div class="topbar"></div>
    <div class="sidebar"></div>
  </div>
</template>

<script setup>
</script>

<style scoped>
.topbar {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 75px;
  background-color: #114155;
  z-index: 1001;
}

.sidebar {
  position: fixed;
  top: 0;
  left: 0;
  width: 75px;
  height: 100vh;
  background-color: #114155;
  z-index: 1001;
}
</style>