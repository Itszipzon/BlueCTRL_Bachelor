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
      <div class="bottom-container">
        <div class="tankdata-box"></div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.page-container {
  padding-bottom: 20px;
  padding-top: 20px;
}

.header {
  font-size: x-large;
  color: #000;
  margin-left: 70px;
}
.content-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  margin-top: 10px;
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

.bottom-container {
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

.tankdata-box {
  width: 1251px;
  height: 287px;
  background-color: #ffffff;
  margin-top: 26px;
}
</style>
