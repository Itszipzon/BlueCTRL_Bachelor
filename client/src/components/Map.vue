<script setup>
import { ref, onMounted } from "vue";
import { LMap, LTileLayer, LMarker } from "vue2-leaflet";
import L from "leaflet";
import "leaflet/dist/leaflet.css";
import MapMarker from '../assets/icons/boatmarker.svg';

const props = defineProps({
  center: Object,
  markers: Array
});

const leafletMap = ref(null);
defineExpose({ leafletObject: leafletMap });

onMounted(() => {
  // Optional: zoom fix on mount
  leafletMap.value?.mapObject?.invalidateSize();
});

const createCustomIcon = () => {
  return L.divIcon({
    className: "custom-icon",
    html: `<img src="${MapMarker}" alt="Marker Icon" style="width: 40px; height: 40px;" />`,
    iconSize: [40, 40],
    iconAnchor: [20, 20],
    popupAnchor: [0, -20],
  });
};
</script>

<template>
  <l-map
    ref="leafletMap"
    :center="[center.lat, center.lng]"
    :zoom="13"
    style="height: 100%; width: 100%"
  >
    <l-tile-layer
      url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
      attribution="Map data © OpenStreetMap contributors"
    />
    <l-marker
      v-for="(marker, index) in markers"
      :key="index"
      :lat-lng="[marker.gpsPosition.latitude, marker.gpsPosition.longitude]"
      :icon="createCustomIcon()"
    />
  </l-map>
</template>


<style scoped>
.custom-icon img {
  display: block;
  width: 100%;
  height: auto;
}
</style>
