<script setup></script>

<template>
  <div class="page-container">
    <h1 class="header">🇳🇴 Boat name</h1>
    <div class="content-container">
      <div class="top-container">
        <div class="boat-tilt"></div>
        <div class="boat-slope"></div>
        <div class="mini-map"></div>
      </div>
      <div class="middle-container">
        <div class="data-box"></div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.page-container {
}
.header {
  font-size: x-large;
  color: #000;
}
.content-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.top-container {
  display: flex;
  justify-content: center;
  gap: 26px;
}
.middle-container {
  display: flex;
  justify-content: center;
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

.data-box {
  width: 1251px;
  height: 287px;
  background-color: #ffffff;
  margin-top: 26px;
}
</style>
