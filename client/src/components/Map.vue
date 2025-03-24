<template>
  <l-map
    :center="[center.lat, center.lng]"
    :zoom="13"
    style="height: 100%; width: 100%"
    ref="leafletMap"
    @ready="onMapReady"
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
      @click="onMarkerClick(marker)"
    >
    </l-marker>
  </l-map>
</template>

<script>
import { LMap, LTileLayer, LMarker, LPopup } from "vue2-leaflet";
import "leaflet/dist/leaflet.css";
import L from "leaflet";
import MapMarker from "../assets/icons/boatmarker.vue";

export default {
  props: {
    large: Boolean,
    markers: Array,
    center: Object,
    resize: Function,
    setValues: Function,
  },
  components: {
    LMap,
    LTileLayer,
    LMarker,
    LPopup,
  },
  methods: {
    onMapReady() {
      this.$emit("map-ready", this.$refs.leafletMap.mapObject);
    },

    createCustomIcon() {
      return L.divIcon({
        className: "custom-icon",
        html: `<img src="${MapMarker}" alt="Marker Icon" style="width: 40px; height: 40px;" />`,
        iconSize: [40, 40],
        iconAnchor: [20, 20],
        popupAnchor: [0, -20],
      });
    },

    onMarkerClick(marker) {
      if (this.large) {
        this.resize();
        setTimeout(() => {
          this.setValues(marker);
        }, 500);
      } else {
        this.setValues(marker);
      }
    },
  },
};
</script>

<style scoped>
.custom-icon img {
  display: block;
  width: 100%;
  height: auto;
}
</style>
