<template>
  <div>
    <div class="chart">
      <div v-if="isLoading" class="spinner-overlay">
        <div class="spinner"></div>
      </div>
      <div
        v-for="(bar, index) in filteredBars"
        :key="index"
        class="bar"
        :style="{ height: bar.height + '%' }"
        :data-value="bar.formattedValue"
      ></div>
    </div>
    <div class="labels">
      <div v-for="(bar, index) in filteredBars" :key="index" class="label">
        {{ bar.label }}
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  props: {
    vessels: { type: Array, required: true },
    timePeriod: { type: String, default: "all" },
  },

  data() {
    return {
      updatedVessels: [...this.vessels],
      processedIds: new Set(),
      isLoading: false,
    };
  },

  watch: {
    vessels: {
      handler: "fetchDistances",
      immediate: true,
      deep: true,
    },
    timePeriod: {
      handler() {
        this.fetchDistances(this.vessels);
      },
    },
  },

  computed: {
    filteredBars() {
      if (!this.updatedVessels.length) return [];
      const raw = this.updatedVessels.map((v) =>
        Number(this.getDistanceForPeriod(v))
      );
      const values = raw.map((v) => (Number.isFinite(v) ? v : 0));

      const max = values.length ? Math.max(...values) : 0;
      return this.updatedVessels.map((v, i) => {
        const val = values[i];
        return {
          label: v.vesselName,
          value: val,
          height: max ? (val / max) * 100 : 0,
          formattedValue: `${Math.round(val)} km`,
        };
      });
    },
  },

  methods: {
    async fetchDistances(newVessels) {
      const currentIds = new Set(newVessels.map((v) => v.id));

      this.updatedVessels = this.updatedVessels.filter((v) =>
        currentIds.has(v.id)
      );

      this.processedIds.forEach((id) => {
        if (!currentIds.has(id)) this.processedIds.delete(id);
      });

      if (!newVessels.length) return;

      this.isLoading = true;

      const MS_DAY = 86_400_000;
      const now = new Date();
      let to = now.getTime();
      let from;

      switch (this.timePeriod) {
        case "lastWeek":
          from = to - 7 * MS_DAY;
          break;
        case "lastMonth":
          from = to - 30 * MS_DAY;
          break;
        case "yesterday":
          const today = new Date(
            now.getFullYear(),
            now.getMonth(),
            now.getDate()
          );
          to = today.getTime();
          from = to - MS_DAY;
          break;
        default:
          from = to - 7 * MS_DAY;
          break;
      }

      try {
        const tasks = newVessels
          .filter((v) => !this.processedIds.has(`${v.id}-${this.timePeriod}`))
          .map(async (v) => {
            try {
              const { data } = await axios.get(
                "http://localhost:8080/api/vessel-gps-positions",
                {
                  params: { vesselId: v.id, from, to },
                  headers: {
                    Authorization: `Basic ${localStorage.getItem("SESSION")}`,
                  },
                }
              );

              const dist = this.totalDistance(data);
              this.processedIds.add(`${v.id}-${this.timePeriod}`);
              const idx = this.updatedVessels.findIndex((e) => e.id === v.id);
              const record = { ...v, travelDistance: dist };

              if (idx === -1) {
                this.updatedVessels.push(record);
              } else {
                this.$set(this.updatedVessels, idx, record);
              }
            } catch (err) {
              console.error(`GPS request failed for vessel ${v.id}`, err);
            }
          });
        await Promise.all(tasks);
      } finally {
        this.isLoading = false;
      }
    },

    getDistanceForPeriod(v) {
      return v.travelDistance || 0;
    },

    totalDistance(points) {
      if (!points || points.length < 2) return 0;

      const R = 6371;
      const rad = (d) => (d * Math.PI) / 180;

      const sum = points.slice(1).reduce((acc, cur, i) => {
        const prev = points[i];
        const dLat = rad(cur.latitude - prev.latitude);
        const dLon = rad(cur.longitude - prev.longitude);

        const a =
          Math.sin(dLat / 2) ** 2 +
          Math.cos(rad(prev.latitude)) *
            Math.cos(rad(cur.latitude)) *
            Math.sin(dLon / 2) ** 2;

        return acc + 2 * R * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
      }, 0);

      return Number.isFinite(sum) ? sum : 0;
    },
  },
};
</script>

<style scoped>
.chart {
  width: 100%;
  margin: 20px auto 0;
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  height: 300px;
  border-left: 1px solid #ccc;
  border-bottom: 1px solid #ccc;
  padding: 10px 10px 0;
  position: relative;
}

.bar {
  flex: 1;
  margin: 0 5px;
  background-color: #3498db;
  position: relative;
  transition: background-color 0.3s ease;
  display: flex;
  align-items: flex-end;
  justify-content: center;
  min-height: 3px;
}

.bar:hover {
  background-color: #2980b9;
}

.bar::after {
  content: attr(data-value);
  position: absolute;
  top: -20px;
  text-align: center;
  font-size: 14px;
  color: #333;
  width: 100%;
}

.labels {
  width: 100%;
  display: flex;
  justify-content: space-between;
  padding: 5px 10px 0;
  margin-top: 5px;
}

.label {
  flex: 1;
  text-align: center;
  font-size: 14px;
  color: #555;
}

@keyframes spin {
  from {
    transform: rotate(0);
  }
  to {
    transform: rotate(360deg);
  }
}
</style>
