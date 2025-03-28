<script setup>
import Map from "../components/Map.vue";
import { ref } from "vue";

const center = ref({
  lat: 62.47225,
  lng: 6.15492,
});
</script>

<template>
  <div class="main-container">
    <div class="top-container">
      <div class="map">
        <Map :center="center" />
      </div>
      <div class="bottom-container"></div>
    </div>
  </div>
</template>

<style scoped>
.main-container {
  height: calc(100vh - 50px);
  display: flex;
  flex-direction: column;
}

.top-container {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.map {
  height: 50vh;
  width: 100%;
}

.bottom-container {
  height: 100%;
  width: 100%;
  background-color: #f5f5f5;
  overflow: auto;
}
</style>
