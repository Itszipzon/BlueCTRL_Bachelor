<script setup>
import { ref, onMounted, onUnmounted, watch } from "vue";
import ShipOverview from "../assets/ShipOverview.vue";
import ShipSideview from "../assets/ShipSideview.vue";
import axios from "axios";
import Revert from "../assets/icons/Revert.vue";
import HelpIcon from "./HelpIcon.vue";

/**
 * @typedef {Object} Tank
 * @property {number} id
 * @property {string} content
 * @property {number} volume
 * @property {number} capacity
 * @property {number} [x]
 * @property {number} [y]
 * @property {number} [z]
 */

/** @type {import('vue').Ref<number>} */
const shipWidth = ref(0);

/** @type {import('vue').Ref<number>} */
const shipHeight = ref(0);

/** @type {import('vue').Ref<number>} */
const mouseX = ref(0);
const mouseY = ref(0);

/** @type {import('vue').Ref<HTMLElement|null>} */
const containerRef = ref(null);

/** @type {import('vue').Ref<Tank[]>} */
const originalTanks = ref([]);

/** @type {import('vue').Ref<Tank[]>} */
const tanks = ref([]);

/** @type {import('vue').Ref<boolean>} */
const foundTanks = ref(false);

/** @type {import('vue').Ref<boolean>} */
const currentAbove = ref(true);

/** @type {import('vue').Ref<boolean>} */
const tanksMoved = ref(false);

let resizeObserver;
let startX = 0;
let startY = 0;

/** @type {import('vue').Ref<number>} */
const tankMovingIndex = ref(-1);

/** @type {import('vue').Ref<boolean>} */
const tankMoving = ref(false);

/** @type {import('vue').Ref<number>} */
const tankOverIndex = ref(-1);

/** @type {string} */
const helpTextMove = "Click and drag to move the tanks.";

const props = defineProps({
  /** @type {string} Vessel ID */
  vesselId: {
    type: String || "",
    required: true,
  },
  /** @type {boolean} Load dummy data for development */
  dummy: {
    type: Boolean,
    default: false,
  },
});

/**
 * Fetch tank data from API or use dummy data
 * @param {string} vesselId
 */
const fetchTankData = async (vesselId) => {
  foundTanks.value = false;
  if (!vesselId) return;

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
    const r = await axios.get(
      `http://localhost:8080/api/bluebox-vessel/${vesselId}/tanks?fromProject=True`,
      {
        headers: {
          Authorization: `Basic ${localStorage.getItem("SESSION")}`,
        },
      }
    );
    const newTemp = r.data.filter((tank) =>
      ["fuel", "fresh water"].some((k) =>
        tank.content.toLowerCase().includes(k)
      )
    );
    tanks.value = newTemp;
    originalTanks.value = JSON.parse(JSON.stringify(newTemp));
    foundTanks.value = true;
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
  if (resizeObserver) resizeObserver.disconnect();
  containerRef.value = null;

  window.removeEventListener("mousemove", handleMouseMove);
  window.removeEventListener("mouseup", mouseUpTank);
});

/**
 * Returns tank positioning and sizing style for rendering.
 * @param {Tank} tank
 * @param {boolean} above - True for top view, false for side view
 * @returns {Object}
 */
const shipTankStyle = (tank, above) => {
  const maxCapacity = Math.max(...tanks.value.map((t) => t.capacity || 1));

  const maxTankSize = {
    width: shipWidth.value * 0.2,
    height: shipHeight.value * 0.2,
  };

  const sizeScale = (tank.capacity || 1) / maxCapacity;
  const width = Math.max(20, maxTankSize.width * sizeScale);
  const height = Math.max(20, maxTankSize.height * sizeScale);

  return {
    position: "relative",
    left: `${(tank.x || 0) * shipWidth.value}px`,
    top: `${(above ? tank.y || 0 : tank.z || 0) * shipHeight.value}px`,
    width: `${width}px`,
    height: `${height}px`,
    cursor: "move",
  };
};

/**
 * Returns styling for volume fill level inside tank.
 * @param {Tank} tank
 * @returns {Object}
 */
const shipTankVolumeStyle = (tank) => {
  const percent = (tank.volume / tank.capacity) * 100;
  let background = "red";

  if (percent > 75) background = "green";
  else if (percent >= 50) background = "yellow";
  else if (percent >= 25) background = "orange";

  return {
    width: "100%",
    height: `${percent}%`,
    backgroundColor: background,
    position: "absolute",
  };
};

/**
 * Handles mouse down to begin tank drag.
 * @param {MouseEvent} event
 * @param {Tank} tank
 * @param {number} index
 * @param {boolean} above
 */
const mouseDownTank = (event, tank, index, above) => {
  tankMovingIndex.value = index;
  tankMoving.value = true;
  currentAbove.value = above;

  startX = event.clientX;
  startY = event.clientY;
};

/**
 * Handles mouse up to stop tank drag.
 */
const mouseUpTank = () => {
  tankMovingIndex.value = -1;
  tankMoving.value = false;
};

/**
 * Handles dragging logic during mouse move.
 * @param {MouseEvent} event
 */
