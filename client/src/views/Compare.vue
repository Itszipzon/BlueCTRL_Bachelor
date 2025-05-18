<script setup>
/**
 * Imports from Vue and child components used in the compare view.
 */
import { inject, ref, watchEffect, watch, nextTick } from "vue";
import LeftContainer from "../components/compare/LeftContainer.vue";
import RightContainer from "../components/compare/RightContainer.vue";

/**
 * Inject the shared reactive boats data from the global context.
 * @type {Ref<{ vessels: Array<Object>, loadingVessels: boolean }>}
 */
const boatsTotal = inject("boats");

/**
 * Local state to hold vessel data and loading indicator.
 */
const boats = ref(null);
const loadingVessels = ref(true);

/**
 * An array of currently selected boats.
 * @type {Ref<Array<Object>>}
 */
const selectedBoats = ref([]);

/**
 * Indicates whether the page is loading (e.g., after a boat selection change).
 * @type {Ref<boolean>}
 */
const pageIsLoading = ref(false);

/**
 * Watches the injected `boatsTotal` and syncs its values locally.
 */
watchEffect(() => {
  boats.value = boatsTotal.value.vessels;
  loadingVessels.value = boatsTotal.value.loadingVessels;
});

/**
 * Toggles selection state of a boat.
 * Adds the boat to selectedBoats if not selected, otherwise removes it.
 *
 * @param {Object} boat - The boat object to toggle.
 */
const toggleBoatSelection = (boat) => {
  const idx = selectedBoats.value.findIndex((b) => b.id === boat.id);

  if (idx === -1) {
    selectedBoats.value = [...selectedBoats.value, boat];
  } else {
    selectedBoats.value = selectedBoats.value.filter((b) => b.id !== boat.id);
  }
};

/**
 * Selects or deselects all boats.
 * If all boats are already selected, it clears the selection.
 * Otherwise, selects all.
 *
 * @param {Array<Object>} allBoats - The complete list of boats to potentially select.
 */
const selectAllBoats = (allBoats) => {
  selectedBoats.value = allBoats.filter(
    (boat) => !selectedBoats.value.some((b) => b.id === boat.id)
  ).length === 0
    ? []
    : [...allBoats];
};

/**
 * Watcher on selectedBoats to simulate a brief loading state whenever the selection changes.
 * Helps indicate to the user that comparison data might be updating.
 */
watch(
  selectedBoats,
  async () => {
    pageIsLoading.value = true;
    await nextTick(); // Wait for DOM updates
    setTimeout(() => {
      pageIsLoading.value = false;
    }, 700); // Simulated loading duration
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
