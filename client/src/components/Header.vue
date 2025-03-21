<script setup>
import { ref, onMounted } from "vue";
import hamburgerMenu from "../assets/icons/hamburger-menu.vue";
import BoatIcon from "./icons/BoatIcon.vue";

const isSidebarOpen = ref(false);
const listType = ref("boats");
const boats = ref([
  { id: 1, vesselName: "Boat Name 1", countryCode: "no" },
  { id: 2, vesselName: "Boat Name 2", countryCode: "au" },
  { id: 3, vesselName: "Boat Name 3", countryCode: "es" },
  { id: 4, vesselName: "Boat Name 4", countryCode: "se" },
  { id: 5, vesselName: "Boat Name 5", countryCode: "za" },
  { id: 6, vesselName: "Boat Name 6", countryCode: "us" },
]);
const selectedBoat = ref(null);
const isLoggedIn = ref(true);

const toggleSidebar = () => {
  isSidebarOpen.value = !isSidebarOpen.value;
};

const selectBoat = (boat) => {
  selectedBoat.value = boat.id;
};

/* const fetchBoats = async () => {
  try {
    const username = localStorage.getItem("username");
    const password = localStorage.getItem("password");

    if (!username || !password) return;

    const credentials = btoa(`${username}:${password}`);
    const response = await fetch("/api/bluebox-vessels-minimal", {
      method: "GET",
      headers: {
        Authorization: `Basic ${credentials}`,
        "Content-Type": "application/json",
      },
    });

    if (!response.ok) {
      throw new Error(
        `HTTP error! Status: ${response.status} - ${response.statusText}`
      );
    }

    const data = await response.json();
    boats.value = data.map((vessel) => ({
      id: vessel.id,
      vesselName: vessel.vesselName,
      countryCode: vessel.countryCode || "unknown",
    }));

    console.log("Boats fetched successfully:", boats.value);
  } catch (error) {
    console.error("Error fetching boats:", error);
  }
}; */

// Check login status when component loads
/* onMounted(() => {
  const storedUsername = localStorage.getItem("username");
  const storedPassword = localStorage.getItem("password");
  if (storedUsername && storedPassword) {
    isLoggedIn.value = true;
    fetchBoats();
  }
}); */
</script>

<template>
  <div class="header-container">
    <div :class="['sidebar', { open: isSidebarOpen }]">
      <div class="sidebar-content">
        <div class="nav-items">
          <div class="nav-item">
            <hamburgerMenu :width="'30px'" :height="'25px'" />
          </div>
          <div class="nav-item" @click="toggleSidebar">
            <BoatIcon class="icon" />
          </div>
        </div>
      </div>

      <div class="sidebar-list">
        <div v-if="!isLoggedIn && isSidebarOpen" class="login-message">
          Log in first to view the boats
        </div>

        <ul
          v-else-if="listType === 'boats'"
          :class="['boat-list', { closed: !isSidebarOpen }]"
        >
          <li
            v-for="boat in boats"
            :key="boat.id"
            :class="[ 'boat-item', { active: selectedBoat === boat.id, closed: !isSidebarOpen }]"
            @click="selectBoat(boat)"
          >
            <img
              class="boat-flag"
              :src="`https://flagcdn.com/h40/${boat.countryCode.toLowerCase()}.png`"
            />
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
}

.boat-item:hover,
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

.login-message {
  text-align: center;
  color: white;
  font-size: 16px;
  padding: 20px;
}
</style>
