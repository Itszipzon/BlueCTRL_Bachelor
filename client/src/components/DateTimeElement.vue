<script setup>
import ArrowLeft from '../assets/icons/ArrowLeft.vue';
import ArrowRight from '../assets/icons/ArrowRight.vue';

</script>

<script>

export default {
  props: {
    startTime: {
      type: Object,
      default: () => ({
        month: new Date().getMonth() + 1,
        year: new Date().getFullYear(),
        day: new Date().getDate(),
      }),
    },
    displayActive: {
      type: Boolean,
      default: false,
    },
    activeDates: {
      type: Array,
      default: () => [],
    },
    onHourClick: Function,
    onDayClick: Function,
    onMonthClick: Function,
  },
  data() {
    return {
      displayType: "days",
    };
  },
  methods: {
    handleHourClick(e) {
      if (this.displayActive && !this.isActiveHour(e.getHours())) {
        return;
      }
      if (this.onHourClick) this.onHourClick(e);
    },
    handleDayClick(e) {
      if (this.displayActive && !this.isActiveDay(e.getDate())) {
        return;
      }
      this.displayType = "time";
      this.startTime.day = e.getDate();
      if (this.onDayClick) this.onDayClick(e);
    },
    handleMonthClick(e) {
      if (this.displayActive && !this.isActiveMonth(e - 1)) {
        return;
      }
      this.setDisplayType("days");
      this.startTime.month = e;
      if (this.onMonthClick) this.onMonthClick(e);
    },
    getMonthName(month, amount) {
      const months = [
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
      ];
      return month >= 1 && month <= 12 ? months[month - 1].slice(0, amount || months[month - 1].length) : "Invalid month";
    },
    getMonth() {
      return this.startTime.month - 1;
    },
    getYear() {
      return this.startTime.year;
    },
    getDay() {
      return this.startTime.day;
    },
    prevMonth() {
      if (this.startTime.month === 1) {
        this.startTime.month = 12;
        this.startTime.year -= 1;
      } else {
        this.startTime.month -= 1;
      }
    },
    nextMonth() {
      if (this.startTime.month === 12) {
        this.startTime.month = 1;
        this.startTime.year += 1;
      } else {
        this.startTime.month += 1;
      }
    },
    prevYear() {
      this.startTime.year -= 1;
    },
    nextYear() {
      this.startTime.year += 1;
    },
    getDaysInMonth() {
      return new Date(this.startTime.year, this.startTime.month, 0).getDate();
    },
    getFirstDayOfMonth() {
      return new Date(this.startTime.year, this.startTime.month - 1, 1).getDay();
    },
    getArrayOfDays() {
      const daysInMonth = this.getDaysInMonth();
      let firstDay = this.getFirstDayOfMonth();
      firstDay = firstDay === 0 ? 6 : firstDay - 1;
      const daysArray = Array.from({ length: daysInMonth }, (_, i) => i + 1);
      const emptyDays = Array.from({ length: firstDay }, () => null);
      const totalCells = emptyDays.length + daysArray.length;
      const lastEmptyDays = Array.from({ length: (7 - (totalCells % 7)) % 7 }, () => null);
      return [...emptyDays, ...daysArray, ...lastEmptyDays];
    },
    getTimePickerDayStyle() {
      return {
        width: `calc((100% / 7) - 4.3px)`,
        height: `calc(100% / ${Math.ceil(this.getArrayOfDays().length / 7)} - 5px)`,
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        fontSize: "16px",
        fontWeight: "bold",
      };
    },
    setDisplayType(type) {
      this.displayType = type;
    },
    isActiveHour(hour) {
      if (!this.displayActive) return true;
      return this.activeDates.some(date => date.getHours() === hour && date.getDate() === this.startTime.day && date.getMonth() === this.startTime.month - 1 && date.getFullYear() === this.startTime.year);
    },
    isActiveDay(day) {
      if (day === null) return false;
      if (!this.displayActive) return true;
      return this.activeDates.some(date => date.getDate() === day && date.getMonth() === this.startTime.month - 1 && date.getFullYear() === this.startTime.year);
    },
    isInactiveDay(day) {
      if (day === null) return false;
      if (!this.displayActive) return false;
      return !this.activeDates.some(date => date.getDate() === day && date.getMonth() === this.startTime.month - 1 && date.getFullYear() === this.startTime.year);
    },
    isActiveMonth(month) {
      if (!this.displayActive) return true;
      return this.activeDates.some(date => date.getMonth() === month && date.getFullYear() === this.startTime.year);
    },
    isActiveYear(year) {
      return this.activeDates.some(activeDate => activeDate.getFullYear() === year);
    },
  },
};
</script>


