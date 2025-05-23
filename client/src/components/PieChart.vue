<template>
  <div class="pie-container">
    <div class="pie-chart" :style="chartStyle"></div>
    <div class="labels">
      <div v-for="(item, index) in items" :key="index" class="label">
        <span :style="{ backgroundColor: item.color }" class="color-box"></span>
        {{ item.label }}: {{ item.value }}%
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      /**
       * @typedef {Object} PieItem
       * @property {string} label - The label for the pie segment.
       * @property {number} value - The percentage value (0-100) for the segment.
       * @property {string} color - The color code for the pie segment.
       */

      /**
       * Array of pie chart segments with labels, values, and colors.
       * @type {PieItem[]}
       */
      items: [
        { label: "A", value: 30, color: "#3498db" },
        { label: "B", value: 25, color: "#9b59b6" },
        { label: "C", value: 20, color: "#e74c3c" },
        { label: "D", value: 25, color: "#f1c40f" },
      ],
    };
  },

  computed: {
    /**
     * Generates the CSS background style for a conic-gradient pie chart
     * based on the current `items` array.
     *
     * @returns {Object} A style object with a `background` property set to a conic-gradient.
     */
    chartStyle() {
      let gradient = "conic-gradient(";
      let total = 0;

      this.items.forEach((item, index) => {
        gradient += `${item.color} ${total}% ${total + item.value}%`;
        total += item.value;
        if (index < this.items.length - 1) gradient += ", ";
      });

      gradient += ")";
      return { background: gradient };
    },
  },
};

</script>

<style scoped>
.pie-container {
  width: 250px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  margin: 20px auto;
}

.pie-chart {
  width: 200px;
  height: 200px;
  border-radius: 50%;
  position: relative;
  margin-bottom: 15px;
}

.labels {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.label {
  display: flex;
  align-items: center;
  font-size: 14px;
  color: #555;
  margin: 2px 0;
}

.color-box {
  width: 12px;
  height: 12px;
  margin-right: 8px;
  display: inline-block;
}
</style>
