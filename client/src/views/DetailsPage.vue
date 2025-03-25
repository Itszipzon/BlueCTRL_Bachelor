<script setup>
import { onMounted, ref, nextTick } from "vue";
import Map from "../components/Map.vue";

const isMapFullscreen = ref(false);
const center = { lat: 62.4722, lng: 6.1495 };
const markers = [{ gpsPosition: { latitude: 62.4722, longitude: 6.1495 } }];

const leafletMapRef = ref(null);

const toggleMapFullscreen = async () => {

  console.log(leafletMapRef);
  isMapFullscreen.value = !isMapFullscreen.value;

  await nextTick();
  if (leafletMapRef.value?.leafletObject) {
    leafletMapRef.value.leafletObject.mapObject.invalidateSize();
  }
};


</script>


<template>
  <div class="page-container">
    <div class="content-container">
      <h1 class="header">🇳🇴 Boat name</h1>
      <div class="top-container">
        <div class="top-container-element"></div>
        <div class="top-container-element"></div>

        <div :class="['top-container-element', { fullscreen: isMapFullscreen }]">
          <!-- Toggle Button -->
          <button class="fullscreen-btn" @click="toggleMapFullscreen">
            {{ isMapFullscreen ? 'Exit Fullscreen' : 'Fullscreen' }}
          </button>

          <!-- Map Component -->
          <Map ref="leafletMapRef" :center="center" :markers="markers" :large="isMapFullscreen" :resize="toggleMapFullscreen" />
        </div>
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
  gap: 20px;
}

.top-container {
  display: flex;
  justify-content: center;
  width: 100%;
  justify-content: space-between;
  margin-top: -10px;
}
.top-container-element {
  background-color: #ffffff;
  width: calc(1064px - 222px * 3);
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


.top-container-element.fullscreen {
  position: fixed;
  top: 50px;
  left: 75px;
  z-index: 2000;
  width: calc(100vw - 75px);
  height: calc(100vh - 50px);
  background: white;
  display: flex;
  flex-direction: column;
}

.top-container-element.fullscreen .leaflet-container {
  width: 100% !important;
  height: 100% !important;
}


.fullscreen-btn {
  position: absolute;
  top: 10px;
  right: 10px;
  z-index: 2100;
  background-color: #114155;
  color: white;
  border: none;
  padding: 8px 12px;
  border-radius: 6px;
  font-size: 14px;
  cursor: pointer;
}


@media (max-width: 1341px) {
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
