<script setup>
import IconDocument from '@/components/icons/IconDocumentation.vue';
import IconShip from '@/components/icons/IconEcosystem.vue';
import IconUser from '@/components/icons/IconCommunity.vue';
import IconGroup from '@/components/icons/IconSupport.vue';
import IconDownload from '@/components/icons/IconTooling.vue';
</script>

<template>
  <div class="header-container">
    <div class="sidebar">
      <div class="logo">
        <div class="logo-icon"></div>
      </div>
      <div class="nav-items">
        <div class="nav-item active">
          <IconDocument />
        </div>
        <div class="nav-item">
          <IconShip />
        </div>
        <div class="nav-item">
          <IconUser />
        </div>
        <div class="nav-item">
          <IconGroup />
        </div>
        <div class="nav-item">
          <IconDownload />
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
}

/*
.header-container {
  display: flex;
  height: 100vh;
  width: 100%;
}
  */

.logo {
  width: 40px;
  height: 40px;
  background-color: #fff;
  border-radius: 8px;
  margin-bottom: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.logo-icon {
  width: 24px;
  height: 24px;
  background-color: #0a3d62;
  border-radius: 4px;
}

.nav-items {
  display: flex;
  flex-direction: column;
  gap: 20px;
  width: 100%;
}

.nav-item {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 40px;
  color: #b3c6d1;
  cursor: pointer;
  position: relative;
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