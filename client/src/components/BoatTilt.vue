<script setup>
import { ref, computed } from 'vue';

const pitch = ref(0);
const roll = ref(0);

const hasSensorData = ref(true);

const props = defineProps({
    type: String,
    vesselId: Number
});

if (props.pitch && props.hasSensorData) {
    console.log('Pitch:', props.pitch);
}

const frontStyle = {
    transform: `rotate(${roll.value}deg)`,
    transition: 'transform 0.2s ease-out',
};

const sideStyle = {
    transform: `rotate(${pitch.value}deg)`,
    transition: 'transform 0.2s ease-out',
};

console.log(sideStyle)
</script>


<template>
    <div class="boat-tilt-container">
        <!-- Front view for roll -->
        <div class="tilt-view" v-if="props.type === 'roll' && hasSensorData">
            <img class="boat-image" src="@/assets/boat-front-view.svg" alt="Boat Front View" :style="frontStyle" />
            <p class="tilt-label">Roll: {{ roll }}°</p>
        </div>

        <!-- Side view for pitch -->
        <div class="tilt-view" v-else-if="props.type === 'pitch' && hasSensorData">
            <img class="boat-image" src="@/assets/boat-side-view.svg" alt="Boat Side View" :style="sideStyle" />
            <p class="tilt-label">Pitch: {{ pitch }}°</p>
        </div>

        <div class="no-sensor-data" v-if="!hasSensorData">
            <p>No sensor data available</p>
        </div>
    </div>
</template>



<style scoped>
.boat-tilt-container {
    display: flex;
    flex-direction: row;
    height: 100%;
    width: 100%;
    align-items: center;
    justify-content: center;
    border-radius: 8px;
}

.tilt-view {
    display: flex;
    flex-direction: column;
    align-items: center;
    color: black;
}

.boat-image {
    width: 80px;
    /* adjust as needed */
    height: auto;
}

.tilt-label {
    margin-top: 8px;
    font-weight: 500;
}

.no-sensor-data {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100%;
    width: 100%;
    color: black;
}
</style>