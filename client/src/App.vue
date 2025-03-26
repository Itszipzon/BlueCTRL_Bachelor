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
</script>

<template>
  <div id="app" class="app">
    <div v-if="isLoggedIn">
      <Header />
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
