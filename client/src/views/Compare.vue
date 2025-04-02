<script setup>
import LeftContainer from "../components/compare/LeftContainer.vue";
import RightContainer from "../components/compare/RightContainer.vue";

const props = defineProps({
  boats: Array,
});
</script>

<template>
  <div class="main-container">
    <div class="top-container">
      <div class="bottom-container">
        <div class="left-container">
          <LeftContainer :boats="props.boats" />
        </div>
        <div class="right-container">
          <RightContainer />
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
