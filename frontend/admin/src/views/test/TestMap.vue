<template>
<!--  <div>-->
<!--    <div>-->
<!--      <l-map ref="mymap" :zoom="zoom" :center="center" class="left-animate-x"  v-bind:style="{ width: options.left.width }" >-->
<!--        <l-tile-layer :url="url" :attribution="attribution" />-->

<!--        &lt;!&ndash;        vị trí tài xế&ndash;&gt;-->
<!--        <l-marker-->
<!--          v-for="location in locations"-->
<!--          :key="location.id"-->
<!--          :lat-lng="location.position">-->
<!--          <l-popup>-->
<!--            <div>-->
<!--              {{location.name}}-->
<!--            </div>-->
<!--          </l-popup>-->
<!--        </l-marker>-->

<!--      </l-map>-->
<!--    </div>-->
<!--    <v-text-field prepend-icon="person" label="Tài xế" v-model="taiXeIsTracking" type="text" placeholder=" "></v-text-field>-->
<!--  </div>-->
  <div style="position:absolute;height:100%;width: 100%;top:30;left:30">
    <div style="position:relative;width: 100%;transition: all 0.5s ease;" v-bind:style="{ height: options.top.height }">

      <l-map ref="mymap" :zoom="zoom" :center="center" class="left-animate"  v-bind:style="{ width: options.left.width }" >
        <l-tile-layer :url="url" :attribution="attribution" />

        <!--        vị trí tài xế-->
        <l-marker
          v-for="location in locations"
          :key="location.id"
          :lat-lng="location.position">
          <l-popup>
            <div>
              {{location.name}}
            </div>
          </l-popup>
        </l-marker>
      </l-map>
      <div class="right-animate" v-bind:style="{ width: options.right.width, left: options.right.left }">
        <div style="padding:15px;">
            <div class="form-group">
              <v-text-field prepend-icon="person" label="Tài xế" v-model="taiXeIsTracking" type="text" placeholder=" "></v-text-field>
            </div>
        </div>

        <!---------------->
      </div>
      <div class="left-icon">
        <img src="../../assets/img/left-arrow.png" v-on:click="step(1)"/>
      </div>
      <div class="right-icon">
        <img src="../../assets/img/right-arrow.png" v-on:click="step(2)"/>
      </div>
      <!--      <div class="top-icon">-->
      <!--        <img src="../../assets/img/long-arrow-pointing-up.png" v-on:click="top(1)"/>-->
      <!--      </div>-->
      <!--      <div class="bottom-icon">-->
      <!--        <img src="../../assets/img/down-arrow.png" v-on:click="top(2)"/>-->
      <!--      </div>-->
    </div>
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
    import VueMqtt from 'vue-mqtt';
    import Vue from "vue";
    Vue.use(VueMqtt, 'wss://emqx.havazdev.net:8087/mqtt', {clientId: 'mqttjs_' + Math.random().toString(16).substr(2, 8),username:'mqtt',password:'HazClients@123' });
    export default {
        name: "TestMap",
        components: {
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
                topic: 'HSHV_queue_driver',
                zoom: 15,
                timeBefore: 30,
                laixetuyen: {},
                timeAfter: 30,
                activett: 1,
                dadon: [],
                khachs: [],
                don: [],
                tra: [],
                chuadon: [],
                options: {
                    left: {
                        width:"80%",
                        left:'0%'
                    },
                    right: {
                        width:"20%",
                        left:'80%'
                    },
                    top: {
                        height:"100%",
                    },
                    bot: {
                        height:"0%",
                    },
                },
                center: [21.028363, 105.792688],
                polyline: {
                    latlngs: [

                    ],
                    color: "blue"
                },
                url: "http://{s}.tile.osm.org/{z}/{x}/{y}.png",
                attribution:
                    '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors',
                map: null,
                locations: [
                    {
                        id: 1,
                        position: {
                            lat: 21.028363,
                            lng: 105.792688
                        },
                        name:'Ahihi'
                    }
                ],
                buff:'',
                taiXeIsTracking : 934
            }
        },
        mqtt: {
            '+' (data, topic) {
                let objData = JSON.parse(data);
                console.log(objData.taiXeId+' - '+topic);
                if(objData.taiXeId == this.taiXeIsTracking) {
                    this.locations[0].position = {lat: objData.latitude, lng: objData.longitude};
                    this.center = {lat: objData.latitude, lng: objData.longitude};
                }
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
                // if(type == 1){
                //     var map = this.map;
                //     var i = 0;
                //     myVar = setInterval(function(){
                //         i++;
                //         if(i == 55){
                //             clearInterval(myVar);
                //         }
                //         map.invalidateSize();
                //     }, 10);
                // }
            }
        },
        mounted() {
            this.$mqtt.subscribe(process.env.VUE_APP_QUEUE_DRIVER)
        }
    }
</script>

<style scoped>
  .left-animate-x{
    transition: all 0.5s ease;
    position: absolute;
    top:0;
    left:0;
    height:100%;
    /*z-index:1;*/
  }
</style>
