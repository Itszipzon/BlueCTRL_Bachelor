<script setup>
import { ref, onBeforeUnmount } from "vue";
import BoatIcon from "../assets/icons/BoatIcon.vue";
import LogOut from "../assets/icons/LogOut.vue";
import BoatCompare from "../assets/icons/BoatCompare.vue";
import router from "../router";
import MapIcon from "../assets/icons/MapIcon.vue";

const props = defineProps({
  boats: Object,
});

const isSidebarOpen = ref(false);
let input = ref("");
const searchListOpen = ref(false);
const searchRef = ref(null);

const isSidebarHovered = ref(false);

const selectBoat = (boat) => {
  isSidebarOpen.value = false;
  if (window.location.pathname !== "/") {
    router.push("/");
    setTimeout(() => {
      window.dispatchEvent(new CustomEvent("selectBoat", { detail: boat }));
    }, 250);
  } else {
    window.dispatchEvent(new CustomEvent("selectBoat", { detail: boat }));
  }
};

console.log(router.history.current.fullPath)

const logout = () => {
  window.dispatchEvent(new Event("logout"));
};

function filteredBoats() {
  return props.boats?.vessels.filter((boat) =>
    boat.vesselName.toLowerCase().includes(input.value.toLowerCase())
  );
}

const onMapClick = () => {
  if (window.location.pathname !== "/") {
    router.push("/");
  } else {
    window.dispatchEvent(new Event("resizeMap"));
  }
};

document.addEventListener("mousedown", (e) => {
  if (isSidebarOpen.value && !e.target.closest(".header-container")) {
    isSidebarOpen.value = false;
  }

  if (!searchListOpen.value && e.target.closest(".header-item-3")) {
    searchListOpen.value = true;
  }

  if (searchListOpen.value && !e.target.closest(".header-item-3")) {
    searchListOpen.value = false;
  }
});

onBeforeUnmount(() => {
  document.removeEventListener("mousedown", handleClickOutside);
});
</script>

<template>
  <div class="header-container">
    <div class="header">
      <div class="header-content">
        <div class="header-left">
          <router-link to="/" class="header-item-2 link">
            <h1>X-connect</h1>
          </router-link>
        </div>
        <div class="header-item-3" ref="searchRef">
          <input type="text" v-model="input" placeholder="Search boats.." />
          <div class="remove-input" v-if="input" @click="() => (input = '')">
            <div class="remove-input-line" />
            <div class="remove-input-line" />
          </div>
          <div class="search-results" v-if="searchListOpen && input">
            <div
              class="boatresult"
              v-for="boat in filteredBoats()"
              @click="
                () => {
                  selectBoat(boat);
                  searchListOpen = false;
                }
              "
              :key="boat.id"
            >
              <img
                class="boat-flag"
                :src="`https://flagcdn.com/h40/${boat.countryCode.toLowerCase()}.png`"
              />
              <span class="boat-name">{{ boat.vesselName }}</span>
            </div>
            <div class="item error" v-if="!filteredBoats().length">
              <p>No results found!</p>
            </div>
          </div>
        </div>
        <div class="header-right"></div>
      </div>
    </div>
    <div
      :class="['sidebar', { open: isSidebarOpen || isSidebarHovered }]"
      @mouseenter="isSidebarHovered = true"
      @mouseleave="isSidebarHovered = false"
    >
      <div class="sidebar-content">
        <div class="nav-items">
          <div class="top-items">
            <div class="nav-item">
              <div class="icon-container">
                <div :class="['icon-wrapper clickable', { active: router.history.current.fullPath === '/'}, { large: isSidebarHovered }]" @click="onMapClick">
                  <div class="icon">
                    <MapIcon />
                  </div>
                  <span v-if="isSidebarHovered" class="icon-label">Map</span>
                </div>
                <div :class="['icon-wrapper clickable', { active: router.history.current.fullPath === '/vessels'}, { large: isSidebarHovered }]" @click="() => router.push('/vessels')">
                  <div class="icon">
                    <BoatIcon />
                  </div>
                  <span v-if="isSidebarHovered" class="icon-label">Vessels</span>
                </div>

                
                <router-link to="/compare" :class="['link icon-wrapper clickable', { active: router.history.current.fullPath.includes('/compare')}, { large: isSidebarHovered }]">
                  <div class="icon">
                    <BoatCompare />
                  </div>
                  <span v-if="isSidebarHovered" class="icon-label"
                    >Compare</span
                  >
                </router-link>
              </div>
            </div>
          </div>

          <div class="nav-item">
            <div class="icon-container">
              <div class="icon-wrapper clickable" @click="logout">
                <div class="icon">
                  <LogOut />
                </div>
                <span v-if="isSidebarHovered" class="icon-label">Log out</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      
    </div>
  </div>
</template>

<style scoped>
@import url("https://fonts.googleapis.com/css2?family=Montserrat&display=swap");

.link {
  background-color: transparent;
}

.header-container {
  display: flex;
  flex-direction: column;
  z-index: 1001;
}

