<script setup>
import { inject, ref, watchEffect, watch, nextTick } from "vue";
import LeftContainer from "../components/compare/LeftContainer.vue";
import RightContainer from "../components/compare/RightContainer.vue";

const boatsTotal = inject("boats");
const boats = ref(null);
const loadingVessels = ref(true);

const selectedBoats = ref([]);
const pageIsLoading = ref(false);

watchEffect(() => {
  boats.value = boatsTotal.value.vessels;
  loadingVessels.value = boatsTotal.value.loadingVessels;
});

const toggleBoatSelection = (boat) => {
  const idx = selectedBoats.value.findIndex((b) => b.id === boat.id);

  if (idx === -1) {
    selectedBoats.value = [...selectedBoats.value, boat];
  } else {
    selectedBoats.value = selectedBoats.value.filter((b) => b.id !== boat.id);
  }

  console.log("Selected boats:", selectedBoats.value);
};

const selectAllBoats = (allBoats) => {
  selectedBoats.value = allBoats.filter(
    (boat) => !selectedBoats.value.some((b) => b.id === boat.id)
  ).length === 0
    ? [] 
    : [...allBoats]; 
};


watch(
  selectedBoats,
  async () => {
    pageIsLoading.value = true;
    await nextTick();
    setTimeout(() => {
      pageIsLoading.value = false;
    }, 700);
  },
  { deep: true }
);
</script>

<template>
  <div class="main-container">
    <div v-if="pageIsLoading" class="spinner-overlay">
      <div class="spinner"></div>
    </div>

    <div class="top-container">
      <div class="bottom-container">
        <div class="left-container">
          <LeftContainer
            :boats="boats"
            :selectedBoats="selectedBoats"
            @boat-selected="toggleBoatSelection"
            @all-boats-selected="selectAllBoats"
          />
        </div>

        <div class="right-container">
          <RightContainer :selectedBoats="selectedBoats" />
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.main-container {
  height: calc(100vh - 50px);
  display: flex;
  flex-direction: column;
  position: relative;
}
.top-container {
  display: flex;
  flex-direction: column;
}
.bottom-container {
  height: calc(100dvh - 50px);
  width: 100%;
  display: flex;
  background: #f5f5f5;
  overflow: auto;
}

.left-container {
  width: 100%;
  border-right: 0.5px solid #ccc;
}
.right-container {
  width: 100%;
  border-left: 0.5px solid #ccc;
}

.spinner-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(255, 255, 255, 0.75);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 10;
}

.spinner {
  width: 50px;
  height: 50px;
  border: 6px solid #3498db;
  border-top: 6px solid transparent;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}
</style>
