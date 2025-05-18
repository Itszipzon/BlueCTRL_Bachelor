<script setup>
import { ref, onMounted, onUnmounted, inject, watchEffect, watch, computed } from 'vue';
import Map from '../components/Map.vue';
import VesselData from '../components/VesselData.vue';
import TankData from '../components/TankData.vue';
import BoatTilt from '../components/BoatTilt.vue';
import Fullscreen from '../assets/icons/Fullscreen.svg';
import Smallscreen from '../assets/icons/Smallscreen.svg';
import HelpIcon from '../components/HelpIcon.vue';
import axios from 'axios';

const boats = inject('boats');
const vessels = ref(null);
const loadingVessels = ref(true);
const dummyData = ref(false);
const selectedMarker = ref(null);
const exagerateValues = ref(false);
const draft = ref(-1);

const mouseX = ref(0);
const mouseY = ref(0);

const mouseAboveDraft = ref(false);

const draftBarStyle = computed(() => {
  return {
    height: draft.value > 0 ? `${draft.value * 10}px` : '1px',
  };
})

const exaggerateValuesText = ref('Exaggerate values');

watchEffect(() => {
  vessels.value = boats.value.vessels;
  loadingVessels.value = boats.value.loadingVessels;
  dummyData.value = boats.value.dummyData;
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

function getDriftId() {
  if (selectedMarker.value.id === 2) { //fpp brukes. App brukes ikke.
    return 25137;
  } else if (selectedMarker.value.id === 24) {
    return 25137;
  } else if (selectedMarker.value.id === 28) { //fwd. Skal være samme som Fpp
    return 98;
  } else if (selectedMarker.value.id === 7234904) {
    return 432;
  } else if (selectedMarker.value.id === 10226293) {
    return 356;
  } else if (selectedMarker.value.id === 11912972) {
    return 446;
  }

  return null;
}

watch(() => selectedMarker.value, (newValue) => {
  if (newValue) {
    const driftId = getDriftId();
    if (driftId) {
      const url = `http://localhost:8080/api/signal?signalId=${driftId}&vesselId=${newValue.id}`;

      axios
        .get(url, {
          headers: {
            Authorization: `Basic ${localStorage.getItem("SESSION")}`,
          },
        })
        .then((r) => {
          if (r.status !== 200) {
            console.error('Error fetching drift data:', r.status);
          }
          console.log('Drift data:', r.data);
          draft.value = 8.39;//r.data.signalEvents[0].doubleValue;
        })
    }
  }
}, { immediate: true });

const mouseOverDraft = (e) => {
  const rect = e.target.getBoundingClientRect();
  const x = e.clientX - rect.left;
  const y = e.clientY - rect.top;

  if (x > 0 && x < rect.width && y > 0 && y < rect.height) {
    mouseAboveDraft.value = true;
  } else {
    mouseAboveDraft.value = false;
  }
}

const handleMouseLeave = () => {
  mouseAboveDraft.value = false;
}

const draftContainerStyle = computed(() => {
  const offset = 10;
  const tooltipWidth = 200;
  const tooltipHeight = 50;

  let left = mouseX.value + offset;
  let top = mouseY.value + offset;

  // Adjust if tooltip would overflow screen
  if (left + tooltipWidth > window.innerWidth) {
    left = mouseX.value - tooltipWidth - offset;
  }
  if (top + tooltipHeight > window.innerHeight) {
    top = mouseY.value - tooltipHeight - offset;
  }

  return {
    left: `${left}px`,
    top: `${top}px`,
  };
});

const handleMouseMove = (event) => {
  mouseX.value = event.clientX;
  mouseY.value = event.clientY;
};

onMounted(() => {
  window.addEventListener('resizeMap', handleResize);
  window.addEventListener("mousemove", handleMouseMove);
});

onUnmounted(() => {
  window.removeEventListener('resizeMap', handleResize);
  window.removeEventListener("mousemove", handleMouseMove);
  if (mapObject) {
    mapObject.off();
  }
});

</script>

<template>
  <div class="main-container">
    <div class="draft-information-container" v-if="mouseAboveDraft" :style="draftContainerStyle">
      <div class="draft-information">
        <p>Draft: {{ draft >= 0 ? draft.toFixed(2) + " Meters" : 'No data' }}</p>
      </div>
    </div>

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
        <div class="marker-vessel-tilt-holder">

          <div class="marker-vessel-tilt">
            <BoatTilt type="roll" :exagerate_values="exagerateValues" :dummyData="dummyData" background-offset="75"
              :vesselId="selectedMarker?.id" />
          </div>
          <div class="marker-vessel-tilt-draft-container">
            <div class="marker-vessel-tilt-draft" @mouseenter="mouseOverDraft" @mouseleave="handleMouseLeave"> <!--Draft, Remove if not happy.-->
              <div class="marker-vessel-tilt-draft-bar" :style="draftBarStyle" />
              <div class="marker-vessel-tilt-draft-line" />
              <div class="marker-vessel-tilt-draft-line" />
              <div class="marker-vessel-tilt-draft-line" />
              <div class="marker-vessel-tilt-draft-line" />
              <div class="marker-vessel-tilt-draft-line" />
              <div class="marker-vessel-tilt-draft-line" />
              <div class="marker-vessel-tilt-draft-line" />
              <div class="marker-vessel-tilt-draft-line" />
              <div class="marker-vessel-tilt-draft-line" />
              <div class="marker-vessel-tilt-draft-line" />
            </div>
            <p>Draft</p>
          </div>
          <div class="marker-vessel-tilt">
            <BoatTilt type="pitch" :exagerate_values="exagerateValues" :dummyData="dummyData" background-offset="75"
              :vesselId="selectedMarker?.id" />
          </div>
        </div>
        <div class="marker-vessel-tilt-exagerate">
          <input type="checkbox" id="exagerate" v-model="exagerateValues" />
          <label for="exagerate">Exagerate values</label>
          <HelpIcon :width="'250px'" :left="'20px'" :top="'-90px'"
            :help-text="'Resize the protractor to show from 0 - 30 instead of 0 - 90 degrees.'" :size="'15px'" />
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
      <TankData :vesselId="selectedMarker?.id" :dummy="dummyData" />
    </div>
  </div>
</template>

<style scoped>
.main-container {
  display: flex;
  flex-direction: column;
  align-items: space-between;
}

.draft-information-container {
  position: fixed;
  z-index: 100000;
  width: 200px;
  height: 50px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  padding: 10px 5px;
  color: black;
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
  flex-direction: column;
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
  height: calc((50vw - 75px - 20px)/2 - 10px - 40px);
  padding: 20px;
}

.marker-vessel-tilt-container button {
  width: fit-content;
  position: absolute;
  bottom: 0px;
  left: 0px;
  border: none;
  background: #005380;
  color: #fff;
  padding: 5px 10px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  z-index: 1001;
}

.marker-vessel-tilt-exagerate {
  display: flex;
  position: absolute;
  bottom: 0px;
  left: 7px;
  justify-content: center;
  align-items: center;
  color: black;
  gap: 5px;
}

.marker-vessel-tilt-container button:hover {
  background: #00406d;
}

.marker-vessel-tilt-holder {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  width: 100%;
  height: 100%;
}

.marker-vessel-tilt {
  width: calc(50% - 50px);
  /* -10px if draft is removed */
  height: 100%;
  background: #f9f9f9;
  border-radius: 6px;
  border: 1px solid #ddd;
}

.marker-vessel-tilt-draft-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  color: black;
}

.marker-vessel-tilt-draft-container p {
  font-weight: 500;
}

.marker-vessel-tilt-draft {
  width: 80px;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
}

.marker-vessel-tilt-draft-line {
  width: 60%;
  height: 2px;
  background: black;
}

.marker-vessel-tilt-draft-line:nth-child(2),
.marker-vessel-tilt-draft-line:nth-child(6),
.marker-vessel-tilt-draft-line:nth-child(11) {
  width: 100%;
}

.marker-vessel-tilt-draft-bar {
  position: absolute;
  bottom: 0px;
  width: 40%;
  background-color: #3498db;
  max-height: 100% !important;
}

.marker-vessel-tilt-draft-bar:hover {
  background-color: #2980b9;
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
  height: calc((50vw - 75px - 20px) / 2 - 10px - 40px);
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

@media (max-width: 950px) {
  .top-container {
    align-items: center;
    flex-direction: column;
    flex-direction: column-reverse;
  }

  .marker-vessel-tilt-container.small {
    margin: 20px 20px 0 20px;
    width: calc(100% - 40px);
    height: calc((100vw - 75px - 20px)/2 - 40px);
    padding: 20px;
  }

  .map.small {
    margin: 20px 0px 0 0;
    height: calc((100vw - 75px - 40px) / 2 - 10px);
    width: calc(100% - 40px);
  }

  @media (max-width: 800px) {
    .marker-vessel-tilt-container.small {
      padding: 6px;
    }

    .marker-vessel-tilt {
      width: calc(50% - 3px);
    }

  }

}
</style>
