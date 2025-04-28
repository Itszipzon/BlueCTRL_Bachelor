<script setup>
import { ref, onMounted, onUnmounted, inject, watchEffect } from 'vue';
import Map from '../components/Map.vue';
import VesselData from '../components/VesselData.vue';
import TankData from '../components/TankData.vue';
import BoatTilt from '../components/BoatTilt.vue';
import Fullscreen from '../components/icons/Fullscreen.svg';
import Smallscreen from '../components/icons/Smallscreen.svg';

const boats = inject('boats');
const vessels = ref(null);
const loadingVessels = ref(true);
const selectedMarker = ref(null);

watchEffect(() => {
  vessels.value = boats.value.vessels;
  loadingVessels.value = boats.value.loadingVessels;
})

const largeMap = ref(true);
let mapObject = null;

const center = ref({
  lat: 62.47225,
  lng: 6.15492,
});

const resizeStyle = ref(getResizeStyle());

function getResizeStyle() {
  return window.innerWidth < 1615 && !largeMap.value
    ? { right: (window.innerWidth / 2) - 75 - (750 / 2 - 15 / 2 - 35) + "px" }
    : {};
}

function toggleMapSize() {
  largeMap.value = !largeMap.value;

  if (mapObject) {
    const currentCenter = mapObject.getCenter();

    setTimeout(() => {
      mapObject.invalidateSize();
      mapObject.setView(currentCenter, mapObject.getZoom());
    }, 300);
  }
}

function onMapReady(map) {
  mapObject = map;
}

function setCenter(lat, lng) {
  center.value = { lat, lng };
}

function zoomInMap(value) {
  mapObject.setZoom(value);
}

function setValues(values) {

  selectedMarker.value = values;
  setTimeout(() => {
    zoomInMap(10);
  }, 250);
  setCenter(values.gpsPosition.latitude, values.gpsPosition.longitude);
}

function handleResize() {
  if (!largeMap.value && selectedMarker.value) {
    toggleMapSize();
    zoomInMap(5)
  }
}


onMounted(() => {
  window.addEventListener('resizeMap', handleResize);
});

onUnmounted(() => {
  window.removeEventListener('resizeMap');
  if (mapObject) {
    mapObject.off();
  }
});

</script>

<template>
  <div class="main-container">
    <div :class="['loading-vessels', { display: loadingVessels }]">
      <div class="loading-vessels-text">Loading vessels...</div>
    </div>
    <div v-if="selectedMarker?.vesselName" :class="['resize-button', { small: !largeMap }]" :style="resizeStyle"
      @click="toggleMapSize">
      <img :src="Fullscreen" v-if="!largeMap" />
      <img :src="Smallscreen" v-else />
    </div>
    <div :class="['top-container', { large: largeMap }]">
      <div :class="['marker-vessel-tilt-container', { small: !largeMap }]"
        :style="selectedMarker?.vesselName ? {} : { height: '0px', padding: '0px', margin: '0px' }">
        <div class="marker-vessel-tilt">
          <BoatTilt type="roll" :vesselId="selectedMarker?.id" />
        </div>
        <div class="marker-vessel-tilt">
          <BoatTilt type="pitch" :vesselId="selectedMarker?.id" />
        </div>
      </div>
      <div :class="['map', { small: !largeMap }]">
        <Map :large="largeMap" :setValues="setValues" :resize="toggleMapSize" :markers="vessels" :center="center"
          @map-ready="onMapReady" />
      </div>
    </div>
    <div :class="['vessel-data-container', { small: !largeMap }]">
      <VesselData :selectedMarker="selectedMarker" />
    </div>
    <div :class="['vessel-data-container', { small: !largeMap }]">
      <TankData />
    </div>
  </div>
</template>

<style scoped>
.main-container {
  display: flex;
  flex-direction: column;
  align-items: space-between;
}

.loading-vessels {
  position: fixed;
  height: 100px;
  width: 250px;
  top: calc(50% - 50px);
  left: calc(50% - 125px);
  background-color: #fff;
  z-index: 100000;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  color: black;
  display: none;

}

.loading-vessels.display {
  display: flex;
  align-items: center;
  justify-content: center;
}

.resize-button {
  height: fit-content;
  width: fit-content;
  position: absolute;
  top: 10px;
  right: 10px;
  font-size: 20px;
  cursor: pointer;
  background-color: white;
  border-radius: 2px;
  box-shadow: 0 0 0 2px rgba(0, 0, 0, 0.2);
  z-index: 1000;
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  transition: top 0.5s ease, right 0.5s ease;
  color: black;
}

.resize-button.small {
  top: 30px;
  right: 30px;
}

.resize-button img {
  width: 30px;
  height: 30px;
}

.top-container {
  display: flex;
  flex-direction: row;
  justify-content: space-between;

}

.marker-vessel-tilt-container {
  background: #ffff;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  margin: 0;
  box-sizing: border-box;
  transition: width 0.5s ease, margin 0.5s ease, padding 0.5s ease;
  overflow: hidden;
  width: 0;
}

.marker-vessel-tilt-container.small {
  margin: 20px 20px 0 20px;
  width: calc(50% - 10px);
  height: calc((50vw - 75px - 20px)/2 - 10px);
  padding: 20px;
}

.marker-vessel-tilt {
  width: calc(50% - 10px);
  height: 100%;
  background: #f9f9f9;
  border-radius: 6px;
  border: 1px solid #ddd;
}

.map {
  transition: all 0.5s ease;
  background-color: #f0f0f0;
  box-sizing: border-box;
  height: calc(100dvh - 50px);
  width: calc(100dvw - 75px);
}

.map.small {
  margin: 20px 20px 0 0;
  height: calc((50vw - 75px - 20px) / 2 - 10px);
  width: calc(50% - 10px);
}

.vessel-data-container {
  height: 0;
  margin: 0;
  padding: 0;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  transition: height 0.5s ease, margin 0.5s ease, padding 0.5s ease;
  overflow: hidden;
  text-wrap: none;
}

.vessel-data-container.small {
  margin: 20px 20px 0 20px;
  padding: 20px;
  height: fit-content;
}

.vessel-data-container.small:last-child {
  margin-bottom: 20px;
}

@media (max-width: 800px) {
  .top-container {
    align-items: center;
    flex-direction: column;
  }

  .marker-vessel-tilt-container.small {
    margin: 20px 20px 0 20px;
    width: calc(94% - 10px);
    height: calc((94vw - 75px - 20px)/2 - 10px);
    padding: 20px;
  }

  .map {
    height: calc(100dvh - 50px);
    width: calc(100dvw - 75px);
    margin: 0;
    padding: 0;
  }

  .map.small {
    margin: 20px 0px 0 0;
    height: calc((94vw - 75px - 20px) / 2 - 10px);
    width: calc(94% - 10px);
  }

  .resize-button.small {
    top: calc((94vw - 75px - 20px) / 2 - 10px + 50px);
    left: calc(94% - 25px);
  }

}
</style>
