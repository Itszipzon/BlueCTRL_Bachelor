<script setup>
const props = defineProps({
  boats: Array,
  selectedBoats: Array,
});

const emit = defineEmits(["boat-selected"]);

const handleBoatClick = (boat) => {
  emit("boat-selected", boat);
};
</script>
<template>
  <div class="left-container-component">
    <div class="boat-list-container">
      <h3>Filter boats</h3>
      <div class="boat-list">
        <div
          :class="[
            'boat-item',
            { selected: selectedBoats.some((b) => b.id === boat.id) },
          ]"
          v-for="boat in boats"
          :key="boat.id"
          @click="handleBoatClick(boat)"
        >
          <div class="left-div">
            <img
              class="boat-flag"
              :src="`https://flagcdn.com/h40/${boat.countryCode.toLowerCase()}.png`"
              alt="flag"
            />
            <span class="boat-name">{{ boat.vesselName }}</span>
          </div>
          <div class="right-div">
            <div class="checkbox">
              <span v-if="selectedBoats.some((b) => b.id === boat.id)">✔</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.left-container-component {
  overflow-y: scroll;
  padding: 20px;
  width: 100%;
  height: 100%;
  color: black;
  display: flex;
  flex-direction: column;
  overflow-y: scroll;
}

.boat-list-container {
  background-color: white;
  padding: 20px;
  border-radius: 6px;
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
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 6px;
  background-color: #f9f9f9;
}

.left-div {
  display: flex;  
  align-items: center;
  justify-content: center;
  gap: 10px;
}

.boat-item.selected {
  background-color: #eaeaea;
}

.boat-item:hover {
  background-color: #eaeaea;
  cursor: pointer;
}

.boat-flag {
  width: 30px;
  height: auto;
}

.boat-name {
  font-size: 14px;
  font-weight: 500;
}

.checkbox {
  width: 18px;
  height: 18px;
  border: 2px solid #555;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: bold;
  color: #005380;
  background-color: white;
}
</style>
