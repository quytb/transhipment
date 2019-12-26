<template>
  <div :is="tag" :class="[getSlideClass, {open:isOpen}]" v-click-outside="closeDropDown">
    <slot name="title">
      <a class="dropdown-toggle btn-rotate nav-link" @click="toggleDropDown" data-toggle="dropdown" href="javascript:void(0)">
        <i :class="icon"></i>
        <p class="notification">{{title}}
          <b class="caret"></b>
        </p>
      </a>
    </slot>
    <ul :class="isDropDownForNoti ? 'dropdown-menu dropdown-for-noti' : 'dropdown-menu'" ref="notibarScrollArea">
      <slot></slot>
    </ul>
  </div>
</template>
<script>
    export default{
        name: 'drop-down',
        props: {
            title: String,
            icon: String,
            slide: {
                type: String,
                default: 'down'
            },
            tag: {
                type: String,
                default: 'div'
            },
            isDropDownForNoti: {
                type: Boolean,
                default: false
            }
        },
        computed: {
            getSlideClass () {
                return this.slide === 'down' ? 'dropdown' : 'dropup'
            }
        },
        data () {
            return {
                isOpen: false
            }
        },
        methods: {
            toggleDropDown () {
                this.isOpen = !this.isOpen
            },
            closeDropDown () {
                this.isOpen = false
            },
            async initScrollBarAsync() {
                await import('perfect-scrollbar/dist/css/perfect-scrollbar.css');
                const PerfectScroll = await import('perfect-scrollbar');
                PerfectScroll.initialize(this.$refs.notibarScrollArea,{wheelSpeed : 0.5})
            },
        },
        mounted() {
            this.initScrollBarAsync();
        }
    }
</script>
<style>
  .dropdown-for-noti{
    height: 400px;
    width: 160px;
    overflow: scroll;
  }
</style>
