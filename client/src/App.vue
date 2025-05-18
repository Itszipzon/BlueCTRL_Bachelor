<script setup>
import { onMounted, provide, ref } from "vue";
import Header from "./components/Header.vue";
import Login from "./views/Login.vue";
import router from "./router";
import axios from "axios";

const isLoggedIn = ref(false);
const boats = ref({
  loadingVessels: true,
  dummyData: true,
  vessels: [],
});

onMounted(() => {
  if (localStorage.getItem("SESSION")) {
    isLoggedIn.value = true;
    gatherBoats();
  }
});

window.addEventListener("logout", () => {
  localStorage.removeItem("SESSION");
  router.push("/");
  isLoggedIn.value = false;
  boats.value = {
    loadingVessels: true,
    vessels: [],
  };
});

window.addEventListener("login", () => {
  isLoggedIn.value = true;
});

const toggleLogin = () => {
  isLoggedIn.value = !isLoggedIn.value;
  if (isLoggedIn.value) {
    gatherBoats();
  } else {
    localStorage.removeItem("SESSION");
    router.push("/");
  }
};

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
