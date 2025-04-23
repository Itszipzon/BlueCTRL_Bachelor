<script>
export default {
  props: {
    vessels: {
      type: Array,
      required: true,
    },
    timePeriod: {
      type: String,
      required: false,
      default: "all", 
    },
  },
  computed: {
    filteredBars() {
      if (!this.vessels) return [];
      return this.vessels.map((vessel) => ({
        label: vessel.vesselName,
        value: this.getTravelDistanceForPeriod(vessel),
      }));
    },
  },
  methods: {
    getTravelDistanceForPeriod(vessel) {
      if (this.timePeriod === "all") {
        return vessel.travelDistance || 0;
      } else if (this.timePeriod === "lastWeek") {
        return vessel.travelDistanceLastWeek || 0;
      } else if (this.timePeriod === "lastMonth") {
        return vessel.travelDistanceLastMonth || 0;
      }
      return 0;
    },
  },
  watch: {
    timePeriod() {
      
    },
  },
};
</script>

<template>
  <div>
    <div class="chart">
      <div
        v-for="(bar, index) in filteredBars"
        :key="index"
        class="bar"
        :style="{ height: bar.value + '%' }"
        :data-value="bar.value"
      ></div>
    </div>

    <div class="labels">
      <div v-for="(bar, index) in filteredBars" :key="index" class="label">
        {{ bar.label }}
      </div>
    </div>
  </div>
</template>

<style scoped>
.chart {
  width: 100%;
  margin: 20px auto;
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  height: 300px;
  border-left: 1px solid #ccc;
  border-bottom: 1px solid #ccc;
  padding: 10px;
}

.bar {
  flex: 1;
  margin: 0 5px;
  background-color: #3498db;
  position: relative;
  transition: background-color 0.3s ease;
}

.bar:hover {
  background-color: #2980b9;
}

.bar::after {
  content: attr(data-value);
  position: absolute;
  top: -20px;
  left: 0;
  right: 0;
  text-align: center;
  font-size: 14px;
  color: #333;
}

.labels {
  width: 100%;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
}

.label {
  text-align: center;
  margin-top: 10px;
  font-size: 14px;
  color: #555;
}
</style>
