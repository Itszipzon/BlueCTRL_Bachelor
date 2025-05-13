<script setup>
import { ref } from "vue";

const displayStatus = ref(false);

const props = defineProps({
  weatherCode: Number,
  iconSize: {
    type: String,
    default: "2em",
  },
});

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

const mouseOver = () => {
  displayStatus.value = true;
};

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
