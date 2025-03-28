<script setup>
import { computed } from 'vue';


const props = defineProps({
    roll: {
        type: Number,
        default: 0,
    },
    pitch: {
        type: Number,
        default: 0,
    },
});


const frontStyle = computed(() => ({
    transform: `rotate(${props.roll}deg)`,
    transition: 'transform 0.2s ease-out', 
}));

const sideStyle = computed(() => ({
    transform: `rotate(${props.pitch}deg)`,
    transition: 'transform 0.2s ease-out',
}));
</script>


<template>
    <div class="boat-tilt-container">
        <!-- Front view for roll -->
        <div class="tilt-view">
            <img class="boat-image" src="@/assets/boat-front-view.svg" alt="Boat Front View" :style="frontStyle" />
            <p class="tilt-label">Roll: {{ roll }}°</p>
        </div>

        <!-- Side view for pitch -->
        <div class="tilt-view">
            <img class="boat-image" src="@/assets/boat-side-view.svg" alt="Boat Side View" :style="sideStyle" />
            <p class="tilt-label">Pitch: {{ pitch }}°</p>
        </div>
    </div>
</template>



<style scoped>
.boat-tilt-container {
    display: flex;
    flex-direction: row;
    gap: 1rem;
    align-items: center;
    justify-content: center;
}

.tilt-view {
    display: flex;
    flex-direction: column;
    align-items: center;
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
</style>