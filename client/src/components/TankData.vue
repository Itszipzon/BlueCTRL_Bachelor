<script setup>
import { ref, onMounted, onUnmounted } from 'vue';

const shipWidth = ref(0);
const shipHeight = ref(0);
let resizeObserver;

onMounted(() => {
  const element = document.getElementById("ship-tank-data");

  if (element) {
    resizeObserver = new ResizeObserver(() => {
      shipWidth.value = element.offsetWidth;
      shipHeight.value = shipWidth.value * 0.35;
    });
    resizeObserver.observe(element);
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
    <svg :width="shipWidth" :height="shipHeight" xmlns="http://www.w3.org/2000/svg">
      <!-- Top line -->
      <line :x1="shipWidth * 0.2" y1="10" :x2="shipWidth - 10" y2="10" stroke="black" stroke-width="1" />
      <!-- Right line -->
      <line :x1="shipWidth - 10" y1="9.5" :x2="shipWidth - 10" :y2="shipHeight - 10 + 0.5" stroke="black"
        stroke-width="1" />
      <!-- Bottom line -->
      <line :x1="shipWidth - 10" :y1="shipHeight - 10" :x2="shipWidth * 0.2" :y2="shipHeight - 10" stroke="black"
        stroke-width="1" />

      <line :x1="shipWidth * 0.2 + 0.25" y1="10.25" x2="0" :y2="(shipHeight - 10) * 0.5" stroke="black"
        stroke-width="1" />
      <line :x1="shipWidth * 0.2 + 0.25" :y1="shipHeight - 10 + 0.25" x2="0" :y2="(shipHeight - 10) * 0.5"
        stroke="black" stroke-width="1" />
    </svg>
    <p>Ship tank data side view</p>
    <svg :width="shipWidth" :height="shipHeight/2" xmlns="http://www.w3.org/2000/svg">
      <g >
        <line :x1="shipWidth * 0.2" y1="10" :x2="shipWidth - 10" y2="10" stroke="black" stroke-width="1" />
        <line :x1="shipWidth - 10" y1="9.5" :x2="shipWidth - 10" :y2="shipHeight/2 - 10 + 0.5" stroke="black"
        stroke-width="1" />
        <line :x1="shipWidth - 10" :y1="shipHeight/2 - 10" :x2="shipWidth * 0.2" :y2="shipHeight/2 - 10" stroke="black"
        stroke-width="1" />

        <line :x1="shipWidth * 0.2 + 0.25" y1="10.25" x2="0" y2="10" stroke="black"
        stroke-width="1" />
        <line :x1="shipWidth * 0.2 + 0.25" :y1="shipHeight/2 - 10" x2="0" y2="10" stroke="black" stroke-width="1" />
      </g>
    </svg>
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
}

.ship-tanks {
  position: absolute;
  height: 200px;
  width: 600px;
  background-color: #f0f0f0;
  left: 0;
  top: 0;
}
</style>