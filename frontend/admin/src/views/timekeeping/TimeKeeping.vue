<template>
  <div>
    <v-card class="pa-0 elevation-1">
      <v-card-title class="px-12 py-0">

        <v-layout row wrap>
          <v-flex xs2 class="py-0 px-3">
            <div style="display: inline-block; font-size: 18px; font-weight: bold; margin-right: 20px; margin-top: 20px">Bảng chấm công ngày</div>
          </v-flex>
          <v-flex xs2 class="py-0 px-3" style="font-weight: normal">
            <v-menu
              ref="selectedDate"
              v-model="selectedDate.menu"
              lazy
              transition="scale-transition"
              offset-y
              full-width
              min-width="290px"
            >
              <template v-slot:activator="{ on }">
                <v-text-field
                  v-model="selectedDate.value"
                  append-icon="event"
                  readonly
                  style="display: inline-block;"
                  v-on="on"
                ></v-text-field>
              </template>
              <v-date-picker v-model="selectedDate.value" no-title scrollable />
            </v-menu>
          </v-flex>

          <v-flex xs3 class="py-0 px-3" style="font-weight: normal">
            <v-autocomplete
              :items="dsChiNhanh"
              item-value="id"
              item-text="name"
              label="Điểm làm việc"
              v-model="chiNhanh"
              multiple
              chips
              deletable-chips
              clearable
              @change="onChangeChiNhanh($event)"
            ></v-autocomplete>

          </v-flex>
          <v-flex xs3 class="py-0 px-3">
            <v-btn class="green white--text" style="display: inline-block; margin-left: 20px;margin-top: 20px" small @click="loadTimekeepingData()">Tải dữ liệu</v-btn>
          </v-flex>
        </v-layout>

      </v-card-title>

      <v-divider class="ma-0"></v-divider>

      <v-card-text class="pa-3">
        <v-layout row wrap>
          <v-flex xs7 class="py-0 px-3">
            <!-- Filter control -->
            <v-autocomplete
              v-model="selectedDriver"
              :items="drivers"
              item-value="taiXeId"
              item-text="taiXeTen"
              label="Lọc tài xế"
              @change="filterDriver()"
            >
              <template v-slot:item="props">
                <span>
                  {{props.item.taiXeTen}}<span v-if="props.item.taiXeId"> - ID: {{props.item.taiXeId}}</span>
                </span>
              </template>
              <template v-slot:selection="props">
                <span>
                  {{props.item.taiXeTen}}<span v-if="props.item.taiXeId"> - ID: {{props.item.taiXeId}}</span>
                </span>
              </template>
            </v-autocomplete>
          </v-flex>
        </v-layout>
        <!-- Main table -->
        <v-data-table
          :headers="timekeepingTableHeaders"
          :items="timekeepingData"
          :rows-per-page-items="rowPerPageItems"
          :pagination.sync="pagination"
          :loading="tableLoading"
          no-data-text="Không có dữ liệu"
          rows-per-page-text="Số bản ghi"
          hide-actions
          class="elevation-0"
        >
          <template v-slot:items="props">
            <tr @click="editRow(props.index)">
              <td class="text-xs-center">{{ props.index + 1 }}</td>
              <td class="text-xs-left">
                <div class="text" v-if="editingRow != props.index">{{ getDriverName(timekeepingData[props.index].taiXeId) }}</div>
                <div class="text" v-if="editingRow == props.index">
                  <v-autocomplete
                    ref="taiXeId"
                    label="Lái xe"
                    :items="drivers"
                    item-value="taiXeId"
                    item-text="taiXeTen"
                    v-model="timekeepingData[props.index].taiXeId"
                  ></v-autocomplete>
                </div>
              </td>
              <td class="text-xs-center" width = 100>
                <div class="text" v-if="editingRow != props.index">{{ getTruckLicense(timekeepingData[props.index].xeId) }}</div>
                <div class="text" v-if="editingRow == props.index">
                  <v-autocomplete
                    ref="xeId"
                    label="Biển kiểm soát"
                    :items="trucks"
                    item-value="xeId"
                    item-text="bks"
                    v-model="timekeepingData[props.index].xeId"
                  ></v-autocomplete>
                </div>
              </td>
              <td class="text-xs-center" width = 100>
                <div class="text" v-if="editingRow != props.index">
                  <span v-if="!getSessionObj(props.item.caId)">{{props.item.maCa}}</span>
                  <v-tooltip right v-if="getSessionObj(props.item.caId)">
                    <template v-slot:activator="{ on }">
                      <span v-on="on">{{ getSessionObj(props.item.caId).tenCa }}</span>
                    </template>
                    <template>
                      <div>
                        <div>Tên ca: {{ getSessionObj(props.item.caId).tenCa }}</div>
                        <div>Mã ca: {{getSessionObj(props.item.caId).maCa}}</div>
                        <div>Giờ bắt đầu: {{parseTime(getSessionObj(props.item.caId).gioBatDau)}}</div>
                        <div>Giờ kết thúc: {{parseTime(getSessionObj(props.item.caId).gioKetThuc)}}</div>
                      </div>
                    </template>
                  </v-tooltip>
                </div>
                <div class="text" v-if="editingRow == props.index">
                  <v-autocomplete
                    ref="caId"
                    label="Ca"
                    :items="sessions"
                    item-value="tcCaId"
                    item-text="maCa"
                    v-model="timekeepingData[props.index].caId"
                  ></v-autocomplete>
                </div>
              </td>
              <td class="text-xs-center">
                <div class="text" v-if="editingRow != props.index">{{ timekeepingData[props.index].gioThucTe }}</div>
                <div class="text" v-if="editingRow == props.index">
                  <v-text-field
                    ref="gioThucTe"
                    label="Giờ thực tế"
                    v-model="timekeepingData[props.index].gioThucTe"
                  ></v-text-field>
                </div>
              </td>
              <td class="text-xs-center">
                <div class="text" v-if="editingRow != props.index">{{ props.item.khachPhatSinh }}</div>
                <div class="text" v-if="editingRow == props.index">
                  <v-text-field
                    ref="khachPhatSinh"
                    label="Khách phát sinh"
                    v-model="timekeepingData[props.index].khachPhatSinh"
                  ></v-text-field>
                </div>
              </td>
              <td class="text-xs-left">
                <div class="text" v-if="editingRow != props.index">{{ props.item.ghiChu }}</div>
                <div class="text" v-if="editingRow == props.index">
                  <v-text-field
                    label="Ghi chú"
                    v-model="timekeepingData[props.index].ghiChu"
                  ></v-text-field>
                </div>
              </td>
            </tr>
          </template>
        </v-data-table>
        <v-divider class="ma-0"></v-divider>
        <v-layout>
          <v-btn small class="green white--text ma-0 mt-3" @click="addNewRow()" v-if="editingRow < 0">Chấm bổ sung</v-btn>
          <v-spacer></v-spacer>
          <v-btn small class="ma-0 mt-3 mr-3 green white--text" @click="save()" v-if="hasChanged">Lưu lại</v-btn>
          <v-btn small class="ma-0 mt-3 red white--text" @click="cancel()" v-if="hasChanged">Huỷ</v-btn>
        </v-layout>
      </v-card-text>
    </v-card>

    <!-- Snackbar -->
    <v-snackbar
      v-model="snackbar.active"
      :color="snackbar.color"
      :multi-line="false"
      :timeout="5000"
      top
      right
    >
      <span class="white--text">{{ snackbar.text }}</span>
      <v-btn
        dark
        flat
        @click="snackbar.active = false"
      >
        Đóng
      </v-btn>
    </v-snackbar>
  </div>
