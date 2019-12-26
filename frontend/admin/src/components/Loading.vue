<template>
  <div class="overlay" v-if="active">
    <v-progress-circular
      :width="3"
      color="cyan"
      indeterminate
    ></v-progress-circular>
  </div>
</template>

<script>
export default {
  name : "ui-loading",
  props : {
    active : Boolean
  }
}
</script>

<style scoped>
  .overlay {
    position: absolute;
    width: 100%; height: 100%;
    top: 0; left: 0;
    background: rgba(100, 100, 100, 0.3);
    z-index: 999;
  }

  .overlay .v-progress-circular{
    top: calc(50% - 22px);
    left: calc(50% - 22px);
  }
</style>
