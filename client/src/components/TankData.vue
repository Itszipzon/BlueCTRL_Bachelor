<script setup>
import { ref, onMounted, onUnmounted, watch } from "vue";
import ShipOverview from "../assets/ShipOverview.vue";
import ShipSideview from "../assets/ShipSideview.vue";
import axios from "axios";

const shipWidth = ref(0);
const shipHeight = ref(0);
const containerRef = ref(null);
const originalTanks = ref([]);
const tanks = ref([]);
const foundTanks = ref(false);
const currentAbove = ref(true);
let resizeObserver;
let startX = 0;
let startY = 0;

const tankMovingIndex = ref(-1);
const tankMoving = ref(false);

const props = defineProps({
  vesselId: {
    type: String | "",
    required: true,
  },
  dummy: {
    type: Boolean,
    default: false,
  },
});

const fetchTankData = async (vesselId) => {
  foundTanks.value = false;
  if (!vesselId) {
    console.log("No valid vesselId");
    return;
  }
  if (props.dummy) {
    if (vesselId === 28) {
      tanks.value = [
        { id: 1, content: "Fuel", volume: 500, capacity: 1000 },
        { id: 2, content: "Fresh Water", volume: 300, capacity: 500 },
        { id: 3, content: "Ballast", volume: 500, capacity: 800 },
        { id: 4, content: "Fresh Water", volume: 100, capacity: 500 },
        { id: 5, content: "Ballast", volume: 330, capacity: 800 },
      ];
      foundTanks.value = true;
    }
    return;
  }
  try {
    axios
      .get(
        `http://localhost:8080/api/bluebox-vessel/${vesselId}/tanks?fromProject=True`,
        {
          headers: {
            Authorization: `Basic ${localStorage.getItem("SESSION")}`,
          },
        }
      )
      .then((r) => {
        const newTemp = r.data.filter(
          (tank) =>
            tank.content.toLowerCase().includes("fuel") ||
            tank.content.toLowerCase().includes("fresh water")
        );
        tanks.value = newTemp;
        originalTanks.value = newTemp;
        foundTanks.value = true;
        console.log("New Temp: ", newTemp);
      });
  } catch (error) {
    console.error("Failed to fetch tank data:", error);
  }
};

onMounted(() => {
  if (containerRef.value) {
    const rect = containerRef.value.getBoundingClientRect();
    shipWidth.value = rect.width;
    shipHeight.value = rect.width * 0.35;

    resizeObserver = new ResizeObserver(() => {
      const updatedRect = containerRef.value.getBoundingClientRect();
      shipWidth.value = updatedRect.width;
      shipHeight.value = updatedRect.width * 0.35;
    });

    resizeObserver.observe(containerRef.value);
  }

  // Initial fetch
  fetchTankData(props.vesselId);

  window.addEventListener("mousemove", handleMouseMove);
  window.addEventListener("mouseup", mouseUpTank);
});

watch(
  () => props.vesselId,
  (newVesselId) => {
    if (newVesselId) {
      fetchTankData(newVesselId);
    }
  },
  { immediate: true, deep: true }
);

watch(
  () => containerRef.value,
  (newContainer) => {
    if (newContainer) {
      const rect = containerRef.value.getBoundingClientRect();

      shipWidth.value = rect.width;
      shipHeight.value = rect.width * 0.35;

      resizeObserver = new ResizeObserver(() => {
        const updatedRect = containerRef.value.getBoundingClientRect();
        shipWidth.value = updatedRect.width;
        shipHeight.value = updatedRect.width * 0.35;
      });
    }
  }
);

onUnmounted(() => {
  if (resizeObserver) {
    resizeObserver.disconnect();
  }
  containerRef.value = null;

  window.removeEventListener("mousemove", handleMouseMove);
  window.removeEventListener("mouseup", mouseUpTank);
});

const shipTankStyle = (tank, above) => {
  const maxCapacity = Math.max(...tanks.value.map(t => t.capacity || 1));

  const maxTankSize = {
    width: shipWidth.value * 0.2,
    height: shipHeight.value * 0.2,
  };

  const sizeScale = (tank.capacity || 1) / maxCapacity;
  const width = Math.max(20, maxTankSize.width * sizeScale);
  const height = Math.max(20, maxTankSize.height * sizeScale);

  return {
    position: tank.x != null && tank.y != null && tank.z != null ? "absolute" : "relative",
    left: `${(tank.x || 0) * shipWidth.value}px`,
    top: `${(above ? (tank.y || 0) : (tank.z || 0)) * shipHeight.value}px`,
    width: `${width}px`,
    height: `${height}px`,
    cursor: "move",
  };
};