<template>
  <div class="date-time-picker__container">
    <div class="date-time-picker__header" v-if="displayType === 'days'">
      <button cla @click="prevMonth()">
        <ArrowLeft />
      </button>
      <h1 @click="setDisplayType('month')">{{ getMonthName(startTime.month) }} {{ getYear() }}</h1>
      <button @click="nextMonth()">
        <ArrowRight />
      </button>
    </div>

    <div class="date-time-picker__header__center" v-else-if="displayType === 'time'">
      <button cla @click="() => setDisplayType('days')">
        <ArrowLeft />
      </button>
      <h1>{{ getDay() }} {{ getMonthName(startTime.month) }} {{ getYear() }}</h1>
      <div style="width: 30px;">
      </div>
    </div>

    <div class="date-time-picker__header" v-else-if="displayType === 'month'">
      <button cla @click="prevYear()">
        <ArrowLeft />
      </button>
      <h1>{{ getYear() }}</h1>
      <button @click="nextYear()">
        <ArrowRight />
      </button>
    </div>
    <div class="date-time-picker__body">
      <div class="date-time-picker__days-container" v-if="displayType === 'days'">
        <div class="date-time-picker__days">
          <div class="date-time-picker__dates__day-name">Mon</div>
          <div class="date-time-picker__dates__day-name">Tue</div>
          <div class="date-time-picker__dates__day-name">Wed</div>
          <div class="date-time-picker__dates__day-name">Thu</div>
          <div class="date-time-picker__dates__day-name">Fri</div>
          <div class="date-time-picker__dates__day-name">Sat</div>
          <div class="date-time-picker__dates__day-name">Sun</div>
        </div>

      </div>
      <div class="date-time-picker__dates" v-if="displayType === 'days'">
        <div v-for="(day, index) in getArrayOfDays()" :key="index" :style="getTimePickerDayStyle()"
          :class="['date-time-picker__dates__day', { active: isActiveDay(day), inactive: isInactiveDay(day) }]"
          @click="day !== null ? handleDayClick(new Date(getYear(), getMonth(), day)) : null">
          <div class="date-time-picker__dates__day__container">
            <div v-if="day !== null" class="date-time-picker__dates__day__display">
              <span>{{ day }}</span>
            </div>
            <div v-else class="date-time-picker__dates__day__display"></div>
          </div>
        </div>
      </div>
      <div class="date-time-picker__hours" v-else-if="displayType === 'time'">
        <div v-for="(hour, index) in 24" :key="index" :class="['date-time-picker__hour', { active: isActiveHour(index) }]"
          @click="handleHourClick(new Date(getYear(), getMonth(), getDay(), index))">
          <div class="date-time-picker__dates__hour__container">
            <div class="date-time-picker__dates__hour__display">
              <span>{{ index < 10 ? `0${index}` : index }}</span>
            </div>
          </div>
        </div>
      </div>
      <div class="date-time-picker__months" v-else-if="displayType === 'month'">
        <div v-for="(month, index) in 12" :key="index" :class="['date-time-picker__month', { active: isActiveMonth(index) }]"
          @click="handleMonthClick(month)">
          <div class="date-time-picker__dates__month__container">
            <div class="date-time-picker__month__display">
              <span>{{ getMonthName(month, 3) }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.date-time-picker__container {
  width: 100%;
  height: 100%;
  color: black;
  display: flex;
  flex-direction: column;
}

.date-time-picker__header {
  width: 100%;
  height: 50px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 5px;
  border-bottom: 1px solid black;
}

.date-time-picker__header h1 {
  margin: 0;
  font-size: 20px;
  font-weight: bold;
}

.date-time-picker__header button {
  border-radius: 50%;
  border: none;
  cursor: pointer;
  font-size: 20px;
  color: black;
  background-color: transparent;
}

.date-time-picker__header__center {
  width: 100%;
  height: 50px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 5px;
  border-bottom: 1px solid black;
}

.date-time-picker__header__center h1 {
  margin: 0;
  font-size: 20px;
  font-weight: bold;
}

.date-time-picker__header__center button {
  border-radius: 50%;
  border: none;
  cursor: pointer;
  font-size: 20px;
  color: black;
  background-color: transparent;
}

.date-time-picker__body {
  width: 100%;
  height: calc(100%);
  display: flex;
  flex-direction: column;
}

.date-time-picker__days-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 5px;
  padding: 5px;
}

.date-time-picker__dates__day-name {
  width: calc((100% / 7) - 4.3px);
  height: calc(100% / 7 - 5px);
  display: flex;
  justify-content: center;
  align-items: center;
}

.date-time-picker__days {
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.date-time-picker__dates {
  width: 100%;
  height: 100%;
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  padding: 5px;
  gap: 5px;
}

.date-time-picker__dates__day {
  border-radius: 6px;
}

.date-time-picker__dates__day.active {
  color: white;
  background-color: #195874;
  cursor: pointer;
}

.date-time-picker__dates__day.inactive {
  color: black;
  border: 1px solid #195874;
  cursor:default;
}

.date-time-picker__dates__day__display {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: transparent;
}

.date-time-picker__months {
  width: 100%;
  height: 100%;
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  padding: 5px;
  gap: 5px;
}

.date-time-picker__month {
  width: calc((100% / 4) - 4px);
  height: calc((100% / 3) - 5px);
  display: flex;
  justify-content: center;
  align-items: center;
  color: black;
  border: 1px solid #195874;
  border-radius: 6px;
  cursor:default;
}

.date-time-picker__month.active {
  color: white;
  background-color: #195874;
  cursor: pointer;
}

.date-time-picker__hours {
  width: 100%;
  height: 100%;
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  padding: 5px;
  gap: 5px;
}

.date-time-picker__hour {
  min-width: calc((100% / 6) - 4.2px);
  min-height: calc((100% / 4) - 5px);
  display: flex;
  justify-content: center;
  align-items: center;
  color: black;
  border: 1px solid #195874;
  border-radius: 6px;
  cursor:default;
}

.date-time-picker__hour.active {
  color: white;
  background-color: #195874;
  cursor: pointer;
}
</style>