.header {
  background-color: #114155;
  height: 50px;
  padding: 0 20px;
  box-sizing: border-box;
  position: fixed;
  top: 0px;
  left: 0px;
  width: 100%;
  display: flex;
  justify-content: space-between;
  border-bottom: 1px solid #ccc;
}

.header-content {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-left: 3px;
  width: 100%;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 20px;
  width: 100%;
}

.header-right {
  display: flex;
  align-items: center;
  background-color: bisque;
  width: 100%;
}

.sidebar {
  position: fixed;
  top: 50px;
  left: 0;
  width: 75px;
  height: calc(100vh - 50px);
  background-color: #114155;
  z-index: 1001;
  transition: width 0.3s ease-in-out;
  display: flex;
}

.sidebar.open {
  width: calc(245px - 75px);
}

.sidebar-content {
  display: flex;
  flex-direction: row;
  height: 100%;
}

.nav-items {
  display: flex;
  flex-direction: column;
  width: 100%;
  align-items: flex-start;
  padding-top: 20px;
  padding-bottom: 20px;
  padding-left: 0;
  justify-content: space-between;
}

.top-items {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  gap: 5px;
}

.sidebar-list {
  display: flex;
  flex-direction: column;
  width: 100%;
}

.boat-list {
  flex-grow: 1;
  list-style: none;
  padding: 0;
  overflow: hidden;
  padding: 10px 10px 0 0;
}

.boat-list.closed {
  width: 0;
  overflow: hidden;
}

.boat-item {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 10px 15px;
  border-radius: 6px;
  color: white;
  cursor: pointer;
  width: 100%;
  transition: all 0.3s ease-in-out;
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
  border-radius: 6px;
}

.boat-name {
  font-size: 14px;
  white-space: nowrap;
}

.nav-item {
  display: flex;
  height: 40px;
  width: 100%;
  justify-content: center;
}

.header-item-1 {
  display: flex;
  align-items: end;
  height: 40px;
}

.header-item-2 {
  display: flex;
  align-items: end;
  height: 40px;
  margin-top: 5px;
  cursor: pointer;
  text-decoration: none;
}

.header-item-2 h1 {
  color: white;
}



.router-link-active,
.router-link-exact-active {
  color: inherit;
  text-decoration: none;
}

.header-item-2:hover {
  text-decoration: none;
  background-color: #114155;
}

.header-item-3 {
  display: flex;
  height: 32px;
  width: 100%;
}

.remove-input {
  position: absolute;
  right: 5px;
  top: calc(32px / 2 - 10px);
  width: 20px;
  height: 20px;
  background-color: rgb(234, 234, 234);
  border-radius: 50%;
  cursor: pointer;
}

.remove-input:hover {
  background-color: rgb(206, 206, 206);
}

.remove-input-line {
  width: 20px;
  height: 2px;
  background-color: #474646;
  display: flex;
  flex-direction: column;
  border-radius: 6px;
}

.remove-input-line:nth-child(1) {
  position: absolute;
  width: 15px;
  top: calc(50% - 1px);
  left: calc(50% - 7.5px);
  transform: rotate(45deg);
}

.remove-input-line:nth-child(2) {
  position: absolute;
  width: 15px;
  top: calc(50% - 1px);
  left: calc(50% - 7.5px);
  transform: rotate(-45deg);
}

.search-results {
  position: absolute;
  top: 100%;
  left: 0;
  background-color: white;
  color: #000;
  border: 1px solid #ccc;
  border-top: none;
  width: 100%;
  max-height: 200px;
  overflow-y: auto;
  z-index: 1002;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.15);
  border-radius: 0 0 6px 6px;
}

.boatresult {
  padding: 10px;
  cursor: pointer;
  border-bottom: 1px solid #eee;
  display: flex;
  gap: 10px;
}

.boatresult:hover {
  background-color: #f0f0f0;
}

.header-item-3 input {
  height: 100%;
  width: 100%;
  border-radius: 6px;
  padding: 10px 30px 10px 10px;
  border: none;
}

.icon-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
  height: fit-content;
  align-items: center;
  width: 100%;
  padding-left: 20px;
}

.active-icon-bar {
  height: 0;
  background-color: white;
  width: 3px;
  margin-right: auto;
  border-top-right-radius: 6px;
  border-bottom-right-radius: 6px;
  transition: height 0.3s;
}

.active-icon-bar.active {
  height: 20px;
}

.icon {
  width: 30px;
  height: 30px;
  display: flex;
  cursor: pointer;
  margin-right: 20px;
}

.icon.clickable:hover {
  background-color: #185c79;
  border-radius: 6px;
}

.login-message {
  text-align: center;
  color: white;
  font-size: 16px;
  padding: 20px;
}

.icon-wrapper {
  display: flex;
  align-items: center;
  width: 32px;
  cursor: pointer;
  transition: all 0.2s ease-in-out;
}

.icon-wrapper.active {
  background-color: #185c79;
  border-radius: 6px;
}

.icon-wrapper.large {
  width: 125px;
}

.icon-label {
  color: white;
  font-size: 14px;
  white-space: nowrap;
  overflow: hidden;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.sidebar.open .icon-label {
  opacity: 1;
}

</style>
