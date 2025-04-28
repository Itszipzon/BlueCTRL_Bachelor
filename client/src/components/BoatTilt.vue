<script setup>
import { ref, computed, onMounted, onBeforeUnmount, watchEffect } from "vue";
import axios from "axios";
import CurvedArrow from "./icons/CurvedArrow.vue";

const sensorValue = ref(0);
const hasSensorData = ref(false);

const containerWidth = ref(0);
const containerHeight = ref(0);
const boatTiltContainer = ref(null);
const arrowSize = ref("30px");
let resizeObserver;

onMounted(() => {
  if (boatTiltContainer.value) {
    resizeObserver = new ResizeObserver((entries) => {
      for (const entry of entries) {
        const { width, height } = entry.contentRect;
        containerWidth.value = width;
        containerHeight.value = height;
      }
      if (containerWidth.value < 211) {
        let newWidth = containerWidth.value * 0.15 - 10;
        arrowSize.value = `${newWidth}px`;
      } else {
        arrowSize.value = "30px";
      }
      console.log("Arrow size:", arrowSize.value);
    });
    resizeObserver.observe(boatTiltContainer.value);
  }
});

onBeforeUnmount(() => {
  if (resizeObserver && containerRef.value) {
    resizeObserver.unobserve(containerRef.value);
  }
});

const props = defineProps({
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

const vesselDimentions = computed(() => {
  if (props.type === "roll") {
    return {
      width: containerWidth.value * 0.25 + "px",
      marginBottom: "-5px"
    };
  } else if (props.type === "pitch") {
    return {
      height: containerHeight.value * 0.25 + "px",
      marginBottom: "-8px"
    };
  }
})

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
  <div class="boat-tilt-container" ref="boatTiltContainer">
    <div v-if="hasSensorData && props.vesselId !== null" class="tilt-view">
      <div class="boat-tilt-arrow-container">
        <CurvedArrow :reversed="sensorValue < 0" :size="arrowSize" />
      </div>
      <!-- Container with the protractor background -->
      <div class="protractor-container">
        <img
          v-if="props.type === 'roll'"
          class="boat-image"
          src="@/assets/boatfront.png"
          alt="Boat Front View"
          :style="[styleTransform, vesselDimentions]"
        />
        <img
          v-else-if="props.type === 'pitch'"
          class="boat-image"
          src="@/assets/boatside.png"
          alt="Boat Side View"
          :style="[styleTransform, vesselDimentions]"
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
  padding: 10px;
}

.boat-tilt-arrow-container {
  width: 100%;
  display: flex;
  justify-content: center;
}

.tilt-view {
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
  width: 100%;
  height: 100%;
}

.protractor-container {
  height: 100%;
  width: 100%;
  margin-bottom: 8px;
  display: flex;
  justify-content: center;
  align-items: flex-end;
  background-image: url("@/assets/icons/protractor.png");
  background-repeat: no-repeat;
  background-position: center calc(100% + 10px);
  background-size: calc(120%) auto;
}

.tilt-label {
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