</template>

<script>
  import moment from 'moment';
  export default {
    data() {
      return {
        hasChanged : false,

        // Variables
        drivers   : [],
        trucks    : [],
        sessions  : [],
        allSessions : [],
        selectedDriver : null,

        selectedDate : {
          menu : false,
          value : new Date().toISOString().substr(0, 10)
        },

        timekeepingTableHeaders : [
          { text: 'STT', align: 'center', sortable: false, width: "60" },
          { text: 'Lái xe', align: 'left', sortable: false, width: "200" },
          { text: 'BKS', align: 'center', sortable: false, width: "200" },
          { text: 'Ca', align: 'center', sortable: false, width: "200" },
          { text: 'Giờ thực tế', align: 'center', sortable: false, width: "40" },
          { text: 'Khách phát sinh', align: 'center', sortable: false, width: "40" },
          { text: 'Ghi chú', align: 'left', sortable: false }
        ],
        timekeepingData     : [],
        timekeepingDataTmp  : [],
        tableLoading        : false,
        dsChiNhanh:null,
        chiNhanh: [],

        rowPerPageItems : [100000],
        pagination      : {
          page        : 1,
          rowsPerPage : 100000
        },

        // Interative editor
        editingRow : -1,

        // Message snackbar
        snackbar : {
          active  : false,
          text    : '',
          color   : 'red'
        },
          processing: false
      }
    },
    methods : {
      editRow(seletectedRowIndex) {
        if(this.editingRow != seletectedRowIndex && !this.validateEditingRow()) return;

        this.editingRow   = seletectedRowIndex;
        this.hasChanged   = true;
      },

      addNewRow() {
        this.timekeepingData.push({});
        this.editRow(this.timekeepingData.length - 1)
      },

      async save() {
        if(!this.validateEditingRow()) return;
        if (this.processing === true) {
            return;
        }
        this.processing = true;

        // Initiate data
        this.timekeepingData.forEach(timekeeping => {
          timekeeping.maCa = this.getSessionObj(timekeeping.caId).maCa;
          timekeeping.taiXeTen = this.getDriverName(timekeeping.taiXeId);
          timekeeping.bks = this.getTruckLicense(timekeeping.xeId);
        });
        // Call api to save data
        await this.$axios.post(`/chamcong/luudulieu`, {ngayChamCong : this.selectedDate.value, updateChamCongDTOS : this.timekeepingData});
        await this.loadTimekeepingData();
        this.processing = false;
        this.hasChanged   = false;
        this.editingRow   = -1;
      },

      async cancel(){
        this.editingRow = -1;
        this.hasChanged = false;
        await this.loadTimekeepingData();
      },

      async loadTimekeepingData(){
        this.snackbar.active = false;
        this.timekeepingData = [];
        this.tableLoading = true;
        try {
          let timekeepingRes = await this.$axios.post('/chamcong/taidulieu',{ ngayChamCong : moment(this.selectedDate.value).format('YYYY-MM-DD'),chiNhanh: this.chiNhanh});

          this.timekeepingData    = timekeepingRes.data;
          this.timekeepingDataTmp = timekeepingRes.data;
          this.selectedDriver     = null;
        } catch(err) {
          this.snackbar.active  = true;
          this.snackbar.text    = err.response.data.message;
        }
        this.tableLoading = false;
      },

      // Utils
      getDriverName(id){
        let driver = this.drivers.find(d => d.taiXeId == id)
        return driver ? driver.taiXeTen : id;
      },

      getTruckLicense(id){
        let truck = this.trucks.find(d => d.xeId == id)
        return truck ? truck.bks : id;
      },

      getSession(id) {
        let session = this.allSessions.find(d => d.tcCaId == id)
        return session ? session.maCa : id;
      },

      getSessionObj(id) {
        return this.allSessions.find(d => d.tcCaId == id);
      },

      changeLicensePlate($event, timekeepingIndex){
        console.log($event, timekeepingIndex)
      },

      parseTime(value) {
        let [hour, minute] = value.toString().split(".");
        minute = minute ? minute.length == 2 ? minute : minute + '0' : '00';
        return `${hour}:${minute}`;
      },
      onChangeChiNhanh : async function(value){
        let driversRes = await this.$axios.post(`/lichtruc/danhsachtaixe/chinhanh`,{chiNhanh: this.chiNhanh});
        this.drivers = driversRes.data;
        await this.loadTimekeepingData();
      },

      validateEditingRow() {
        if(this.editingRow < 0) return true;

        let valid = true;
        let row = this.timekeepingData.find((timekeeping, index) => index == this.editingRow);
        if(!row.taiXeId) {
          valid = false;
          this.$refs.taiXeId.error = true;
        }

        if(!row.xeId) {
          valid = false;
          this.$refs.xeId.error = true;
        }

        if(!row.caId) {
          valid = false;
          this.$refs.caId.error = true;
        }

        if(row.gioThucTe && row.gioThucTe < 0) {
          valid = false;
          this.$refs.gioThucTe.error = true;
        }

        if(row.khachPhatSinh && row.khachPhatSinh < 0) {
          valid = false;
          this.$refs.khachPhatSinh.error = true;
        }

        return valid;
      },

      // Filter
      filterDriver() {
        if(this.selectedDriver){
          this.timekeepingData = this.timekeepingDataTmp.filter(cd => cd.taiXeId == this.selectedDriver);
        } else {
          this.timekeepingData = this.timekeepingDataTmp;
        }
      },
    },

    async mounted() {
      let [driverRes, truckRes, sessionRes, allSessionRes,dsChiNhanhRes] = await Promise.all([
        this.$axios.get(`/lichtruc/danhsachtaixe`),
        this.$axios.get(`/lichtruc/danhsachxe`),
        this.$axios.get(`/catruc/activeca`),
        this.$axios.post(`/catruc/danhsach`, {page : 1, size: 100000}),
        this.$axios.get(`/chinhanh/danhsach`)
        // this.$axios.get(`/chamcong/taidulieu/${moment(this.selectedDate.value).format('YYYY-MM-DD')}`)
      ]);

      this.drivers      = driverRes.data;
      this.trucks       = truckRes.data;
      this.sessions     = sessionRes.data;
      this.allSessions  = allSessionRes.data.content;
      this.dsChiNhanh  = dsChiNhanhRes.data;

      await this.loadTimekeepingData();
    }
  }
</script>

<style>

</style>
