<script setup>
import BarChart from "../BarChart.vue";

const props = defineProps({
  selectedBoats: Array,
});
</script>

<template>
  <div class="right-container-component">
    <div class="charts-container">
      <div class="traveldistance-chart">
        <h3>Travel Distance</h3>
        <div v-if="selectedBoats && selectedBoats.length > 0">
          <BarChart :vessels="selectedBoats" />
        </div>
        <div v-else class="empty-chart">
          <p>No boats selected</p>
          <BarChart :vessels="selectedBoats" />
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.right-container-component {
  padding: 20px;
  width: 100%;
  height: 100%;
  color: black;
  display: flex;
  flex-direction: column;
}

.charts-container {
  background-color: white;
  padding: 20px;
  border-radius: 6px;
}

.traveldistance-chart {
  position: relative;
}

.traveldistance-chart h3 {
  font-size: 16px;
  font-weight: bolder;
  font-family: "Poppins", sans-serif;
}

.empty-chart {
  position: relative;
}

.empty-chart p {
  position: absolute;
  top: 40%;
  left: 50%;
  transform: translate(-50%, -50%);
  text-align: center;
  z-index: 1;
}
</style>
