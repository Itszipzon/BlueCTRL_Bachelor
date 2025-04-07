<script setup>
import { ref, computed, onMounted, watchEffect } from "vue";
import axios from "axios";

const sensorValue = ref(0);
const hasSensorData = ref(false);

const props = defineProps({
  // "roll" or "pitch"
  type: {
    type: String,
    required: true,
    validator: (value) => ["roll", "pitch"].includes(value),
  },
  vesselId: {
    type: Number,
    required: false,
  },
});

function getSignalId() {
  if (props.vesselId === 2) {
    return props.type === "roll" ? 18193 : 18194;
  } else {
    return props.type === "roll" ? 118 : 119;
  }
}

watchEffect(() => {
  console.log("vesselId changed to", props.vesselId);
  fetchSensorData();
});

async function fetchSensorData() {
  if (props.vesselId) {
    const signalId = getSignalId();
    const url = `http://localhost:8080/api/signal?signalId=${signalId}&vesselId=${props.vesselId}`;
    if (localStorage.getItem("SESSION")) {
      axios
        .get(url, {
          headers: {
            Authorization: `Basic ${localStorage.getItem("SESSION")}`,
          },
        })
        .then((r) => {
          if (r.status === 200) {
            const data = r.data;
            console.log(data);
            if (data.signalEvents && data.signalEvents.length > 0) {
              // Use the last value in the signalEvents array
              const lastEvent = data.signalEvents[data.signalEvents.length - 1];
              sensorValue.value = lastEvent.doubleValue;
              hasSensorData.value = true;
            } else {
              hasSensorData.value = false;
            }
          }
        })
        .catch((e) => {
          localStorage.removeItem("SESSION");
        });
    }
  } else {
    console.log("No vessel id");
  }
}

/* onMounted(() => {
    fetchSensorData();
    setInterval(fetchSensorData, 10 * 1000);
}); */

// Computed style for the rotating boat image.
const styleTransform = computed(() => ({
  transform: `rotate(${sensorValue.value}deg)`,
  transition: "transform 0.2s ease-out",
}));
</script>

<template>
  <div class="boat-tilt-container">
    <div v-if="hasSensorData && props.vesselId !== null" class="tilt-view">
      <!-- Container with the protractor background -->
      <div class="protractor-container">
        <img
          v-if="props.type === 'roll'"
          class="boat-image"
          src="@/assets/boatfront.png"
          alt="Boat Front View"
          :style="styleTransform"
        />
        <img
          v-else-if="props.type === 'pitch'"
          class="boat-image"
          src="@/assets/boatside.png"
          alt="Boat Side View"
          :style="styleTransform"
        />
      </div>
      <!-- The text is placed in normal flow below the protractor container -->
      <p v-if="props.type === 'roll'" class="tilt-label">
        Roll: {{ sensorValue }}°
      </p>
      <p v-else-if="props.type === 'pitch'" class="tilt-label">
        Pitch: {{ sensorValue }}°
      </p>
    </div>
    <div v-else class="no-sensor-data">
      <p>No sensor data available</p>
    </div>
  </div>
</template>

<style scoped>
.boat-tilt-container {
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: black;
}

.tilt-view {
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
  width: 100%;
  height: 100%;
  background: url("@/assets/icons/protractor.png") no-repeat center
    calc(100% - 20px);
  background-size: 120%;
  margin-bottom: 5px;
}

/* The container displays the protractor as a background */
.protractor-container {
  background-size: contain;
  margin-bottom: 8px;
  /* Space between the protractor and the text */
}

/* Center the boat image within the protractor container */
.boat-image {
  display: block;
  width: 80px;
  /* Adjust size as needed */
  margin: 0 auto;
}

/* The tilt text below the protractor */
.tilt-label {
  margin-top: 8px;
  font-weight: 500;
  text-align: center;
}

.no-sensor-data {
  display: flex;
  justify-content: center;
  align-items: center;
  color: black;
}
</style>
