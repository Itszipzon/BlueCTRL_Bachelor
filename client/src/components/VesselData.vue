<script setup>
import { ref, watch } from "vue";
import axios from "axios";
import WeatherStatus from "./WeatherStatus.vue";

/**
 * Reactive variable to store weather data fetched from the Open-Meteo API.
 * @type {import('vue').Ref<null | object>}
 */
const weatherData = ref(null);

/**
 * Props passed into the component.
 * @type {{
 *   selectedMarker?: {
 *     id?: string|number,
 *     vesselName?: string,
 *     gpsPosition?: { latitude?: number, longitude?: number },
 *     countryCode?: string
 *   }
 * }}
 */
const props = defineProps({
  selectedMarker: {
    type: Object,
    required: false,
  },
});

/**
 * Watches for changes in the selected marker's GPS position.
 * If valid latitude and longitude are found, it fetches weather data.
 */
watch(
  () => props.selectedMarker?.gpsPosition,
  /**
   * Fetches weather data from the Open-Meteo API based on provided GPS coordinates.
   * @param {{ latitude?: number, longitude?: number }} position - GPS position from the selected marker.
   * @returns {Promise<void>}
   */
  async (position) => {
    if (position?.latitude && position?.longitude) {
      try {
        const response = await axios.get(
          "https://api.open-meteo.com/v1/forecast",
          {
            params: {
              latitude: position.latitude,
              longitude: position.longitude,
              current: "temperature_2m,weather_code",
              daily: "weather_code,temperature_2m_max,temperature_2m_min",
              timezone: "Europe/Berlin",
            },
          }
        );

        weatherData.value = response.data;
      } catch (error) {
        console.error("Failed to fetch weather:", error);
        weatherData.value = null;
      }
    }
  },
  { immediate: true }
);

/**
 * Returns the lowercase version of the given country code, or a placeholder if not provided.
 * Used for building flag image URLs.
 *
 * @param {string | undefined} countryCode - The ISO country code (e.g., "DE", "US").
 * @returns {string} Lowercase country code or "placeholder".
 */
function getCountryCode(countryCode) {
  if (countryCode) {
    return countryCode.toLowerCase();
  } else {
    return "placeholder";
  }
}
</script>

<template>
  <div class="vessel-data-hero">
    <div class="vessel-data-header">
      <img
        :src="`${
          selectedMarker?.countryCode
            ? `https://flagcdn.com/h40/${getCountryCode(
                selectedMarker?.countryCode
              )}.png`
            : `https://placehold.co/40x40/ffffff/ffffff`
        }`"
      />
      <h3>{{ selectedMarker?.vesselName }}</h3>
    </div>
    <div class="vessel-data-box">
      <div class="vessel-data-inner-box">
        <div class="vessel-data-container">
          <h3>Vessel info</h3>
          <div class="vessel-data-sub-container-values">
            <p>ID:</p>
            <p>{{ selectedMarker?.id }}</p>
          </div>
          <div class="vessel-data-sub-container-values">
            <p>LATITUDE:</p>
            <p>{{ selectedMarker?.gpsPosition?.latitude }}</p>
          </div>
          <div class="vessel-data-sub-container-values">
            <p>LONGITUDE:</p>
            <p>{{ selectedMarker?.gpsPosition?.longitude }}</p>
          </div>
        </div>
      </div>

      <div class="vessel-data-inner-box">
        <div class="weather-data-container">
          <h3>Weather at location</h3>

          <div v-if="weatherData?.current">
            <div class="vessel-data-sub-container-values">
              <p>Temperature now:</p>
              <p>{{ weatherData.current.temperature_2m }}°C</p>
            </div>
            <div class="vessel-data-sub-container-values">
              <p>Weather:</p>
              <WeatherStatus
                :weatherCode="weatherData.current.weather_code"
                iconSize="32px"
              />
            </div>
          </div>

          <div v-else>
            <p>Loading weather data...</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.vessel-data-hero {
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.vessel-data-box {
  width: 100%;
  height: 100%;

  display: flex;
  box-sizing: border-box;
}

.vessel-data-inner-box {
  width: 100%;
  border-right: 0.5px solid #ccc;
  border-left: 0.5px solid #ccc;
}

.vessel-data-header {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  gap: 10px;
  color: black;
}

.vessel-data-header h3 {
  font-size: 16px;
  font-weight: bolder;
  font-family: "Poppins", sans-serif;
}
.vessel-data-header img {
  width: 40px;
  height: 32px;
  border-radius: 6px;
}

.vessel-data-container {
  width: 100%;
  display: flex;
  flex-wrap: wrap;
  margin-left: 10px;
  margin-right: 10px;
}
.weather-data-container {
  margin-left: 10px;
  margin-right: 10px;
}

.vessel-data-container h3 {
  margin: 0;
  padding: 8px 0;
  color: #464646;
}

.vessel-data-sub-container-values {
  display: flex;
  justify-content: flex-start;
  width: 100%;
}

.vessel-data-sub-container-values p {
  margin: 0;
  padding: 5px 0;
}

.vessel-data-sub-container-values p:first-child {
  min-width: 170px;
  display: flex;
  justify-content: flex-start;
  color: #4d4d4d;
  font-weight: bold;
}

.vessel-data-sub-container-values p:last-child {
  color: black;
  font-weight: normal;
  width: 100%;
  display: flex;
  justify-content: flex-start;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

@media (max-width: 950px) {
  .vessel-data-box {
    flex-direction: column;
  }

  .vessel-data-inner-box {
    width: 100%;
    border-top: 0.5px solid #ccc;
    border-bottom: 0.5px solid #ccc;
    border-right: none;
    border-left: none;
  }
}
</style>
