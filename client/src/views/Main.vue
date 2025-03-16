<script setup>
import { ref, onMounted, onUnmounted  } from 'vue';
import Map from '../components/Map.vue';

const markers = ref([]);
const selectedMarker = ref({});

const largeMap = ref(true);
let mapObject = null;

const center = ref({
  lat: 62.47225,
  lng: 6.15492,
});

const resizeStyle = ref(getResizeStyle());

function getResizeStyle() {
  return window.innerWidth < 1615 && !largeMap.value
    ? { right: (window.innerWidth / 2) - 75 - (750/2 - 15/2 - 35) + "px" }
    : {};
}

function toggleMapSize() {
  largeMap.value = !largeMap.value;

  handleResize();
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
  console.log(mapObject.getBounds());
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
    zoomInMap(15);
  }, 250);
  setCenter(values.gpsPosition.latitude, values.gpsPosition.longitude);
}

function handleResize() {
  resizeStyle.value = getResizeStyle();
}

onMounted(() => {
  window.addEventListener('resize', handleResize);
});

onUnmounted(() => {
  window.removeEventListener('resize', handleResize);
});
</script>

<template>
  <div class="main-container">
    <Map :large="largeMap" :setValues="setValues" :resize="toggleMapSize" :markers="markers" :center="center"
    @map-ready="onMapReady" />
  </div>
</template>

<style scoped>
.main-container {
  height: 100vh;
  width: 100%;
}
</style>
