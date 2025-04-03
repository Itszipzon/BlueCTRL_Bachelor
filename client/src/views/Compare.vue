<script setup>
import LeftContainer from "../components/compare/LeftContainer.vue";
import RightContainer from "../components/compare/RightContainer.vue";
import { inject, ref } from "vue";

const boats = inject("boats");

const selectedBoats = ref([]);

const toggleBoatSelection = (boat) => {
  const index = selectedBoats.value.findIndex((b) => b.id === boat.id);
  if (index === -1) {
    selectedBoats.value.push(boat);
  } else {
    selectedBoats.value.splice(index, 1);
  }
};


</script>

<template>
  <div class="main-container">
    <div class="top-container">
      <div class="bottom-container">
        <div class="left-container">
          <LeftContainer :boats="boats" :selectedBoats="selectedBoats" @boat-selected="toggleBoatSelection" />
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
}

.top-container {
  display: flex;
  flex-direction: column;
}

.map-container {
  height: 300px;
  width: 100%;
}

.bottom-container {
  height: calc(100dvh - 50px);
  width: 100%;
  display: flex;
  background-color: #f5f5f5;
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
</style>
