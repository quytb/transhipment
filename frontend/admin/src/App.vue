<template>
  <v-app>
    <div :class="{'nav-open': $sidebar.showSidebar}">
      <notifications transition-name="notification-list" transition-mode="out-in"></notifications>
      <router-view></router-view>
    </div>
  </v-app>
</template>

<script>
import('sweetalert2/dist/sweetalert2.css');
import('vue-notifyjs/themes/default.css');
export default {};
</script>

<style lang="scss">
.v-card__title {font-size: 1.1em; font-weight: bold; line-height: 28px;}
.row{ margin: 0 !important; }
label{margin: 0 !important;}
.vue-notifyjs.notifications {
  .alert {
    z-index: 10000;
  }
  .list-move {
    transition: transform 0.3s, opacity 0.4s;
  }
  .list-item {
    display: inline-block;
    margin-right: 10px;
  }
  .list-enter-active {
    transition: transform 0.2s ease-in, opacity 0.4s ease-in;
  }
  .list-leave-active {
    transition: transform 1s ease-out, opacity 0.4s ease-out;
  }

  .list-enter {
    opacity: 0;
    transform: scale(1.1);
  }
  .list-leave-to {
    opacity: 0;
    transform: scale(1.2, 0.7);
  }
  .notification-list-move {
    transition: transform 0.3s, opacity 0.4s;
  }
  .notification-list-item {
    display: inline-block;
    margin-right: 10px;

  }
  .notification-list-enter-active, .notification-list-leave-active {
    transition: opacity 0.4s;
  }
  .notification-list-enter, .notification-list-leave-to /* .list-leave-active for <2.1.8 */
  {
    opacity: 0;
  }
}
</style>
