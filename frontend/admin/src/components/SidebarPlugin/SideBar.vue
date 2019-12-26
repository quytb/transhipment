<template>
  <div :class="sidebarClasses"
       :data-background-color="backgroundColor"
       :data-active-color="activeColor">
    <div class="logo">
      <a class="simple-text logo-mini"
         href="#">
        <div class="logo-img">
          <img src="@/assets/img/logo.png" alt="">
        </div>
      </a>
      <a class="simple-text logo-normal" href="/">{{ title }}</a>
    </div>
    <div class="sidebar-wrapper" ref="sidebarScrollArea">
      <slot>

      </slot>
      <ul :class="navClasses">
        <slot name="links">
          <sidebar-item v-for="(link, index) in sidebarLinksTmp"
                        :key="link.name + index"
                        :link="link">

            <sidebar-item v-for="(subLink, index) in link.children"
                          :key="subLink.name + index"
                          :link="subLink">
            </sidebar-item>
          </sidebar-item>
        </slot>

      </ul>
    </div>
  </div>
</template>
<script>
    import Auth from "../../auth";

    export default {
        props: {
            title: {
                type: String,
                default: 'Transport Dashboard'
            },
            type: {
                type: String,
                default: 'sidebar',
                validator: (value) => {
                    let acceptedValues = ['sidebar', 'navbar'];
                    return acceptedValues.indexOf(value) !== -1
                }
            },
            backgroundColor: {
                type: String,
                default: 'black',
                validator: (value) => {
                    let acceptedValues = ['white', 'brown', 'black'];
                    return acceptedValues.indexOf(value) !== -1
                }
            },
            activeColor: {
                type: String,
                default: 'success',
                validator: (value) => {
                    let acceptedValues = ['primary', 'info', 'success', 'warning', 'danger'];
                    return acceptedValues.indexOf(value) !== -1
                }
            },
            logo: {
                type: String,
                default: '@/assets/img/logo.png'
            },
            sidebarLinks: {
                type: Array,
                default: () => []
            }
        },
        data() {
            return {
                linkHeight: 54,
                activeLinkIndex: 0,
                sidebarLinksTmp: []
            }
        },
        components: {},
        computed: {
            sidebarClasses() {
                if (this.type === 'sidebar') {
                    return 'sidebar'
                } else {
                    return 'collapse navbar-collapse off-canvas-sidebar'
                }
            },
            navClasses() {
                if (this.type === 'sidebar') {
                    return 'nav sidebar123'
                } else {
                    return 'nav navbar-nav sidebar123'
                }
            }
        },
        methods: {
            async initScrollBarAsync() {
                await import('perfect-scrollbar/dist/css/perfect-scrollbar.css');
                const PerfectScroll = await import('perfect-scrollbar');
                PerfectScroll.initialize(this.$refs.sidebarScrollArea)
            },
            checkPermissionSidebar: function () {
                this.sidebarLinksTmp = this.sidebarLinks.slice();
                for (let i = 0; i < this.sidebarLinksTmp.length; i++) {
                    if (!this.sidebarLinksTmp[i].permissions) {
                        continue;
                    }

                    if (Auth.hasPermission(this.sidebarLinksTmp[i].permissions)) {
                        for (let j = 0; j < this.sidebarLinksTmp[i].children.length; j++) {
                            if (!Auth.hasPermission(this.sidebarLinksTmp[i].children[j].permissions)) {
                                this.sidebarLinksTmp[i].children.splice(j, 1);
                                j--;
                            }
                        }
                    } else {
                        this.sidebarLinksTmp.splice(i, 1);
                        i--;
                    }
                }
            }
        },
        mounted() {
            this.checkPermissionSidebar();
            this.initScrollBarAsync();
        },
        beforeDestroy() {
            if (this.$sidebar.showSidebar) {
                this.$sidebar.showSidebar = false
            }
        },
        findActiveLink() {
            this.links.forEach((link, index) => {
                if (link.isActive()) {
                    this.activeLinkIndex = index;
                }
            });
        },
    }

</script>
<style>
  @media (min-width: 992px) {
    .navbar-search-form-mobile,
    .nav-mobile-menu {
      display: none;
    }
  }
</style>
