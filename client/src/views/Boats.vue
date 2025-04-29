<script setup>
import { inject, ref, watchEffect, watch, nextTick } from "vue";

const boatsTotal = inject("boats");

const boats = ref(null);
const loadingVessels = ref(true);

watchEffect(() => {
  boats.value = boatsTotal.value.vessels;
  loadingVessels.value = boatsTotal.value.loadingVessels;
});

const props = defineProps({
  boats: Object,
});

const isLoggedIn = ref(true);

console.log("Boats:", boats.value);
</script>

<template>
  <div class="main-container">
    <div class="boat-list-container">
      <h3>Browse boats</h3>
      <div class="boat-list" v-for="boat in boats" :key="boat.id">
        <div class="boat-item">
          <img
            class="boat-flag"
            :src="`https://flagcdn.com/h40/${boat.countryCode.toLowerCase()}.png`"
            alt="flag"
          />
          <span class="boat-name">{{ boat.vesselName }}</span>
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

.boat-list-container {
  background-color: white;
  color: black;
  padding: 20px;
}

.boat-list-container h3 {
  font-size: 16px;

  font-weight: bolder;
  font-family: "Poppins", sans-serif;
}

.boat-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
  padding-top: 10px;
}

.boat-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 6px;
  background-color: #f9f9f9;
}

.boat-flag {
  width: 30px;
  height: auto;
}

.boat-name {
  font-size: 14px;
  font-weight: 500;
}
</style>