const handleMouseMove = (event) => {
  mouseX.value = event.clientX;
  mouseY.value = event.clientY;
  if (!tankMoving.value || tankMovingIndex.value === -1) return;

  tanksMoved.value = true;

  const dx = event.clientX - startX;
  const dy = event.clientY - startY;

  startX = event.clientX;
  startY = event.clientY;

  const tank = tanks.value[tankMovingIndex.value];
  const dxRel = dx / shipWidth.value;
  const dyRel = dy / shipHeight.value;

  if (currentAbove.value) {
    tank.x = (tank.x || 0) + dxRel;
    tank.y = (tank.y || 0) + dyRel;
    tank.z = tank.z || 0;
  } else {
    tank.x = (tank.x || 0) + dxRel;
    tank.z = (tank.z || 0) + dyRel;
    tank.y = tank.y || 0;
  }
};

/**
 * Save tank layout to backend.
 */
const handleSave = () => {
  const data = tanks.value.map((tank) => ({
    vesselId: props.vesselId,
    tankId: tank.id,
    x: tank.x,
    y: tank.y,
    z: tank.z,
  }));

  axios
    .post("http://localhost:8080/api/savetanks", data)
    .then((r) => {
      alert(r.data ? "Tank data saved successfully!" : "Failed to save tank data.");
    })
    .catch((error) => {
      console.error("Error saving tank data:", error);
      alert("Failed to save tank data.");
    });
};

/**
 * Revert tanks to original layout.
 */
const revertTanks = () => {
  tanks.value = JSON.parse(JSON.stringify(originalTanks.value));
  tanksMoved.value = false;
};

/**
 * Tooltip style near cursor
 * @returns {Object}
 */
const tankDisplayStyle = () => {
  const activeIndex = tankOverIndex.value >= 0 ? tankOverIndex.value : tankMovingIndex.value;
  if (activeIndex === -1) return { display: "none" };

  return {
    left: `${mouseX.value + 10}px`,
    top: `${mouseY.value + 10}px`,
  };
};

/**
 * Set hovered tank index.
 * @param {number} index
 */
const mouseOverTank = (index) => {
  tankOverIndex.value = index;
};

/**
 * Clear hovered tank index.
 */
const mouseOutTank = () => {
  tankOverIndex.value = -1;
};
</script>

<template>
  <div class="ship-tank-data" id="ship-tank-data" v-if="foundTanks">
    <div class="ship-tank-display" v-if="tankOverIndex >= 0 || tankMovingIndex >= 0" :style="tankDisplayStyle()">
      <div class="ship-tank-display-element">
        <p>Content:</p>
        <p>{{ tanks[tankOverIndex >= 0 ? tankOverIndex : 0].content }}</p>
      </div>
      <div class="ship-tank-display-element">
        <p>Capacity:</p>
        <p>{{ tanks[tankOverIndex >= 0 ? tankOverIndex : 0].capacity.toFixed(2) }} l</p>
      </div>
      <div class="ship-tank-display-element">
        <p>Volume:</p>
        <p>{{ tanks[tankOverIndex >= 0 ? tankOverIndex : 0].volume.toFixed(2) }} l</p>
      </div>
      <div class="ship-tank-display-element">
        <p>Filled:</p>
        <p>{{ ((tanks[tankOverIndex >= 0 ? tankOverIndex : 0].volume / tanks[tankOverIndex >= 0 ? tankOverIndex :
          0].capacity) * 100).toFixed(2) }}%</p>
      </div>
    </div>
    <div class="ship-tank-position-save-container" v-if="tanksMoved">
      <button @click="revertTanks">
        <Revert />
      </button>
      <button @click="handleSave">Save</button>
    </div>
    <div class="ship-drawings">
      <h3>Ship tank data overview <HelpIcon :left="'20px'" :top="'20px'" :size="'24px'" :help-text="helpTextMove" /> </h3>
      <div class="ship-drawing-container" ref="containerRef">
        <div class="ship-tanks">
          <div class="ship-tank" v-for="(tank, index) in tanks" :key="`Tank${index}`" :style="shipTankStyle(tank, true)"
            @mousedown="(e) => mouseDownTank(e, tank, index, true)" @mouseup="mouseUpTank"
            @mouseover="mouseOverTank(index)" @mouseout="mouseOutTank()">
            <div class="ship-tank-volume" :style="shipTankVolumeStyle(tank)" />
          </div>
        </div>
        <ShipOverview :width="shipWidth" :height="shipHeight" />
      </div>
      <h3>Ship tank data side view <HelpIcon :left="'20px'" :top="'20px'" :size="'24px'" :help-text="helpTextMove" /></h3>
      <div class="ship-drawing-container">
        <div class="ship-tanks">
          <div class="ship-tank" v-for="(tank, index) in tanks" :key="`Tank${index}`"
            :style="shipTankStyle(tank, false)" @mousedown="(e) => mouseDownTank(e, tank, index, false)"
            @mouseup="mouseUpTank" @mouseover="mouseOverTank(index)" @mouseout="mouseOutTank()">
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
}

.ship-tank-display {
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  background-color: rgba(255, 255, 255, 1);
  border-radius: 8px;
  display: flex;
  position: fixed;
  flex-direction: column;
  z-index: 100;
  width: 150px;
  padding: 5px;
}

.ship-tank-display-element {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}

.ship-tank-position-save-container {
  position: absolute;
  display: flex;
  top: 0;
  right: 0;
  gap: 5px
}

.ship-tank-position-save-container button {
  background-color: #1a6281;
  border: 1px solid black;
  padding: 2px 4px;
  border-radius: 8px;
  color: white;
  font-size: 15px;
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

.ship-drawings h3 {
  display: flex;
  gap: 10px;
  font-size: 16px;
  font-weight: bolder;
  font-family: "Poppins", sans-serif;
  color: black;
  padding: 0;
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
