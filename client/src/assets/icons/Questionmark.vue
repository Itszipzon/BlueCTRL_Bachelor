<script>
 export default {
  props: {
    size: {
      type: String,
      default: "24px",
    },
  }
 }
</script>
<template>
  <svg :width="size" :height="size" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg" fill="#000000">
    <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
    <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
    <g id="SVGRepo_iconCarrier">
      <path fill-rule="evenodd"
        d="M11 11H9v-.148c0-.876.306-1.499 1-1.852.385-.195 1-.568 1-1a1.001 1.001 0 00-2 0H7c0-1.654 1.346-3 3-3s3 1 3 3-2 2.165-2 3zm-2 4h2v-2H9v2zm1-13a8 8 0 100 16 8 8 0 000-16z"
        fill="#000000"></path>
    </g>
  </svg>
</template>