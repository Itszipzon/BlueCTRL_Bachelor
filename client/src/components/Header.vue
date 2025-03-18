<script setup>
import { ref } from "vue";
//import HamburgerMenu from "./icons/HamburgerMenu.vue";
import hamburgerMenu from "../assets/icons/hamburger-menu.vue";
import BoatIcon from "./icons/BoatIcon.vue";

const isSidebarOpen = ref(false);
const listType = ref("boats");

const toggleSidebar = () => {
  isSidebarOpen.value = !isSidebarOpen.value;
};

const boats = [
  { id: 1, vesselName: "Boat Name 1", countryCode: "no" },
  { id: 2, vesselName: "Boat Name 2", countryCode: "au" },
  { id: 3, vesselName: "Boat Name 3", countryCode: "es" },
  { id: 4, vesselName: "Boat Name 4", countryCode: "se" },
  { id: 5, vesselName: "Boat Name 5", countryCode: "za" },
  { id: 6, vesselName: "Boat Name 6", countryCode: "us" },
];

const selectedBoat = ref(null);

const selectBoat = (boat) => {
  selectedBoat.value = boat.id;
};

document.addEventListener("click", (e) => {
  if (isSidebarOpen.value && !e.target.closest(".sidebar")) {
    isSidebarOpen.value = false;
  }
});
</script>

<template>
  <div class="header-container">
    <div :class="['sidebar', { 'open': isSidebarOpen }]">
      <div class="sidebar-content">
        <!-- Navigation Icons -->
        <div class="nav-items">
          <div class="nav-item" @click="toggleSidebar">
            <!-- <HamburgerMenu class="icon" /> -->
          <hamburgerMenu :active="isSidebarOpen" :width="'30px'" :height="'25px'" />
          </div>
          <div class="nav-item">
            <BoatIcon class="icon" />
          </div>
        </div>

        <!-- Boat List (Visible When Sidebar is Open) -->
      </div>
      <div class="sidebar-list">
        <ul v-if="listType === 'boats'" :class="['boat-list', { closed: !isSidebarOpen }]">
          <li
            v-for="boat in boats"
            :key="boat.id"
            :class="['boat-item', { active: selectedBoat === boat.id, closed: !isSidebarOpen }]"
            @click="selectBoat(boat)"
          >
          <img class="boat-flag" :src="`https://flagcdn.com/h40/${boat.countryCode}.png`" />
            <span class="boat-name">{{ boat.vesselName }}</span>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<style scoped>

.sidebar {
  position: fixed;
  top: 0;
  left: 0;
  width: 75px;
  height: 100vh;
  background-color: #114155;
  z-index: 1001;
  transition: width 0.3s ease-in-out;
  display: flex;
}

.sidebar.open {
  width: calc(325px - 75px);
}

.sidebar-content {
  display: flex;
  flex-direction: row;
  height: 100%;
}

.nav-items {
  display: flex;
  flex-direction: column;
  gap: 20px;
  width: 75px;
  align-items: center;
  padding-top: 20px;
}

.boat-list {
  flex-grow: 1;
  list-style: none;
  padding: 0;
  padding-top: 10px;
  overflow: hidden;
}

.boat-list.closed {
  width: 0;
}

.boat-item {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 10px 15px;
  border-radius: 8px;
  color: white;
  cursor: pointer;
  width: 100%;
  transition: all 0.3s ease-in-out;
  overflow: hidden;
}

.boat-item.closed {
  width: 0;
  padding-left: 0;
  padding-right: 0;
}

.boat-item:hover {
  background: rgba(255, 255, 255, 0.2);
}

.boat-item.active {
  background: rgba(255, 255, 255, 0.2);
}

.boat-flag {
  width: 35px;
}

.boat-name {
  font-size: 14px;
  white-space: nowrap;
}

.nav-item {
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  height: 40px;
}

.icon {
  fill: white;
  width: 30px;
  height: 30px;
}
</style>
