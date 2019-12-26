<template>
  <nav class="navbar navbar-expand-lg navbar-light height_75px navbar-default" style="height: 75px">
    <div class="container-fluid">
      <button class="blue-grey btn btn-fill btn-icon btn-side-bar" @click="minimizeSidebar">
        <i :class="'ti-menu-alt'"></i>
      </button>
      <a class="navbar-brand" href="#">{{routeName}}</a>
      <button class="navbar-toggler navbar-burger"
              type="button"
              @click="toggleSidebar"
              :aria-expanded="$sidebar.showSidebar"
              aria-label="Toggle navigation">
        <i :class="'ti-menu-alt'"></i>
      </button>
      <div class="collapse navbar-collapse">
        <ul class="nav navbar-nav ml-auto topbar">
          <li class="nav-item">
            <a href="#" class="nav-link">
              <i class="ti-user"></i>
              <p>&nbspXin chào,&nbspAdmin</p>
            </a>
          </li>
          <div class="ring">
          <drop-down :is-drop-down-for-noti="true" class="dropdown-noti" tag="li" :title="notis.length > 0 ? notis.length+'' : ''" icon="ti-bell" @click='loadData'>
            <li v-for=" item in data " class="notifications">
              <div :class="{'readed': item.statusRead == true,  'unRead': item.statusRead == false}" @click="updateStatusRead(item)">
              <router-link :to="{path: item.mainUrl, query: { bvvids: item.bvvIds }}" target='_blank'>{{item.content}}</router-link>
              </div>
            </li>
          </drop-down>
          </div>
          <li class="nav-item">
            <a href="#" @click="confirmDialog = true" class="nav-link btn-rotate">
              <i class="ti-shift-right"></i>
              <p>&nbspĐăng xuất</p>
            </a>
          </li>
        </ul>
      </div>
      <v-dialog
        v-model="confirmDialog"
        max-width="500"
      >
        <v-card>
          <v-card-title class="pa-3">
            <v-layout row class="pa-0">
              <v-flex xs12>Bạn có chắc chắn muốn đăng xuất?</v-flex>
              <v-spacer></v-spacer>
            </v-layout>
          </v-card-title>
          <v-divider class="ma-0"></v-divider>

          <v-card-actions>
            <v-spacer></v-spacer>

            <v-btn
              color="red white--text"
              small
              @click="logout()"
            >
              Đăng xuất
            </v-btn>

            <v-btn
              color=""
              small
              @click="confirmDialog = false"
            >
              Hủy
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </div>
    <audio muted="muted" ref="audioElm" src="../../assets/mp3/notification.mp3"></audio>
  </nav>
</template>
<script>
import Auth from "../../auth";
import SockJS from 'sockjs-client'
import Stomp from 'webstomp-client'
export default {
  computed: {
    routeName() {
      const { name } = this.$route;
      return this.capitalizeFirstLetter(name);
    }
  },
  data() {
    return {
      activeNotifications: false,
      received_messages: "",
      send_message: null,
      connected: false,
      createDialog: false,
      notis:[],
      numberOfItemsUnread:"",
      data:[],
      audio : new Audio(require('../../assets/mp3/notification.mp3')),
      confirmDialog: false
    };
  },
  methods: {
    capitalizeFirstLetter(string) {
      return string.charAt(0).toUpperCase() + string.slice(1);
    },
    toggleNotificationDropDown() {
      this.activeNotifications = !this.activeNotifications;
    },
    closeDropDown() {
      this.activeNotifications = false;
    },
    toggleSidebar() {
      this.$sidebar.displaySidebar(!this.$sidebar.showSidebar);
    },
    hideSidebar() {
      this.$sidebar.displaySidebar(false);
    },
    minimizeSidebar () {
      this.$sidebar.toggleMinimize()
    },
    logout() {
      Auth.logout();
      this.$router.push('/login');
    },
    connect () {
        this.socket = new SockJS(process.env.VUE_APP_API_ENDPOINT+'/socket');
        this.stompClient = Stomp.over(this.socket);
        let seft = this;
        this.stompClient.connect({}, (frame) => {
          this.connected = true
          this.stompClient.subscribe('/topic/noti', (tick) => {
              let data = JSON.parse(tick.body);
              seft.loadData()
              seft.filterStatusRead();
              seft.notifyVue('bottom', 'left', data);
          })
        }, (error) => {
            this.connected = false
            window.setTimeout(function() {
                this.connect();
            }.bind(this), 2500)
        })
    },
    notifyVue (verticalAlign, horizontalAlign, data) {
      this.$notify(
          {
            title: 'Thông báo',
            message: data.content,
            icon: 'ti-gift',
            duration : 7000,
            horizontalAlign: horizontalAlign,
            verticalAlign: verticalAlign,
            type: 'warning'
          })
    },


      //  this.$router.replace({path:data.mainUrl, params:{ bvvIds: data.bvvIds }});
      async loadData() {
          let data = await this.$axios.get('/notification/list');
          this.data = data.data;
          this.filterStatusRead();
      },
      filterStatusRead() {
          this.notis = this.data.filter(item => item.statusRead == false);
      },
      async updateStatusRead(item) {

        item.statusRead = true;
          let data = {
              notificationId: item.notificationId,
              statusRead: item.statusRead
          }
          await this.$axios.post('/notification/update', data);
          await this.loadData();

      },
  },
  async mounted () {
      this.connect();
      await this.loadData();
  },
    beforeDestroy(){
        if (this.stompClient) {
            this.stompClient.disconnect()
        }
    }
};
</script>
<style>
  @media (max-width: 992px) {
    .btn-side-bar {
      display: none !important;
    }
  }
  html, body {
    overflow: visible !important;
  }

  ul.sidebar123 li {
    width: 100%;
    padding: 8px 0px;
  }

  ul.sidebar123 li a p{
    font-size: 30px;
  }

  ul.topbar li a{
    font-size: 14px;
    font-size: 14px;
  }

  ul.sidebar123 li ul li{
    width: 100%;
    padding: 8px 0px;
    margin-left: 5px;
  }

  ul.sidebar123 li ul li:first-child{
    margin-top: 14px;
  }

  ul.sidebar123 li ul li a :hover{
    font-size: 18px;
  }

  ul.sidebar123 li ul li a{
    width: 100%;
    font-size: 18px;
  }
  .card .alert {
    position: relative !important;
    width: 100%
  }
  .unRead {
    font-size: 18px;
    background-color: #3578e578;
  }
  .notifications {
    margin: 0px;
    padding: 0px;
    border-bottom:  1px solid #ccc !important;
  }
  .ring i, .ring p{
    margin-top: 22px ;
    display: inline;
  }
</style>
