<script>
  export default{
    props: {
      height: {
        type: Number,
        required: true
      },
      width: {
        type: Number,
        required: true
      }
    }
  }
</script>

<template>
  <svg :width="width" :height="height" xmlns="http://www.w3.org/2000/svg">
      <!-- Top line -->
      <line :x1="width * 0.2" y1="10" :x2="width - 10" y2="10" stroke="black" stroke-width="1" />
      <!-- Right line -->
      <line :x1="width - 10" y1="9.5" :x2="width - 10" :y2="height - 10 + 0.5" stroke="black"
        stroke-width="1" />
      <!-- Bottom line -->
      <line :x1="width - 10" :y1="height - 10" :x2="width * 0.2" :y2="height - 10" stroke="black"
        stroke-width="1" />

      <line :x1="width * 0.2 + 0.25" y1="10.25" x2="0" :y2="(height - 10) * 0.5" stroke="black"
        stroke-width="1" />
      <line :x1="width * 0.2 + 0.25" :y1="height - 10 + 0.25" x2="0" :y2="(height - 10) * 0.5"
        stroke="black" stroke-width="1" />
    </svg>
</template>