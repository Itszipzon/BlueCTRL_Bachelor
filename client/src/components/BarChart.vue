<script setup>
import axios from "axios";
import { ref, watch } from "vue";

// Dates for the last 7 days
const from = new Date().setDate(new Date().getDate() - 7);
const to = new Date().setDate(new Date().getDate());

const props = defineProps({
  vessels: {
    type: Array,
    required: true,
  },
  timePeriod: {
    type: String,
    default: "all",
  },
});

const updatedVessels = ref([...props.vessels]);
const processedVesselIds = ref(new Set()); // Track processed vessels by ID

// Watch the vessels prop for changes
watch(
  () => props.vessels,
  async (newVessels, oldVessels) => {
    if (!newVessels?.length) return;

    // Check for newly added vessels that haven't been processed
    const newVessel = newVessels.find(
      (vessel) => !processedVesselIds.value.has(vessel.id)
    );

    if (newVessel) {
      try {
        const response = await axios.get(
          `http://localhost:8080/api/vessel-gps-positions?vesselId=${newVessel.id}&from=${from}&to=${to}`,
          {
            headers: {
              Authorization: `Basic ${localStorage.getItem("SESSION")}`,
            },
          }
        );

        const gpsData = response.data;
        const distance = calculateTotalTravelDistance(gpsData);

        console.log("Distance for vessel", newVessel.id, ":", distance);

        // Mark this vessel as processed
        processedVesselIds.value.add(newVessel.id);

        // Update only the new vessel's travel distance
        const updatedVessel = { ...newVessel, travelDistance: distance };

        const index = updatedVessels.value.findIndex(
          (vessel) => vessel.id === newVessel.id
        );
        if (index !== -1) {
          updatedVessels.value[index] = updatedVessel; // Replace only the updated vessel
        } else {
          updatedVessels.value.push(updatedVessel); // Add the new vessel if not already present
        }
      } catch (error) {
        console.error(
          `Error fetching GPS data for vessel ${newVessel.id}`,
          error
        );
      }
    }
  },
  { immediate: true, deep: true }
);

function calculateTotalTravelDistance(dataPoints) {
  if (!dataPoints || dataPoints.length < 2) return 0;

  const R = 6371; // Earth's radius in km
  let totalDistance = 0;

  for (let i = 1; i < dataPoints.length; i++) {
    const prev = dataPoints[i - 1];
    const curr = dataPoints[i];

    const dLat = toRadians(curr.latitude - prev.latitude);
    const dLon = toRadians(curr.longitude - prev.longitude);

    const lat1 = toRadians(prev.latitude);
    const lat2 = toRadians(curr.latitude);

    const a =
      Math.sin(dLat / 2) ** 2 +
      Math.sin(dLon / 2) ** 2 * Math.cos(lat1) * Math.cos(lat2);
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    const distance = R * c;

    totalDistance += distance;
  }

  return totalDistance;
}

function toRadians(degrees) {
  return degrees * (Math.PI / 180);
}

defineExpose({
  updatedVessels,
});
</script>



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
      if (!this.updatedVessels) return [];
      const maxDistance = Math.max(
        ...this.updatedVessels.map((vessel) => vessel.travelDistance || 0)
      );

      return this.updatedVessels.map((vessel) => ({
        label: vessel.vesselName,
        value: this.getTravelDistanceForPeriod(vessel),
        height:
          maxDistance > 0
            ? (this.getTravelDistanceForPeriod(vessel) / maxDistance) * 100
            : 0,
        formattedValue: `${Math.round(this.getTravelDistanceForPeriod(vessel))} km`
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
    timePeriod() {},
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
        :style="{ height: bar.height + '%' }"
        :data-value="bar.formattedValue" >
      </div>
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
  margin: 20px auto 0;
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  height: 300px;
  border-left: 1px solid #ccc;
  border-bottom: 1px solid #ccc;
  padding: 10px 10px 0 10px;
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
</style>