const shipTankVolumeStyle = (tank) => {
  const percent = (tank.volume / tank.capacity) * 100;
  let background = "red";

  if (percent > 75) {
    background = "green";
  } else if (percent > 50) {
    background = "yellow";
  } else if (percent > 25) {
    background = "orange";
  } else {
    background = "red";
  }

  return {
    width: "100%",
    height: `${percent}%`,
    backgroundColor: background,
    position: `${tank.x !== null && tank.y !== null && tank.z !== null ? "absolute" : "relative"}`
  };
};

const mouseDownTank = (event, tank, index, above) => {
  tankMovingIndex.value = index;
  tankMoving.value = true;
  currentAbove.value = above;

  startX = event.clientX;
  startY = event.clientY;
};

const mouseUpTank = () => {
  tankMovingIndex.value = -1;
  tankMoving.value = false;
};

const handleMouseMove = (event) => {
  if (!tankMoving.value || tankMovingIndex.value === -1) return;

  const dx = event.clientX - startX;
  const dy = event.clientY - startY;

  startX = event.clientX;
  startY = event.clientY;

  const tank = tanks.value[tankMovingIndex.value];
  const dxRel = dx / shipWidth.value;
  const dyRel = dy / shipHeight.value;

  // Check if the tank is in the top view or side view
  if (currentAbove.value) {
    // Top view: move in x/y plane
    tank.x = (tank.x || 0) + dxRel;
    tank.y = (tank.y || 0) + dyRel;
    tank.z = tank.z || 0; // Reset z to 0 for top view
  } else {
    // Side view: move in x/z plane
    tank.x = (tank.x || 0) + dxRel;
    tank.z = (tank.z || 0) + dyRel;
    tank.y = tank.y || 0; // Reset y to 0 for side view
  }

  console.log(tank);
};

const handleSave = () => {
  const data = []
  axios.post("http://localhost:8080/api/savetanks", {
  })
  console.log("Saving tank data:", tanks);
}
</script>
<template>
  <div class="ship-tank-data" id="ship-tank-data" v-if="foundTanks">
    <div class="ship-tank-position-save-container">
      <button @click="handleSave">Save</button>
    </div>
    <div class="ship-drawings">
      <p>Ship tank data overview</p>
      <div class="ship-drawing-container" ref="containerRef">
        <div class="ship-tanks">
          <div class="ship-tank" v-for="(tank, index) in tanks" :key="`Tank${index}`" :style="shipTankStyle(tank, true)"
            @mousedown="(e) => mouseDownTank(e, tank, index, true)" @mouseup="mouseUpTank">
            <div class="ship-tank-volume" :style="shipTankVolumeStyle(tank)" />
          </div>
        </div>
        <ShipOverview :width="shipWidth" :height="shipHeight" />
      </div>
      <p>Ship tank data side view</p>
      <div class="ship-drawing-container">
        <div class="ship-tanks">
          <div class="ship-tank" v-for="(tank, index) in tanks" :key="`Tank${index}`"
            :style="shipTankStyle(tank, false)" @mousedown="(e) => mouseDownTank(e, tank, index, false)"
            @mouseup="mouseUpTank">
            <div class="ship-tank-volume" :style="shipTankVolumeStyle(tank)" />
          </div>
        </div>
        <ShipSideview :width="shipWidth" :height="shipHeight" />
      </div>
    </div>
  </div>
  <div class="ship-tank-no-tank" v-else>
    <p>No tank data available for this vessel.</p>
  </div>
</template>
<style scoped>
p {
  color: black;
}

.ship-tank-data {
  display: flex;
  align-items: center;
  max-height: 500px;
  height: 500px;
}

.ship-tank-position-save-container {
  position: absolute;
  display: flex;
  top: 0;
  right: 0;
}

.ship-tank-position-save-container button {
  background-color: #1a6281;
  border: 1px solid black;
  padding: 4px;
  border-radius: 8px;
  color: white;
  font-size: 20px;
  cursor: pointer;
  z-index: 11;
}

.ship-tank-position-save-container button:hover {
  background-color: #114155;
}

.ship-tanks {
  display: flex;
  position: absolute;
  flex-direction: row;
  flex-wrap: wrap;
  gap: 2px;
}

.ship-tank {
  height: 50px;
  width: 50px;
  border: 1px solid black;
  display: flex;
  align-items: flex-end;
  background-color: white;
  z-index: 1;
}

.ship-drawings {
  height: 100%;
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.ship-drawing-container {
  position: relative;
  justify-content: center;
  width: 100%;
  max-width: 800px;
  height: fit-content;
}

.ship-tank-no-tank {
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
