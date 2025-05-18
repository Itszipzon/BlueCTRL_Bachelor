<script setup>
import { ref, computed, onMounted, onBeforeUnmount, watchEffect } from "vue";
import axios from "axios";
import CurvedArrow from "../assets/icons/CurvedArrow.vue";
import protractor_non_exagerate from "@/assets/icons/protractor_non_exagerate.png";
import protractor_exagerate from "@/assets/icons/protractor_exagerate.png";

const sensorValue = ref(0);
const hasSensorData = ref(false);

const containerWidth = ref(0);
const containerHeight = ref(0);
const boatTiltContainer = ref(null);
const svgContainer = ref(null);
const svgWidth = ref(0);
const svgHeight = ref(0);
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
    });
    resizeObserver.observe(boatTiltContainer.value);
  }
  if (svgContainer.value) {
    resizeObserver = new ResizeObserver((entries) => {
      for (const entry of entries) {
        const { width, height } = entry.contentRect;
        svgWidth.value = width;
        svgHeight.value = height;
      }
    });
    resizeObserver.observe(svgContainer.value);
  }
});

onBeforeUnmount(() => {
  if (resizeObserver && svgContainer.value) {
    resizeObserver.unobserve(svgContainer.value);
  }
});

const props = defineProps({
  type: {
    type: String,
    required: true,
    validator: (value) => ["roll", "pitch"].includes(value),
  },
  vesselId: {
    type: String | "",
    required: false,
  },
  backgroundOffset: {
    type: String,
    default: 100,
  },
  exagerate_values: {
    type: Boolean,
    default: false,
  },
  dummyData: {
    type: Boolean,
    default: false,
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
    const w = containerWidth.value * 0.25;
    const h = containerHeight.value * 0.25;
    return {
      width: `${w}px`,
      maxHeight: `${h}px`,
      marginBottom: "-20px",
    };
  } else if (props.type === "pitch") {
    return {
      pureWidth: 0,
      pureHeight: containerHeight.value * 0.25,
      height: containerHeight.value * 0.25 + "px",
      marginBottom: "-20px",
    };
  }
});

watchEffect(() => {
  fetchSensorData();
});

async function fetchSensorData() {
  if (props.vesselId) {
    if (props.dummyData) {
      if (props.vesselId === 28) {
        sensorValue.value = props.type === "roll" ? 10 : -10;
        hasSensorData.value = true;
      } else if (props.vesselId === 2) {
        sensorValue.value = props.type === "roll" ? 5 : -5;
        hasSensorData.value = true;
      }
      return;
    }
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
            if (data.signalEvents && data.signalEvents.length > 0) {
              const lastEvent = data.signalEvents[data.signalEvents.length - 1];
              sensorValue.value =
                Math.round(lastEvent.doubleValue * 10000) / 10000;
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
  }
}

const backgroundStyle = computed(() => ({
  backgroundImage: `url(${
    props.exagerate_values ? protractor_exagerate : protractor_non_exagerate
  })`,
  backgroundRepeat: "no-repeat",
  backgroundPosition: `center calc(${props.backgroundOffset}%)`,
  backgroundSize: "calc(100%) auto",
}));

// Computed style for the rotating boat image.
const styleTransform = computed(() => ({
  transform: `rotate(${
    sensorValue.value * (props.exagerate_values ? 3 : 1)
  }deg) scale(4.2)`,
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
      <div class="protractor-container" :style="backgroundStyle">
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
        <div class="svg-boat-tilt-indicator" ref="svgContainer">
          <svg xmlns="http://www.w3.org/2000/svg">
            <g>
              <line
                :x1="svgWidth / 2 - 1"
                :y1="svgHeight * 0.65"
                :y2="svgHeight / 18"
                :x2="svgWidth / 2 - 1"
                stroke="black"
                stroke-width="2"
              />
            </g>
          </svg>
        </div>
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

.svg-boat-tilt-indicator {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.boat-image {
  object-fit: contain;
}
</style>
