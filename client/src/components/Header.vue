<script setup>
import { ref, onMounted, onBeforeUnmount } from "vue";
import BoatIcon from "../assets/icons/BoatIcon.vue";
import LogOut from "../assets/icons/LogOut.vue";
import BoatCompare from "../assets/icons/BoatCompare.vue";
import router from "../router";

const props = defineProps({
  boats: Array,
});

const isSidebarOpen = ref(false);
const listType = ref("boats");
const selectedBoat = ref(null);
const isLoggedIn = ref(true);
let input = ref("");
const searchRef = ref(null);

const toggleSidebar = (element) => {
  if (!element) {
    isSidebarOpen.value = !isSidebarOpen.value;
  } else if (element === listType.value) {
    isSidebarOpen.value = !isSidebarOpen.value;
  } else {
    listType.value = element;
    isSidebarOpen.value = true;
  }

  console.log(!element);
};

const selectBoat = (boat) => {
  isSidebarOpen.value = false;
  if (window.location.pathname !== "/") {
    router.push("/");
    console.log("redirecting");
    setTimeout(() => {
      window.dispatchEvent(new CustomEvent("selectBoat", { detail: boat }));
    }, 250);
  } else {
    window.dispatchEvent(new CustomEvent("selectBoat", { detail: boat }));
  }
};

const logout = () => {
  window.dispatchEvent(new Event("logout"));
};

function filteredBoats() {
  return props.boats?.value.filter((boat) =>
    boat.vesselName.toLowerCase().includes(input.value.toLowerCase())
  );
}

document.addEventListener("mousedown", (e) => {
  if (isSidebarOpen.value && !e.target.closest(".header-container")) {
    isSidebarOpen.value = false;
  }
});

const handleClickOutside = (event) => {
  if (searchRef.value && !searchRef.value.contains(event.target)) {
    input.value = "";
  }
};

onMounted(() => {
  document.addEventListener("mousedown", handleClickOutside);
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
          <div class="search-results" v-if="input">
            <div
              class="boatresult"
              v-for="boat in filteredBoats()"
              :key="boat.id"
            >
              <p>{{ boat.vesselName }}</p>
            </div>
            <div class="item error" v-if="!filteredBoats().length">
              <p>No results found!</p>
            </div>
          </div>
        </div>
        <div class="header-right"></div>
      </div>
    </div>
    <div :class="['sidebar', { open: isSidebarOpen }]">
      <div class="sidebar-content">
        <div class="nav-items">
          <div class="top-items">
            <div class="nav-item">
              <div class="icon-container">
                <div class="icon" @click="() => toggleSidebar('boats')">
                  <BoatIcon :active="listType === 'boats'" />
                </div>
                <div class="icon">
                  <router-link to="/compare" class="link">
                  <BoatCompare />
                  </router-link>
                </div>
              </div>
            </div>
          </div>

          <div class="nav-item">
            <div class="icon-container">
              <div :class="['active-icon-bar']" />
              <div class="icon" @click="logout">
                <LogOut />
              </div>
            </div>
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
            v-for="boat in props.boats"
            :key="boat.id"
            :class="[
              'boat-item',
              { active: selectedBoat === boat.id, closed: !isSidebarOpen },
            ]"
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
  width: 75px;
  align-items: center;
  padding-top: 20px;
  padding-bottom: 20px;
  justify-content: space-between;
}

.top-items {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  gap: 5px;
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
  border-radius: 6px;
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
  align-items: end;
  height: 32px;
  width: 100%;
  position: relative;
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
}

.boatresult:hover {
  background-color: #f0f0f0;
}

.header-item-3 input {
  height: 100%;
  width: 100%;
  border-radius: 6px;
  padding: 10px;
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

.login-message {
  text-align: center;
  color: white;
  font-size: 16px;
  padding: 20px;
}
</style>
