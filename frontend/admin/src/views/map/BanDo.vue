<template>
  <div style="position:absolute;height:100%;width: 100%;top:30;left:30">
    <div style="position:relative;width: 100%;transition: all 0.5s ease;" v-bind:style="{ height: options.top.height }">
      <v-progress-circular
        :size="70"
        :width="7"
        color="purple"
        indeterminate
      ></v-progress-circular>
      <l-map id="map" ref="mymap" v-model="map" :zoom="zoom" :center="center" class="left-animate"  v-bind:style="{ width: options.left.width }" >
        <l-tile-layer :url="url" :attribution="attribution" />
        <l-polyline v-for="(item, index) in tripDisplay" :key="index" v-if="isShowXeTuyen && isShowTrip[index]" :lat-lngs="item.polylineHub" :color="item.color" />


        <!--        vị trí các hub-->
        <div v-for="(item, index) in tripDisplay" :key="index+'ad'">
          <l-marker
            v-for="hub in item.hubs"
            v-if="isShowTrip[index]"
            :key="hub.id"
            :lat-lng="hub.position">
            <l-popup>
              <div>
                {{hub.name}}
              </div>
            </l-popup>
          </l-marker>
        </div>
<!--                vị trí lái xe tuyến-->
        <l-marker v-for="(item, index) in tripDisplay" :key="index+'xet'"
                  v-if="isShowXeTuyen && isShowTrip[index]"
                  :lat-lng="item.positionTaiXe">
          <l-icon class-name="someExtraClass" :class-name="item.color + ' someExtraClass'">
            <img src="../../assets/img/xetuyen.png"/>
          </l-icon>
          <l-popup>
            <div>
              {{item.name}}
            </div>
          </l-popup>
        </l-marker>

        <div v-for="(item, index) in tripDisplay" :key="index+'khh'">
          <l-marker
            v-for="khach in item.khachArray"
            v-if="isShowTrip[index] && khach.khLatitude"
            :key="khach.id"
            :lat-lng="{lat:khach.khLatitude, lng:khach.khLongitude}">
            <l-icon class-name="someExtraClass" :class-name="item.color + ' someExtraClass'">
              <img src="../../assets/img/people.png"/>
            </l-icon>
            <l-popup>
              <div>
                {{khach.tenKh}} - {{khach.khSdt}}
              </div>
            </l-popup>
          </l-marker>
        </div>

        <!--        vị trí lái xe trung chuyển-->
        <div v-for="(item, index) in tripDisplay" :key="index+'xtc'">
          <l-marker
              v-for="xeTC in item.laiXeTCArray"
              :key="xeTC.txId"
              v-if="isShowTrip[index] && xeTC.xtcLongitude"
              :lat-lng="{lat:xeTC.xtcLatitude, lng:xeTC.xtcLongitude}">
            <l-icon class-name="someExtraClass" :class-name="item.color + ' someExtraClass'">
              <img width="50px" src="../../assets/img/car.png"/>
            </l-icon>
            <l-popup>
              <div>{{xeTC.xtcTen}}</div>
            </l-popup>
          </l-marker>
        </div>

        <!--        vị trí các khách hàng-->
        <l-marker
          v-for="khach in tripDisplay.khachArray"
          v-if="isShowXeTC"
          :key="khach.bbvId+'marker'"
          :lat-lng="khach.position"
        >
          <l-icon class-name="someExtraClass">
            <img width="50px" src="../../assets/img/people.png"/>
          </l-icon>
          <l-popup>
            <div>{{khach.tenKh}} - {{khach.khSdt}}</div>
          </l-popup>
        </l-marker>

      </l-map>
      <div class="right-animate" v-bind:style="{ width: options.right.width, left: options.right.left }">
        <div style="padding:15px;">
          <form class="row">
            <div class="col-md-3">
              <div class="form-group">
                <v-autocomplete
                  label="Trước giờ xuất bến"
                  :items="timeBefores"
                  item-value="value"
                  item-text="text"
                  v-model="timeBefore"
                ></v-autocomplete>
              </div>
            </div>
            <div class="col-md-3">
              <div class="form-group">
                <v-autocomplete
                  label="Sau giờ xuất bến"
                  :items="timeAfters"
                  item-value="value"
                  item-text="text"
                  v-model="timeAfter"
                ></v-autocomplete>
              </div>

            </div>
            <div class="col-md-3">
              <v-btn small class="green white--text ml-0" @click="getListTrip()">Tìm kiếm</v-btn>
            </div>
          </form>
          <div class="panel panel-default" style="margin-top: 0px;">
              <div class="panel-body row">
                <div class="col-md-12">
                  <div class="form-group">
                    <v-autocomplete
                      label="Danh sách chuyến"
                      :items="trips"
                      item-value="tripId"
                      :item-text="itemText"
                      @change="onChangeSelectTripId"
                      no-data-text="Vui lòng chọn thời gian tìm kiếm"
                      multiple
                      chips
                      v-model="tripIds"
                      v-on:input="limiter"
                    >
                    </v-autocomplete>
                  </div>
                </div>
                <div class="col-md-3">
                  <div class="form-group">
                    <v-checkbox
                      label="Show xe tuyến"
                      class="ma-0"
                      v-model="isShowXeTuyen"
                    ></v-checkbox>
                  </div>
                </div>
                <div class="col-md-3">
                  <div class="form-group">
                    <v-checkbox
                      label="Show xe trung chuyển"
                      class="ma-0"
                      v-model="isShowXeTC"
                    ></v-checkbox>
                  </div>
                </div>
                <v-data-table
                  :headers="dialogTableHeaders"
                  :items="tripDisplay"
                  v-if="tripDisplay.length"
                  no-data-text="Không có dữ liệu"
                  rows-per-page-text="Số bản ghi"
                  hide-actions
                  class="elevation-1"
                >
                  <template v-slot:items="trip">
                    <tr style="cursor: pointer">
                      <td style="width: 20px">
                        <div class="form-group text-xs-center">
                          <v-checkbox
                            label=""
                            class="ma-0"
                            v-model="isShowTrip[trip.index]"
                          ></v-checkbox>
                        </div>
                      </td>
                      <td>{{ trip.item.taiXeTen }} - {{ trip.item.taiXeId }}</td>
                      <td>{{ trip.item.xeTuyenBKS }}</td>
                      <td>{{ trip.item.sdt }}</td>
                      <td>{{ trip.item.gioXuatBen }}</td>
                      <td @click="openFullScreen()">
                        <div
                          class="px-3 py-1"
                          :class="trip.item.color">
                            {{ trip.item.tuyenTen }}
                        </div>
                      </td>
                    </tr>
                  </template>
                </v-data-table>
              </div>
          </div>
        </div>

        <!---------------->
        <h1 v-if="tripDisplay.length" data-id="2" class="badge badge-info text-wrap edit-item" style="color: rgb(255, 255, 255); cursor: pointer;margin-left:15px;">Danh sách khách:</h1>
        <div class="panel panel-default" style="padding:15px;">
          <div class="panel-body">
            <v-data-table
              :headers="headers"
              :items="tripDisplay"
              v-if="tripDisplay.length"
              no-data-text="Không có dữ liệu"
              rows-per-page-text="Số bản ghi"
              hide-actions
              class="elevation-1"
            >
              <template v-slot:items="khachItem">
                <tr>
                  <td colspan="3">
                    <div
                      class="px-3 py-1"
                      :class="khachItem.item.color">
                      {{khachItem.item.tuyenTen}} <-> {{khachItem.item.gioXuatBen}}
                    </div>
                  </td>
                  <td>{{khachItem.item.khachArray.length > 0 ? khachItem.item.khachArray.length +' khách' : ''}}</td>
                  <td colspan="4"></td>
                </tr>
                <tr v-for="(khach, index) in khachItem.item.khachArray">
                  <td>{{index+1}}</td>
                  <td>{{khach.tenKh}}</td>
                  <td>{{khach.khSdt}}</td>
                  <td>{{khach.xtcTen}}</td>
                  <td>{{khach.role}}</td>
                  <td>{{khach.tenVtc}}</td>
                  <td>{{khach.hubName}}</td>
                  <td>{{khach.khTrangThaiTc}}</td>
                </tr>
              </template>
            </v-data-table>
            <td><v-btn v-if="tripDisplay.length" small class="green white--text ml-0" @click="handleClose()">xử lý</v-btn></td>
          </div>
        </div>
      </div>
      <div class="left-icon">
        <img src="../../assets/img/left-arrow.png" v-on:click="step(1)"/>
      </div>
      <div class="right-icon">
        <img src="../../assets/img/right-arrow.png" v-on:click="step(2)"/>
      </div>
    </div>
    <v-dialog
      v-model="confirmDialog"
      max-width="100%"
    >
      <v-card>
        <v-card-text class="pa-3">
          <vue-tabs class="card-content">
            <v-tab title="Trung chuyển đón">
              <Pickup :isInterval="false"></Pickup>
            </v-tab>
            <v-tab title="Trung chuyển trả">
              <Dropoff :isInterval="false"></Dropoff>
            </v-tab>
          </vue-tabs>
        </v-card-text>
      </v-card>
    </v-dialog>
  </div>
