<script setup>
const props = defineProps({
  selectedBoats: Array,
});
</script>
<template>
  <div class="right-container-component">
    <div v-if="selectedBoats && selectedBoats.length > 0">
      <h3>Selected Boats</h3>
      <ul>
        <li v-for="boat in selectedBoats" :key="boat.id">
          <p><strong>Name:</strong> {{ boat.vesselName }}</p>
          <p><strong>Country Code:</strong> {{ boat.countryCode }}</p>
        </li>
      </ul>
    </div>
    <div v-else>
      <p>No boats selected</p>
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
