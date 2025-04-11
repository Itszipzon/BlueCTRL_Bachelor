<template>
  <div>
    <div class="chart">
      <div
        v-for="(bar, index) in bars"
        :key="index"
        class="bar"
        :style="{ height: bar.value + '%' }"
        :data-value="bar.value"
      ></div>
    </div>

    <div class="labels">
      <div v-for="(bar, index) in bars" :key="index" class="label">
        {{ bar.label }}
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      bars: [
        { label: "A", value: 1 },
        { label: "B", value: 40 },
        { label: "C", value: 90 },
        { label: "D", value: 50 },
        { label: "E", value: 80 },
      ],
    };
  },
};
</script>

<style scoped>
.chart {
  width: 80%;
  max-width: 600px;
  margin: 50px auto;
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  height: 300px;
  border-left: 2px solid #333;
  border-bottom: 2px solid #333;
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
  width: 80%;
  max-width: 600px;
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
