<script setup>
import { ref } from "vue";

/**
 * Reactive flag to control display of weather status text.
 * @type {import('vue').Ref<boolean>}
 */
const displayStatus = ref(false);

/**
 * Props passed to the component.
 * @type {{ weatherCode: number, iconSize?: string }}
 */
const props = defineProps({
  weatherCode: Number,
  iconSize: {
    type: String,
    default: "2em",
  },
});

/**
 * Returns the icon class name for a given weather code.
 * @param {number} code - Weather code.
 * @returns {string} Icon class for the weather.
 */
function getIconClass(code) {
  if (code === 0) return "wi-day-sunny";
  if ([1, 2, 3].includes(code)) return "wi-day-cloudy";
  if ([45, 48].includes(code)) return "wi-day-fog";
  if ([51, 53, 55].includes(code)) return "wi-day-sleet";
  if ([61, 63, 65].includes(code)) return "wi-rain";
  if ([71, 73, 75].includes(code)) return "wi-snow";
  if ([95, 96, 99].includes(code)) return "wi-day-storm-showers";
  return "wi-day-sunny";
}

/**
 * Returns the weather status description for a given weather code.
 * @param {number} code - Weather code.
 * @returns {string} Human-readable weather status.
 */
function getWeatherStatus(code) {
  if (code === 0) return "Sunny";
  if ([1, 2, 3].includes(code)) return "Cloudy";
  if ([45, 48].includes(code)) return "Foggy";
  if ([51, 53, 55].includes(code)) return "Sleet";
  if ([61, 63, 65].includes(code)) return "Rainy";
  if ([71, 73, 75].includes(code)) return "Snowy";
  if ([95, 96, 99].includes(code)) return "Stormy";
  return "Unknown Weather";
}

/**
 * Handler to show the weather status text on mouse over.
 */
const mouseOver = () => {
  displayStatus.value = true;
};

/**
 * Handler to hide the weather status text on mouse leave.
 */
const mouseLeave = () => {
  displayStatus.value = false;
};
</script>

<template>
  <div class="weatherStatus" @mouseover="mouseOver" @mouseleave="mouseLeave">
    <p :class="[{ show: true }]">{{ getWeatherStatus(weatherCode) }}</p>
    <i class="wi" :class="getIconClass(weatherCode)" :style="{ fontSize: iconSize }"></i>
  </div>
</template>

<style scoped>
.weatherStatus {
  color: black;
}

.weatherStatus p {
  display: none;
}

.weatherStatus p.show {
  display: block;
  position: absolute;
  top: 0;
  right: 50px;
  padding: 5px;
  border-radius: 5px;
}

.wi-day-sunny {
  color: gold;
}

.wi-day-cloudy {
  color: #888;
}

.wi-rain {
  color: #0077be;
}

.wi-snow {
  color: #7fdbff;
}

.wi-day-storm-showers {
  color: #b22222;
}

.wi-day-fog {
  color: #999;
}
</style>
