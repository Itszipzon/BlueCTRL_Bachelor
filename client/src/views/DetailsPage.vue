<script setup></script>

<template>
  <div class="page-container">
    <h1 class="header">🇳🇴 Boat name</h1>
    <div class="content-container">
      <div class="boat-tilt"></div>
      <div class="boat-slope"></div>
      <div class="mini-map"></div>
    </div>
  </div>
</template>

<style scoped>
.header {
  font-size: x-large;
  color: #000;
}

.content-container {
  display: flex;
  justify-content: center;
  gap: 20px;
}

.boat-tilt {
    background-color: #ffffff;
    width: 400px;
    height: 287px;
}
.boat-slope {
    background-color: #ffffff;
    width: 400px;
    height: 287px;
}
.mini-map {
    background-color: #ffffff;
    width: 400px;
    height: 287px;
}
</style>
