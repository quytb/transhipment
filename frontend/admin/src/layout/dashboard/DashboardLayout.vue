<template>
  <div class="wrapper">
    <side-bar type="sidebar" :sidebar-links="sidebarLinks">
      <form class="navbar-form navbar-left navbar-search-form navbar-search-form-mobile" role="search">
        <div class="input-group">
          <span class="input-group-addon"><i class="fa fa-search"></i></span>
          <input type="text" value="" class="form-control" placeholder="Search...">
        </div>
      </form>
      <mobile-menu></mobile-menu>
    </side-bar>

    <a to="#" name="Log out" icon="ti-panel" class="log_out" />
    <div class="main-panel">
      <top-navbar></top-navbar>
      <dashboard-content @click.native="toggleSidebar"></dashboard-content>
      <content-footer></content-footer>
    </div>

  </div>
</template>

<style lang="scss">
  .log_out{
    position: absolute;
    display: inline-block;
    bottom: 0;
  }
</style>

<script>
  import TopNavbar from "./TopNavbar.vue";
  import ContentFooter from "./ContentFooter.vue";
  import DashboardContent from "./Content.vue";
  import MobileMenu from "./MobileMenu";
  import SidebarLink from "../../sidebarLinks";
  export default {
    components: {
      TopNavbar,
      ContentFooter,
      DashboardContent,
      MobileMenu
    },
      data (){
          return {
              sidebarLinks: SidebarLink
          }
      },
    methods: {
      toggleSidebar() {
        if (this.$sidebar.showSidebar) {
          this.$sidebar.displaySidebar(false);
        }
      }
    },
    mounted(){
      this.$sidebar.displaySidebar(false);
    }
  };
</script>
