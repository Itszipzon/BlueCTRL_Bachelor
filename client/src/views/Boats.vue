<script setup>
import { ref } from "vue";

const props = defineProps({
  boats: Object,
});

const isLoggedIn = ref(true);
</script>

<template>
  <div class="main-container">
    <div class="boat-list">
    
    
    </div>
  </div>
</template>

<style scoped>
.main-container {
  height: calc(100vh - 50px);
  display: flex;
  flex-direction: column;
  background-color: #f5f5f5;
}
</style>
