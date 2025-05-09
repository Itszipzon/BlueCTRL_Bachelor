<script setup>
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
</script>

<template>
  <i
    class="wi"
    :class="getIconClass(weatherCode)"
    :style="{ fontSize: iconSize }"
  ></i>
</template>

<style scoped>
.wi-day-sunny {
  color: gold;
}
.cloudy {
  color: #888;
}
.rain {
  color: #0077be;
}
.snow {
  color: #7fdbff;
}
.storm {
  color: #b22222;
}
.foggy {
  color: #999;
}
</style>
