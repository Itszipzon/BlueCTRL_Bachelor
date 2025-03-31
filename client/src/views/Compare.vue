<script setup>
import Map from "../components/Map.vue";
import { ref } from "vue";
import VesselData from "../components/VesselData.vue";
import LeftContainer from "../components/compare/LeftContainer.vue"
import RightContainer from "../components/compare/RightContainer.vue"

const center = ref({
  lat: 62.47225,
  lng: 6.15492,
});
</script>

<template>
  <div class="main-container">
    <div class="top-container">
      <div class="map-container">
        <Map :center="center" />
      </div>
      <div class="bottom-container">
        <div class="left-container">
          <LeftContainer />
        </div>
        <div class="right-container">
          <RightContainer />
        </div>
      </div>
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
  display: flex;
  flex-direction: column;
}

.map-container {
  height: 300px;
  width: 100%;
}

.bottom-container {
  height: calc(100dvh - 50px - 300px);
  width: 100%;
  display: flex;
  flex-direction: grid;
  background-color: #f5f5f5;
  overflow: auto;
}

.left-container {
  background-color: aquamarine;
  width: 100%;
}

.right-container {
  background-color: bisque;
  width: 100%;
}
</style>
