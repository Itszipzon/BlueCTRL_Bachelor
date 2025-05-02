<script setup>
import { ref, onMounted, onUnmounted, watch } from 'vue';
import ShipOverview from '../assets/ShipOverview.vue';
import ShipSideview from '../assets/ShipSideview.vue';
import axios from 'axios';

const shipWidth = ref(0);
const shipHeight = ref(0);
const containerRef = ref(null);
const tanks = ref([]);
const foundTanks = ref(false);
let resizeObserver;

const props = defineProps({
  vesselId: {
    type: String | "",
    required: true
  }
});

const fetchTankData = async (vesselId) => {
  foundTanks.value = false;
  if (!vesselId) {
    console.log("No valid vesselId")
    return
  }
  let temp = [];
  try {
    axios.get(`http://localhost:8080/api/bluebox-vessel/${vesselId}/tanks?fromProject=True`, {
      headers: {
        Authorization: `Basic ${localStorage.getItem("SESSION")}`,
      }
    })
      .then((r) => {
        temp = r.data;

        axios.get(`http://localhost:8080/api/bluebox-vessel/${vesselId}/tanks/current`, {
          headers: {
            Authorization: `Basic ${localStorage.getItem("SESSION")}`,
          }
        })
          .then((current) => {
            current.data.forEach(newTank => {
              const existingTank = temp.find(t => t.id === newTank.id);
              if (existingTank) {
                foundTanks.value = true;
                existingTank.volume = newTank.volume;
              }
            });

            if (foundTanks.value) {
              const newTemp = temp.filter(tank =>
                tank.content.toLowerCase().includes("fuel") || tank.content.toLowerCase().includes("fresh water")
              )
              tanks.value = newTemp
              console.log(newTemp)
            }
          })
      });
  } catch (error) {
    console.error('Failed to fetch tank data:', error);
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
)

onUnmounted(() => {
  if (resizeObserver) {
    resizeObserver.disconnect();
  }
});

const shipTankVolumeStyle = (tank) => {
  const percent = (tank.volume / tank.capacity) * 100
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
    backgroundColor: background
  }
}

</script>
<template>
  <div class="ship-tank-data" id="ship-tank-data" v-if="foundTanks">
    <div class="ship-tanks">
      <div class="ship-tank" v-for="(tank, index) in tanks" :key="`Tank${index}`">
        <div class="ship-tank-volume" :style="shipTankVolumeStyle(tank)" />
      </div>
    </div>
    <p>Ship tank data overview</p>
    <div class="ship-drawing-container" ref="containerRef">
      <ShipOverview :width="shipWidth" :height="shipHeight" />
    </div>
    <p>Ship tank data side view</p>
    <div class="ship-drawing-container">
      <ShipSideview :width="shipWidth" :height="shipHeight" />
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
  height: fit-content;
  width: 100%;
  display: flex;
  justify-content: center;
  flex-direction: column;
  align-items: center;
}

.ship-tanks {
  position: absolute;
  height: 200px;
  width: 600px;
  left: 0;
  top: 0;
  display: flex;
  flex-wrap: wrap;
  gap: 2px;
}

.ship-tank {
  height: 50px;
  width: 50px;
  border: 1px solid black;
  display: flex;
  align-items: flex-end;
}

.ship-drawing-container {
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