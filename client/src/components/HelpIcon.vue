<script setup>
import { ref, computed } from 'vue';
import Questionmark from '../assets/icons/Questionmark.vue';

const props = defineProps({
  helpText: {
    type: String,
    required: true,
  },
  size: {
    type: String,
    default: '24px',
  },
  left: {
    type: String,
    default: '0px',
  },
  top: {
    type: String,
    default: '0px',
  },
  width: {
    type: String,
    default: '150px',
  },
});

const helpIconInformationStyle = computed(() => {
  return {
    left: props.left,
    top: props.top,
    width: props.width,
  };
});

const displayInformation = ref(false);

const mouseOver = () => {
  displayInformation.value = true;
};

const mouseLeave = () => {
  displayInformation.value = false;
};

</script>

<template>
  <div class="help-icon" @mouseover="mouseOver" @mouseleave="mouseLeave">
    <div :class="['help-icon-information', { show: displayInformation }]" :style="helpIconInformationStyle">
      <p>{{ helpText }}</p>
    </div>
    <Questionmark :size="size" />
  </div>
</template>

<style scoped>
.help-icon {
  cursor: pointer;
  width: fit-content;
  height: fit-content;
}

.help-icon-information {
  display: none;
  z-index: 1000;
  position: absolute;
}

.help-icon-information.show {
  display: block;
  background-color: white;
  border-radius: 5px;
  padding: 10px;
  box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
}
</style>