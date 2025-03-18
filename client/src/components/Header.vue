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
  { id: 1, name: "Boat Name 1", flag: "🇳🇴" },
  { id: 2, name: "Boat Name 2", flag: "🇦🇺" },
  { id: 3, name: "Boat Name 3", flag: "🇪🇸" },
  { id: 4, name: "Boat Name 4", flag: "🇸🇪" },
  { id: 5, name: "Boat Name 5", flag: "🇿🇦" },
  { id: 6, name: "Boat Name 6", flag: "🇺🇸" },
];

const selectedBoat = ref(null);

const selectBoat = (boat) => {
  selectedBoat.value = boat.id;
};
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
            <span class="boat-flag">{{ boat.flag }}</span>
            <span class="boat-name">{{ boat.name }}</span>
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
  font-size: 24px;
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
