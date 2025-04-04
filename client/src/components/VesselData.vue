<script setup>

const props = defineProps({
  selectedMarker: {
    type: Object,
    required: false
  }
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
    <div class="vessel-data-header">
      <img
        :src="`${selectedMarker?.countryCode ? `https://flagcdn.com/h40/${getCountryCode(selectedMarker?.countryCode)}.png` : `https://placehold.co/40x40/ffffff/ffffff`}`" />
      <h2>{{ selectedMarker?.vesselName }}</h2>
    </div>
    <div class="vessel-data-container">
      <div class="vessel-data-sub-container">
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
</template>

<style scoped>
.vessel-data-hero {
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 10px;
  padding: 10px 0;
}

.vessel-data-header {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  gap: 10px;
  color: black;

}

.vessel-data-container {
  width: 100%;
  background-color: #fff;
  display: flex;
  flex-wrap: wrap;
}

.vessel-data-sub-container {
  width: 100%;
  padding: 10px 15px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0px 0px 6px rgba(0, 0, 0, 0.2);
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  border-radius: 8px;
  width: 250px;
}

.vessel-data-sub-container h3 {
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
  width: 80px;
  display: flex;
  justify-content: flex-start;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>