</template>
<script>

  import {
    LMap,
    LTileLayer,
    LCircle,
    LRectangle,
    LPolygon,
    LPolyline,
    LMarker,
    LIcon,
    LPopup
  } from "vue2-leaflet";
  import Pickup from "../transit/Pickup";
  import Dropoff from "../transit/Dropoff";
  import { Loading } from 'element-ui';
  import VueMqtt from 'vue-mqtt';
  import Vue from "vue";
  Vue.use(VueMqtt, 'wss://emqx.havazdev.net:8087/mqtt', {clientId: 'mqttjs_' + Math.random().toString(16).substr(2, 8),username:'mqtt',password:'HazClients@123' });
  var myVar;
  export default {
    name: "BanDo",
    components: {
      Dropoff,
      Pickup,
      LMap,
      LTileLayer,
      LCircle,
      LRectangle,
      LPolygon,
      LPolyline,
      LMarker,
      LIcon,
      LPopup
    },
    data() {
      return {
        zoom: 10,
        timeBefore: 30,
        laixetuyen: {},
        timeAfter: 30,
        activett: 1,
        dadon: [],
        khachs: [],
        don: [],
        tra: [],
        chuadon: [],
        urlHavaz: process.env.VUE_APP_API_ENDPOINT,
        options: {
          left: {
            width:"50%",
            left:'0%'
          },
          right: {
            width:"50%",
            left:'50%'
          },
          top: {
            height:"100%",
          },
          bot: {
            height:"0%",
          },
        },
        trips: [],
        tripId: 1,
        hubs: [],
        tatCaKhach: [],
        tatCaKhachDeVe: [],
        canhbaos: [],
        khachhangs: [],
        center: [10.820991, 106.629591],
        polyline: {
          latlngs: [

          ],
          color: "blue"
        },
        url: "http://{s}.tile.osm.org/{z}/{x}/{y}.png",
        attribution:
          '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors',
        map: null,
        timeBefores:[
          {text:'Không chọn',value:0},
          {text:'10 phút',value:10},
          {text:'20 phút',value:20},
          {text:'30 phút',value:40},
          {text:'60 phút',value:60}
        ],
        timeAfters:[
          {text:'Không chọn',value:0},
          {text:'10 phút',value:10},
          {text:'20 phút',value:20},
          {text:'30 phút',value:40},
          {text:'60 phút',value:60}
        ],
        dialogTableHeaders: [
          { text: '', align: 'center', sortable: false, width: "1%" },
          { text: 'Tên tài xế', align: 'center', sortable: false, width: "60" },
          { text: 'Biển số', align: 'left', sortable: false, width: "160"  },
          { text: 'Số điện thoại', align: 'center', sortable: false, width: "160"  },
          { text: 'Giờ xuất bến', align: 'center', sortable: false, width: "120"  },
          { text: 'Chiều', align: 'center', sortable: false, width: "160"  }
        ],
        dialogTableData : [],
        canhBaoHeaders:[
          { text: 'Type', align: 'center', sortable: false, width: "30" },
          { text: 'Cung', align: 'left', sortable: false, width: "160"  },
          { text: 'Tuyến', align: 'center', sortable: false, width: "160"  },
          { text: 'Giờ xuất', align: 'center', sortable: false, width: "120"  },
          { text: 'Tên khách', align: 'center', sortable: false, width: "160"  },
          { text: 'Sdt', align: 'center', sortable: false, width: "160"  },
          { text: 'Điểm đón', align: 'center', sortable: false, width: "160"  },
          { text: 'Vùng', align: 'center', sortable: false, width: "160"  },
          { text: 'Điểm kết nối', align: 'center', sortable: false, width: "160"  },
          { text: 'Người đón', align: 'center', sortable: false, width: "160"  },
          { text: 'Xử lý', align: 'left', sortable: false }
        ],
        headers: [
          { text: 'Stt', align: 'center', sortable: false, width: "10"  },
          { text: 'Tên khách', align: 'center', sortable: false, width: "160"  },
          { text: 'Số điện thoại', align: 'center', sortable: false, width: "160"  },
          { text: 'Người đón', align: 'center', sortable: false, width: "160"  },
          { text: 'Role', align: 'center', sortable: false, width: "160"  },
          { text: 'Vùng', align: 'center', sortable: false, width: "160"  },
          { text: 'Hub', align: 'center', sortable: false, width: "160"  },
          { text: 'Trạng thái', align: 'center', sortable: false, width: "30"  }
        ],
        itemheaders : [],
        danhSachDonTraHeader: [
          { text: 'Stt', align: 'center', sortable: false, width: "10"  },
          { text: 'Tuyến', align: 'center', sortable: false, width: "160"  },
          { text: 'Giờ xuất bến', align: 'center', sortable: false, width: "160"  },
          { text: 'Tên khách', align: 'center', sortable: false, width: "160"  },
          { text: 'SDT', align: 'center', sortable: false, width: "160"  },
          { text: 'Điểm trả', align: 'center', sortable: false, width: "160"  },
          { text: 'Vùng', align: 'center', sortable: false, width: "160"  },
          { text: 'Người trả', align: 'center', sortable: false, width: "160"  },
          { text: 'Thứ tự trả', align: 'center', sortable: false, width: "160"  },
          { text: 'Trạng thái', align: 'center', sortable: false, width: "160"  },
          { text: 'Lý do', align: 'center', sortable: false, width: "160"  },
        ],
        staticAnchor: [16, 37],
        polylineHub:[],
        isShowXeTuyen : true,
        isShowXeTC : true,
        confirmDialog: false,
        choose:-1,
        tripIds: [],
        tripDisplay: [],
        colorArray:['blue','green','red'],
        isShowTrip:[true,true,true],
        locationTaiXeTuyens: [
            {
                id: 1,
                position: {
                    lat: 21.028363,
                    lng: 105.792688
                },
                name:'Ahihi'
            }
        ],
      };
    },
    mqtt: {
        '+' (data, topic) {
            let objData = JSON.parse(data);
            for (let i = 0; i<this.tripDisplay.length;i++){
                let element = this.tripDisplay[i];
                if (element.taiXeId == objData.taiXeId){
                    console.log('dung roi');
                    // this.locationTaiXeTuyens[i].isShow = true;
                    this.tripDisplay[i].positionTaiXe = {lat: objData.latitude, lng: objData.longitude};
                }
                for(let j = 0; j<element.laiXeTCArray.length;j++){
                    if(element.laiXeTCArray[j].txId == objData.taiXeId){
                        this.tripDisplay[i].laiXeTCArray[j].xtcLatitude = objData.latitude;
                        this.tripDisplay[i].laiXeTCArray[j].xtcLongitude = objData.longitude;
                    }
                }
                i++;
            }
            console.log(this.tripDisplay+' - '+topic);
        }
    },
    methods: {
      step(i) {
        var type = 0;
        if(i == 1){
          if(this.options.left.width == '100%'){
            this.options.left.width = '50%';
            this.options.right.width = '50%';
            this.options.right.left = '50%';
            type = 1;
          }else if(this.options.left.width == '50%'){
            this.options.left.width = '0%';
            this.options.right.width = '100%';
            this.options.right.left = '0%';
            type = 1;
          }
        }
        if(i == 2){
          if(this.options.right.width == '100%'){
            this.options.left.width = '50%';
            this.options.right.width = '50%';
            this.options.right.left = '50%';
            type = 1;
          }else if(this.options.right.width == '50%'){
            this.options.left.width = '100%';
            this.options.right.width = '0%';
            this.options.right.left = '50%';
            type = 1;
          }
        }
        if(type == 1){
          var map = this.map;
          var i = 0;
          myVar = setInterval(function(){
            i++;
            if(i == 55){
              clearInterval(myVar);
            }
            map.invalidateSize();
          }, 10);
        }
      },
      top(i) {
        var type = 0;
        if(i == 1){
          if(this.options.top.height == '100%'){
            this.options.top.height = '75%';
            this.options.bot.height = '25%';
            type = 1;
          }
        }

        if(i == 2){
          if(this.options.top.height == '75%'){
            this.options.top.height = '100%';
            this.options.bot.height = '0%';
            type = 1;
          }
        }
        if(type == 1){
          var map = this.map;
          var i = 0;
          myVar = setInterval(function(){
            i++;
            if(i == 55){
              clearInterval(myVar);
            }
            map.invalidateSize();
          }, 10);
        }
      },
      getListTrip() {
        var instance = axios.create();
        delete instance.defaults.headers.common['X-CSRF-TOKEN'];
        instance.get(this.urlHavaz+'/havaznow/listtrip/'+this.timeBefore+'/'+this.timeAfter)
          .then(response => (this.trips = response.data))
          .catch(function (resp) {
            alert("Not data");
          });
      },
      async onChangeSelectTripId(){
          var self = this;
          this.tripDisplay = this.trips.filter(a => this.tripIds.includes(a.tripId));
          let tripDisplayTmp = [];
          let locationTaiXeTuyensTmp = [];
          let loadingInstance = Loading.service({ fullscreen: true });
          for (let index = 0;index < this.tripDisplay.length;index++) {
              let element = this.tripDisplay[index];
              var instance = axios.create();
              element.positionTaiXe = {lat:0,lng:0};
              instance.get(self.urlHavaz+'/havaznow/listhub/'+element.tripId)
                  .then(async function (res) {
                      let data = res.data;
                      let hubs = [];
                      let array_ = [];
                      let arrayHub = [];
                      var khachArray = [];
                      var laiXeTCArray = [];
                      for(var i = 0; i < data.length; i++){
                          hubs.push(
                              {
                                  id: data[i].id,
                                  position: { lat: data[i].lat, lng: data[i].lng },
                                  name: data[i].station,
                              }
                          );
                          if(data[i].is_business == 1){
                              array_.push([data[i].lat, data[i].lng]);
                              arrayHub.push({lat: data[i].lat,lng: data[i].lng});
                          }
                      }
                      element.hubs = hubs;

                      await instance.post(self.urlHavaz+'/havaznow/listpoint',arrayHub)
                          .then(function (res) {
                              arrayHub = res.data;
                          })
                          .catch(function (resp) {
                              console.log(resp);
                          });

                      await instance.get(self.urlHavaz+'/havaznow/tripdetails/'+element.tripId+'/trangthai/1/dskhach')
                          .then(function (res) {
                              var tatCaXeTC = res.data.dsXeTc ? res.data.dsXeTc : [];
                              if(tatCaXeTC) {
                                  let i;
                                  let j;
                                  for (i = 0; i< tatCaXeTC.length;i++) {
                                      var dsKhachHang = tatCaXeTC[i].dsKhachHang ? tatCaXeTC[i].dsKhachHang : [];
                                      if(tatCaXeTC[i].xtcLatitude && tatCaXeTC[i].xtcLongitude){
                                          laiXeTCArray.push(tatCaXeTC[i]);
                                          for (j = 0; j< dsKhachHang.length;j++){
                                              dsKhachHang[j].xtcTen = tatCaXeTC[i].xtcTen;
                                              dsKhachHang[j].xtcSdt = tatCaXeTC[i].xtcSdt;
                                              dsKhachHang[j].xtcBks = tatCaXeTC[i].xtcBks;
                                              dsKhachHang[j].role = tatCaXeTC[i].role;
                                          }
                                      }
                                      khachArray = khachArray.concat(dsKhachHang);
                                  }
                              }
                          })
                          .catch(function (resp) {
                              console.log(resp);
                              alert("Not data");
                          });

                      element.khachArray = khachArray;
                      element.laiXeTCArray = laiXeTCArray;
                      element.latlngs = array_;
                      element.polylineHub = arrayHub;
                      element.color = self.colorArray[index];
                      tripDisplayTmp.push(element);
                  })
                  .catch(function (resp) {
                      console.log(resp);
                      alert("Not data");
                  });
          }
          this.locationTaiXeTuyens= locationTaiXeTuyensTmp;
          this.tripDisplay = tripDisplayTmp;
          // console.log(this.locationTaiXeTuyens)
          loadingInstance.close();

      },
      itemText: item => item.tuyenTen + ' <-> ' + item.gioXuatBen,
      handleClose() {
        this.confirmDialog = true;
      },
      showKhachHang(id){
        this.choose = id;
      },
      calculateCenter (arg) {
        console.log(arg[0]);
      },
      limiter(e) {
          if(e.length > 3) {
            this.$emit('close', 'Chỉ được chọn tối đa 3 chuyến');
            e.pop();
          }
      }
    },
    mounted() {
        this.$mqtt.subscribe(process.env.VUE_APP_QUEUE_DRIVER);
        this.map = this.$refs.mymap.mapObject;
    }
  };
