<script setup>
import { ref, onMounted } from "vue";
import hamburgerMenu from "../assets/icons/hamburger-menu.vue";
import BoatIcon from "./icons/BoatIcon.vue";

const isSidebarOpen = ref(false);
const listType = ref("boats");
const boats = ref([]); // Store API boats here
const selectedBoat = ref(null);


const toggleSidebar = () => {
  isSidebarOpen.value = !isSidebarOpen.value;
};

const selectBoat = (boat) => {
  selectedBoat.value = boat.id;
};

// Fetch boats from API
const fetchBoats = async () => {
  try {
    const username = "username";  //endre brukernavn og passord til ditt for å teste
    const password = "password";  //HUSK Å IKKE PUSHE BRUKERNAVNET OG PASSORDET DITT
    //Dette skal byttes til info fra log-in
    const credentials = btoa(`${username}:${password}`); // Encode as Base64

    const response = await fetch("/api/bluebox-vessels-minimal", {
      method: "GET",
      headers: {
        "Authorization": `Basic ${credentials}`,
        "Content-Type": "application/json",
      },
    });

    if (!response.ok) {
      throw new Error(`HTTP error! Status: ${response.status} - ${response.statusText}`);
    }

    const data = await response.json();
    boats.value = data.map(vessel => ({
      id: vessel.id,
      vesselName: vessel.vesselName,
      countryCode: vessel.countryCode || "unknown",
    }));

    console.log("Boats fetched successfully:", boats.value); // Debugging
  } catch (error) {
    console.error("Error fetching boats:", error);
  }
};


// Fetch boats when component loads
onMounted(fetchBoats);
</script>

<template>
  <div class="header-container">
    <div :class="['sidebar', { 'open': isSidebarOpen }]">
      <div class="sidebar-content">
        <!-- Navigation Icons -->
        <div class="nav-items">
          <div class="nav-item">
            <hamburgerMenu :width="'30px'" :height="'25px'" />
          </div>
          <div class="nav-item" @click="toggleSidebar">
            <BoatIcon class="icon" />
          </div>
        </div>
      </div>

      <!-- Boat List (Visible When Sidebar is Open) -->
      <div class="sidebar-list">
        <ul v-if="listType === 'boats'" :class="['boat-list', { closed: !isSidebarOpen }]">
          <li
            v-for="boat in boats"
            :key="boat.id"
            :class="['boat-item', { active: selectedBoat === boat.id, closed: !isSidebarOpen }]"
            @click="selectBoat(boat)"
          >
            <img class="boat-flag" :src="`https://flagcdn.com/h40/${boat.countryCode.toLowerCase()}.png`" />
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

.boat-item:hover, .boat-item.active {
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
