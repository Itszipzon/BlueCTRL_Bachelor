<script setup>
import { onMounted, ref } from "vue";
import Header from "./components/Header.vue";
import Login from "./views/Login.vue";

const isLoggedIn = ref(false);

onMounted(() => {
  if (localStorage.getItem("SESSION")) {
    isLoggedIn.value = true;
  }
});

window.addEventListener("logout", () => {
  localStorage.removeItem("SESSION");
  isLoggedIn.value = false;
});

window.addEventListener("login", () => {
  isLoggedIn.value = true;
});

const toggleLogin = () => {
  isLoggedIn.value = !isLoggedIn.value;
}

const boats = ref([
  { id: 1, vesselName: "Boat Name 1", countryCode: "no" },
  { id: 2, vesselName: "Boat Name 2", countryCode: "au" },
  { id: 3, vesselName: "Boat Name 3", countryCode: "es" },
  { id: 4, vesselName: "Boat Name 4", countryCode: "se" },
  { id: 5, vesselName: "Boat Name 5", countryCode: "za" },
  { id: 6, vesselName: "Boat Name 6", countryCode: "us" },
]);
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