</script>
<style>
  @import "../../../node_modules/leaflet/dist/leaflet.css";
  body {
    margin: 0px;
    font-family: Helvetica, Verdana, sans-serif;
  }
  .right-icon{
    position:absolute;
    left:50%;
    top: 15px;
    margin-left: 10px;
    cursor:pointer;
    z-index:100;
    width:24px;
  }
  .top-icon{
    position: absolute;
    width: 24px;
    left: 50%;
    margin-left: -12px;
    top: 0px;
    z-index: 100000;
    cursor: pointer;
  }
  .bottom-icon{
    position: absolute;
    width: 24px;
    left: 50%;
    margin-left: -12px;
    top: 30px;
    z-index: 100000;
    cursor:pointer;
  }
  .left-icon{
    position:absolute;
    left:50%;
    top: 15px;
    margin-left: -33px;
    cursor:pointer;
    z-index:100;
    width:24px;
  }
  .panel-default{
    padding-top: 0px !important;
    margin-top: -13px;
    margin-bottom: -20px !important;
  }
  img{
    width:100%;
    height:auto;
  }
  .left-animate{
    transition: all 0.5s ease;
    position: absolute;
    top:0;
    left:0;
    height:100%;
    z-index:1;
  }
  .right-animate{
    transition: all 0.5s ease;
    position: absolute;
    top:0;
    height:100%;
    overflow: auto;
  }
  #side {
    float: left;
    width: 208px;
  }
  .leaflet-control-attribution{
    display:none;
  }
  #full_div {
    position: absolute;
    overflow-x: auto;
    top: 0;
    right: 0;
    left: 208px;
    bottom: 0;
    padding-left: 8px;
    border-left: 1px solid #ccc;
  }
  ul {
    list-style-type: none;
    margin: 0;
    padding: 0;
  }
  .form-control{
    height:30px;}
  label {
    font-size: 12px !important;
  }
  li {
    font: 200 15px/1.5 Helvetica, Verdana, sans-serif;
    /*border-bottom: 1px solid #ccc;*/
  }
  li:last-child {
    border: none;
  }
  li a {
    font-size: 8px;
    padding-left: 8px;
    text-decoration: none;
    color: #000;
    display: block;
    -webkit-transition: font-size 0.3s ease, background-color 0.3s ease;
    -moz-transition: font-size 0.3s ease, background-color 0.3s ease;
    -o-transition: font-size 0.3s ease, background-color 0.3s ease;
    -ms-transition: font-size 0.3s ease, background-color 0.3s ease;
    transition: font-size 0.3s ease, background-color 0.3s ease;
  }
  .someExtraClass {
    font-size: 12px;
    border: 1px solid #333;
    border-radius: 0 20px 20px 20px;
    box-shadow: 5px 3px 10px rgba(0, 0, 0, 0.2);
    text-align: center;
    width: 30px !important;
    height: 30px !important;
    margin: 0 !important;
  }
  li a:hover {
    font-size: 20px;
    background: #f6f6f6;
  }
  .table thead th, .table th, .table td{
    padding:5px;
    font-size:12px !important;
  }
  .footer{
    border: none !important;
  }
  .red{
    background-color: red;
  }
  .blue{
    background-color: blue;
  }
  .green{
    background-color: green;
  }
</style>
