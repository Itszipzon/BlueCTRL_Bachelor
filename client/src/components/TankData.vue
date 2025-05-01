<script setup>
import { ref, onMounted, onUnmounted } from 'vue';
import ShipOverview from '../assets/ShipOverview.vue';
import ShipSideview from '../assets/ShipSideview.vue';

const shipWidth = ref(0);
const shipHeight = ref(0);
const containerRef = ref(null);
let resizeObserver;

onMounted(async () => {

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
});

onUnmounted(() => {
  if (resizeObserver) {
    resizeObserver.disconnect();
  }
});
</script>
<template>
  <div class="ship-tank-data" id="ship-tank-data">
    <div class="ship-tanks">

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
}

.ship-drawing-container {
  width: 100%;
  max-width: 800px;
  height: fit-content;
}
</style>