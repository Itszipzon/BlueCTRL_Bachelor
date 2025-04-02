<script setup>

const props = defineProps({
  selectedBoat: Object,
});
</script>
<template>
<div class="right-container-component">
    <div v-if="selectedBoat">
      <h3>Selected Boat</h3>
      <p><strong>Name:</strong> {{ selectedBoat.vesselName }}</p>
      <p><strong>Country Code:</strong> {{ selectedBoat.countryCode }}</p>
    </div>
    <div v-else>
      <p>No boat selected</p>
    </div>
</div>
</template>
<style scoped>
.right-container-component {
    padding: 20px;
    width: 100%;
    height: 100%;
    color: black;
    display: flex;
    flex-direction: column;
}
</style>