<script>
export default {
  props: {
    active: Boolean,
    width: String,
    height: String,
    lineheight: String,
  },
  computed: {
    hamburgerstyle() {
      return {
        width: this.width,
        height: this.height,
      };
    },
    hamburgerlinestyle() {
      return {
        height: this.lineheight ? this.lineheight : "3px",
      }
    },
  },
};
</script>

<template>

  <div :style="hamburgerstyle" class="hamburger-menu">
    <div :style="hamburgerlinestyle" :class="['hamburger-menu-line', { active: active }]"></div>
    <div :style="hamburgerlinestyle" :class="['hamburger-menu-line', { active: active }]"></div>
    <div :style="hamburgerlinestyle" :class="['hamburger-menu-line', { active: active }]"></div>
  </div>

</template>

<style scoped>

.hamburger-menu {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  cursor: pointer;
}

.hamburger-menu-line {
  width: 100%;
  background-color: #ffffff;
  border-radius: 2px;
  transition: all 0.3s;
}

.hamburger-menu-line.active:first-child {
  transform: translateY(12px) rotate(45deg);
}

.hamburger-menu-line.active:nth-child(2) {
  width: 0;
}

.hamburger-menu-line.active:last-child {
  transform: translateY(-10px) rotate(-45deg);
}

</style>