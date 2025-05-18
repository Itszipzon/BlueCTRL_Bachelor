<script setup>
import { ref } from 'vue';
import DateTimeElement from '../components/DateTimeElement.vue';

</script>

<script>
const selectedTime = ref(null);
const active = ref(true);

const activeDates = ref([
  new Date(2025, 3, 1, 0),
  new Date(2025, 3, 1, 1),
  new Date(2025, 3, 1, 2),
  new Date(2025, 3, 1, 3),
  new Date(2025, 3, 1, 4),
  new Date(2025, 3, 2, 0),
  new Date(2025, 3, 3, 0),
  new Date(2025, 3, 4, 0),
  new Date(2026, 4, 1, 2),
]);

const toggleActive = () => {
  active.value = !active.value;
};

const clickHour = (e) => {
  selectedTime.value = e;
};

const displayHour = (e) => {
  if (e.getHours() < 10) {
    return `0${e.getHours()}`;
  } else {
    return e.getHours();
  }
};

const dayOfWeek = (e) => {
  const days = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
  return days[e.getDay()];
};

const monthName = (e) => {
  const months = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];
  return months[e.getMonth()];
};
</script>

<template>
  <div class="test">
    <button @click="toggleActive">Click me</button>
    <h1 v-if="selectedTime">{{ dayOfWeek(selectedTime) }} {{ monthName(selectedTime) }} {{ selectedTime.getDate() }} {{ selectedTime.getFullYear() }} {{ displayHour(selectedTime) }}:00</h1>
    <div :class="['date-time-picker', { active: active }]">
      <DateTimeElement :displayActive="false" :onHourClick="clickHour" :activeDates="activeDates" />
    </div>
  </div>
</template>

<style scoped>
  .test {
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
    height: 100%;
    width: 100%;
  }

  .test h1 {
    font-size: 20px;
    color: #000;
    margin-bottom: 20px;
  }

  .test button {
    position: fixed;
    top: 80px;
    left: 50%;
  }

  .date-time-picker {
    width: 300px;
    height: 0px;
    border-radius: 8px;
    background-color: white;
    transition: all 0.5s ease;
    overflow: hidden;
  }

  .date-time-picker.active {
    height: 300px;
  }
</style>