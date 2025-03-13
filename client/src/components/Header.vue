<script setup>
import { ref } from 'vue';
import HamburgerMenu from './icons/HamburgerMenu.vue';
import IconCommunity from './icons/IconCommunity.vue';

const isSidebarOpen = ref(false);

const toggleSidebar = () => {
  isSidebarOpen.value = !isSidebarOpen.value;
};
</script>

<template>
  <div class="header-container">
    <div :class="['sidebar', { 'sidebar-open': isSidebarOpen }]">
      <div class="nav-items">
        <div class="nav-item" @click="toggleSidebar">
          <HamburgerMenu class="icon"/>
        </div>
        <div class="nav-item" >
         <IconCommunity class="icon"/>
        </div>
      </div>
    </div>
  </div>
</template>



<style scoped>
/* .topbar {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 75px;
  background-color: #114155;
  z-index: 1001;
} */

.sidebar {
  position: fixed;
  top: 0;
  left: 0;
  width: 75px;
  height: 100vh;
  background-color: #114155;
  z-index: 1001;
  transition: width 0.3s ease-in-out;
}

.sidebar-open {
  width: 325px;
}

/*
.header-container {
  display: flex;
  height: 100vh;
  width: 100%;
}
  */


.nav-items {
  display: flex;
  flex-direction: column;
  gap: 20px;
  width: 100%;
  margin-top: 20px;
  position: relative;
}

.nav-item {
  display: flex;
  align-items: center;
  
  cursor: pointer;
  position: relative;
  height: 40px;
  padding-left: 23px;
  
}

.icon {
  fill: white;
  width: 30px;
  height: 30px;
}

.nav-item.active {
  color: #fff;
}

.nav-item.active::before {
  content: '';
  position: absolute;
  left: 0;
  width: 3px;
  height: 20px;
  background-color: #fff;
  border-radius: 0 3px 3px 0;
}
</style>