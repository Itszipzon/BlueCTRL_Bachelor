<script>
  export default {
    props: {
      width: {
        type: Number,
        required: true
      },
      height: {
        type: Number,
        required: true
      },
      division: {
        type: Number,
        default: 1.5
      }
    }
  }
</script>

<template>
  <svg :width="width" :height="height/division" xmlns="http://www.w3.org/2000/svg">
      <g >
        <line :x1="width * 0.2" y1="10" :x2="width - 10" y2="10" stroke="black" stroke-width="1" />
        <line :x1="width - 10" y1="9.5" :x2="width - 10" :y2="height/division - 10 + 0.5" stroke="black"
        stroke-width="1" />
        <line :x1="width - 10" :y1="height/division - 10" :x2="width * 0.2" :y2="height/division - 10" stroke="black"
        stroke-width="1" />

        <line :x1="width * 0.2 + 0.25" y1="10.25" x2="0" y2="10" stroke="black"
        stroke-width="1" />
        <line :x1="width * 0.2 + 0.25" :y1="height/division - 10" x2="0" y2="10" stroke="black" stroke-width="1" />
      </g>
    </svg>
</template>
