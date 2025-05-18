<script setup>
/**
 * Imports Vue Composition API functions, components, router, and Axios.
 */
import { onMounted, provide, ref } from "vue";
import Header from "./components/Header.vue";
import Login from "./views/Login.vue";
import router from "./router";
import axios from "axios";

/**
 * Reactive boolean to track if the user is logged in.
 * Initialized to false by default.
 */
const isLoggedIn = ref(false);

/**
 * Reactive object to store boats (vessels) data and loading state.
 * - loadingVessels: tracks if the API call is in progress.
 * - dummyData: toggles fake vessel names.
 * - vessels: array to store fetched vessel data.
 */
const boats = ref({
  loadingVessels: true,
  dummyData: true,
  vessels: [],
});

/**
 * onMounted lifecycle hook:
 * - Checks for a saved session in localStorage.
 * - If session exists, sets user as logged in and fetches boats.
 */
onMounted(() => {
  if (localStorage.getItem("SESSION")) {
    isLoggedIn.value = true;
    gatherBoats();
  }
});

/**
 * Event listener for global "logout" events.
 * - Clears session from localStorage.
 * - Redirects to homepage.
 * - Resets login and boat state.
 */
window.addEventListener("logout", () => {
  localStorage.removeItem("SESSION");
  router.push("/");
  isLoggedIn.value = false;
  boats.value = {
    loadingVessels: true,
    vessels: [],
  };
});

/**
 * Event listener for global "login" events.
 * - Sets the user login state to true.
 */
window.addEventListener("login", () => {
  isLoggedIn.value = true;
});

/**
 * Toggles login state.
 * - If logging in, fetch boats.
 * - If logging out, clear session and redirect.
 */
const toggleLogin = () => {
  isLoggedIn.value = !isLoggedIn.value;
  if (isLoggedIn.value) {
    gatherBoats();
  } else {
    localStorage.removeItem("SESSION");
    router.push("/");
  }
};

/**
 * Fetches boat (vessel) data from backend API.
 * Adds dummy vessel names if dummyData is enabled.
 * Handles errors by logging out the user.
 */
function gatherBoats() {
  axios
    .get("http://localhost:8080/api/bluebox-vessels-minimal", {
      headers: {
        Authorization: `Basic ${localStorage.getItem("SESSION")}`,
      },
    })
    .then((response) => {
      if (response.status !== 200) {
        console.error("Error fetching boats:", response.status);
      }

      boats.value.loadingVessels = false;
      boats.value.vessels = response.data;

      // If dummyData flag is on, override vessel names for testing/demo purposes
      if (boats.value.dummyData) {
        const dummyVesselNames = [
          "SS Viking",
          "SS Ocean Explorer",
          "SS Nordskog",
          "Imrik Explorer",
          "Singh Seek",
          "SS Ocean Voyager",
          "SS Oceanic",
          "SS Oceanic Explorer",
        ];
        boats.value.vessels.forEach((vessel, index) => {
          vessel.vesselName = dummyVesselNames[index % dummyVesselNames.length];
        });
      }
    })
    .catch((error) => {
      console.error("Error fetching boats:", error);
      localStorage.removeItem("SESSION");
      isLoggedIn.value = false;
      router.push("/");
    });
}

/**
 * Provides the boats data to child components using Vue's dependency injection.
 */
provide("boats", boats);
</script>

<template>
  <div id="app" class="app">
    <div v-if="isLoggedIn">
      <Header :boats="boats" />
      <div class="mainPage">
        <router-view />
      </div>
    </div>
    <div v-else class="login">
      <Login :doLogin="toggleLogin" />
    </div>
  </div>
</template>

<style scoped>
#app {
  padding: 0;
  margin: 0;
}

.mainPage {
  height: calc(100dvh - 50px);
  width: calc(100dvw - 75px);
  /* margin-top: 75px; */
  margin-left: 75px;
  margin-top: 50px;
  background-color: #e8eff2;
  overflow-y: auto;
}

.login {
  height: 100vh;
  width: 100vw;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #e8eff2;
}
</style>
