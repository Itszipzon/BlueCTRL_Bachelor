<script setup>
const props = defineProps({
  selectedMarker: {
    type: Object,
    required: false,
  },
});

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
    <div class="vessel-data-box">
      <div class="vessel-data-inner-box">
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
    </div>
    <div class="vessel-data-box"></div>
    <div class="vessel-data-box"></div>
  </div>
</template>

<style scoped>
.vessel-data-hero {
  width: 100%;
  display: flex;
}

.vessel-data-box {
  width: 100%;
  border-right: 0.5px solid #ccc;
  border-left: 0.5px solid #ccc;
}

.vessel-data-inner-box {
  height: 100%;
  margin-right: 10px;
  margin-left: 10px;
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
  background-color: #fff;
  display: flex;
  flex-wrap: wrap;
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
  color: #005380;
  font-weight: normal;
  width: 100%;
  display: flex;
  justify-content: flex-start;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
