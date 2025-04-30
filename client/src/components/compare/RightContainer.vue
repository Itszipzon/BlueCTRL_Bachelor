<script setup>
import BarChart from "../BarChart.vue";
import { ref } from "vue";

const props = defineProps({
  selectedBoats: Array,
});

const selectedTimePeriod = ref("all");
</script>

<template>
  <div class="right-container-component">
    <div class="charts-container">
      <div class="traveldistance-chart">
        <h3>Travel Distance</h3>
        <div class="time-period-selector">
          <label for="time-period">Select Time Period:</label>
          <select id="time-period" v-model="selectedTimePeriod">
            <option value="lastWeek">Last Week</option>
            <option value="lastMonth">Last Month</option>
            <option value="yesterday">Yesterday</option>
          </select>
        </div>
        <div v-if="selectedBoats && selectedBoats.length > 0">
          <BarChart :vessels="selectedBoats" :timePeriod="selectedTimePeriod" />
        </div>
        <div v-else class="empty-chart">
          <p>No boats selected</p>
          <BarChart :vessels="selectedBoats" :timePeriod="selectedTimePeriod" />
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.time-period-selector {
  margin-bottom: 10px;
}

.time-period-selector label {
  margin-right: 10px;
  font-size: 14px;
}

.time-period-selector select {
  padding: 5px;
  font-size: 14px;
  border: 1px solid #ddd;
  border-radius: 6px;
  background-color: #f9f9f9;
}
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
