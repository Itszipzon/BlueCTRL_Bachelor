<script setup></script>

<template>
  <div class="page-container">
    <div class="content-container">
      <h1 class="header">🇳🇴 Boat name</h1>
      <div class="top-container">
        <div class="top-container-element"></div>
        <div class="top-container-element"></div>
        <div class="top-container-element"></div>
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
  display: flex;
  justify-content: center;
  margin: 10px;
}

.header {
  font-size: x-large;
  color: #000;
}
.content-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  width: 1256px;
  height: 100%;
  gap: 26px;
}

.top-container {
  display: flex;
  justify-content: center;
  width: 100%;
  justify-content: space-between;
  gap: 10px;
}
.top-container-element {
  background-color: #ffffff;
  width: calc(1064px - 225px * 3);
  height: calc(1064px - 250px * 3);
}

.middle-container {
  display: flex;
  justify-content: center;
  width: 100%;
}

.data-box {
  background-color: #ffffff;
  width: 100%;
  height: 262px;
}

.bottom-container {
  display: flex;
  justify-content: center;
  width: 100%;
}

.tankdata-box {
  background-color: #ffffff;
  width: 100%;
  height: 262px;
}


@media (max-width: 1280px){
  .content-container {
    max-width: 100%;
  }
}

.top-containter {
  display: flex;
  flex-wrap: wrap;
  gap: 5px;
}

.data-box,
.tankdata-box {
  height: 200px; 
}


@media (max-width: 900px) {
  .content-container {
    max-width: 100%;
  }
  .top-container {
    display: grid;
    justify-content: center;
  }
}


</style>
