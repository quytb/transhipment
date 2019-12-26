<template>
  <v-layout column>

    <!-- Tìm kiếm -->
    <v-card class="pa-0 elevation-1">
      <v-card-title class="pa-3 row">
        <div class="col-md-6">Tìm kiếm</div>
        <div class="col-md-6">
          <div style="float: right">Điều hành nhiều lệnh &nbsp;
            <p-switch v-model="switches.defaultOn" :change="changeStatusDieuHanh" type="success" on-text="Mở" off-text="Đóng"></p-switch>
          </div>
        </div>
      </v-card-title>
      <v-divider class="ma-0"></v-divider>

      <v-card-text class="pa-3">
        <v-container
          grid-list-xl
          class="pa-0"
          fluid
        >
          <v-layout row wrap>
            <UILoading :active="isLoading" ></UILoading>

            <v-flex xs6>
              <v-menu
                ref="selectedDate"
                v-model="searchForm.ngayTao.menu"
                lazy
                transition="scale-transition"
                offset-y
                full-width
                min-width="290px"
                tabindex="6"
              >
                <template v-slot:activator="{ on }">
                  <v-text-field
                    v-model="searchForm.ngayTao.value"
                    append-icon="event"
                    readonly
                    label="Ngày tạo"
                    v-on="on"
                  ></v-text-field>
                </template>
                <v-date-picker v-model="searchForm.ngayTao.value" no-title scrollable @change="reloadDsMaLenh()">
                </v-date-picker>
              </v-menu>

              <v-autocomplete
                :items="[{taiXeId : '', taiXeTen : 'Tất cả'}].concat(dsTaiXe)"
                item-value="taiXeId"
                item-text="taiXeTen"
                label="Tài xế"
                @change="reloadDsMaLenh()"
                v-model="searchForm.taiXeId"
              ></v-autocomplete>


              <v-autocomplete
                :items="dsLenh"
                item-value="lenhId"
                item-text="maLenh"
                label="Mã lệnh"
                tabindex="5"
                v-model="searchForm.lenhId"
              ></v-autocomplete>
            </v-flex>

            <v-flex xs6>
              <v-select
                :items="dsTrangThai"
                item-value="value"
                item-text="text"
                label="Trạng thái"
                @change="reloadDsMaLenh()"
                v-model="searchForm.trangThai"
              ></v-select>

              <v-select
                :items="dsKieuLenh"
                item-value="value"
                item-text="text"
                label="Kiểu lệnh"
                @change="reloadDsMaLenh()"
                v-model="searchForm.kieuLenh"
              ></v-select>

              <v-btn small class="green white--text ml-0" @click="search()">Tìm kiếm</v-btn>
            </v-flex>


          </v-layout>
        </v-container>
      </v-card-text>
    </v-card>

    <!-- Danh sách lệnh -->
    <v-card class="mt-4 pa-0 elevation-1">
      <v-card-title class="pa-3">
        <v-layout row wrap>
          <v-flex>Danh sách lệnh</v-flex>
          <v-spacer></v-spacer>
        </v-layout>
      </v-card-title>
      <v-divider class="ma-0"></v-divider>

      <v-card-text>
        <v-data-table
          :headers="taskHeaders"
          :items="tasks"
          :rows-per-page-items="rowPerPageItems"
          :pagination.sync="pagination"
          :total-items="pagination.totalItems"
          :loading="taskTableLoading"
          no-data-text="Không có dữ liệu"
          rows-per-page-text="Số bản ghi"
          class="elevation-1"
        >
          <template v-slot:items="props">
            <td class="text-xs-center">{{ props.index + 1 }}</td>
            <td style="min-width: 150px;">{{ props.item.taiXeTen }}</td>
            <td style="min-width: 150px;">{{ props.item.bks ? props.item.bks : "----------------------"}}</td>
            <td v-if="props.item.trangThai == 3">{{ props.item.khachHuy }}</td>
            <td v-else>{{ props.item.tongKhach > 0 ? props.item.tongKhach : "-" }}</td>
            <td class="text-xs-center" style="min-width: 150px;">
              <div
                class="px-3 py-1"
                :class="{
                  'dadieu' : props.item.trangThai == 1,
                  'dangdidon' : props.item.trangThai == 2,
                  'dahuy' : props.item.trangThai == 3,
                  'dadon' : props.item.trangThai == 4
                }"
              >{{ props.item.trangThaiText }}</div>
            </td>
            <td>{{ props.item.kieuLenhText }}</td>
            <td style="min-width: 200px;">{{ props.item.createdDate }}</td>
            <td style="min-width: 150px;">{{ props.item.nguoiTao }}</td>
            <td style="">{{ props.item.nguoiHuy }}</td>
            <td style="min-width: 180px;">{{ props.item.lyDoHuy }}</td>
            <td>
              <v-btn
                small
                @click="editLenh(props.item.tcLenhId,props.item.taiXeTen)"
                icon
              ><v-icon>edit</v-icon></v-btn>
            </td>
            <td>
              <v-btn
                small
                icon
                v-if="!(props.item.trangThai == 3 || props.item.trangThai == 4)"
                @click="confirmDelete(props.item.tcLenhId)"
              ><v-icon>delete</v-icon></v-btn>
            </td>
          </template>
        </v-data-table>
      </v-card-text>
    </v-card>

    <CommandDetail
      :active="commandDetailDialog"
      :lenhId="lenhId"
      :dsTaiXe="dsTaiXe"
      :taiXeTen="taiXeTen"
      @close="onCommandDetailClosed"
    />

    <!-- Confirm delete command dialog -->
    <v-dialog
      v-model="confirmDialog"
      max-width="500"
    >
      <v-card>
        <v-card-title class="pa-3">
          <v-layout row class="pa-0">
            <v-flex xs12 style="text-align: center">Bạn có chắc muốn hủy lệnh này?</v-flex>
            <v-spacer></v-spacer>
          </v-layout>
        </v-card-title>
        <v-divider class="ma-0"></v-divider>

        <v-card-actions>
          <v-layout row wrap>
            <v-flex xs12>
              <v-textarea
                tabindex="5"
                cols="100"
                rows="3"
                label="Lý do"
                v-model="lyDoHuy">

              </v-textarea>
            </v-flex>
            <v-flex xs12 style="text-align: right">
              <v-btn
                color="red white--text"
                small
                @click="deleteCommand()"
              >
                Hủy
              </v-btn>
              <v-btn
                color=""
                small
                @click="confirmDialog = false"
              >
                Đóng
              </v-btn>
            </v-flex>
          </v-layout>
        </v-card-actions>
      </v-card>
    </v-dialog>

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
  </v-layout>
</template>

<script>
import CommandDetail from './components/CommandDetail.vue';
import UILoading from '@/components/Loading.vue';
import PSwitch from '../../components/Switch';

export default {
  components : {
    CommandDetail,
    UILoading,
    PSwitch
  },
  computed : {
    pagination : {
      get(){ return this.pagination_data; },
      set(value) { this.pagination_data = value; }
    }
  },
  data() {
    return {
      isLoading           : false,
      commandDetailDialog : false,
      confirmDialog       : false,

      switches: {
          defaultOn: true
      },
      // Data
      dsTrangThai : [
        { "value": 0, "text": "Tất cả" },
        { "value": 1, "text": "Đã điều" },
        { "value": 2, "text": "Đang chạy" },
        { "value": 3, "text": "Đã bị hủy" },
        { "value": 4, "text": "Đã hoàn tất" },
        { "value": 5, "text": "Đã tạo" }
      ],
      dsTaiXe     : [],
      dsTuyen     : [],
      dsLenh      : [],
      dsKieuLenh  : [
        {value : 0, text: "Tất cả"},
        {value : 1, text: "Lệnh đón"},
        {value : 2, text: "Lệnh trả"}
      ],

      // Tasks
      taskHeaders: [
        { text: 'STT', align: 'center', sortable: false },
        { text: 'Lái xe', align: 'left', sortable: false },
        { text: 'Biển kiểm soát', align: 'left', sortable: false },
        { text: 'Số khách', align: 'left', sortable: false },
        { text: 'Trạng thái', align: 'center', sortable: false },
        { text: 'Kiểu lệnh', align: 'left', sortable: false },
        { text: 'Ngày tạo', align: 'left', sortable: false },
        { text: 'Người tạo', align: 'left', sortable: false, width: "100" },
        { text: 'Người hủy', align: 'left', sortable: false, width: "100" },
        { text: 'Lý do hủy', align: 'left', sortable: false, width: "100" },
        { text: 'Chi tiết', align: 'center', sortable: false, width: "100" },
        { text: 'Hủy', align: 'center', sortable: false, width: "100" }
      ],
      tasks : [],
      taskTableLoading : false,
      lenhId : null, // Lenh được chọn để edit
      taiXeTen: "", //ten tai xe cua lenh duoc chon

      // Pagination
      rowPerPageItems     : [10, 20, 30, 50, 100, 200, 500],
      pagination_data     : {
        page        : 1,
        rowsPerPage : 10
      },

      // Search form
      searchForm : {
        ngayTao : {
          menu : false,
          value : new Date().toISOString().substr(0, 10)
        },
        kieuLenh  : 0,
        taiXeId   : null,
        trangThai : 0,
        tuyenId   : null,
        lenhId    : null
      },

      // Intervals
      interval : null,
      intervalCounter : 60,

      // Message snackbar
      snackbar : {
        color: 'green',
        active : false,
        text : ''
      },
      lyDoHuy:''
    }
  },
  methods : {
    async search() {
      this.taskTableLoading = true;
      let criterials = {
        kieuLenh  : this.searchForm.kieuLenh,
        ngayTao   : this.searchForm.ngayTao.value,
        taiXeId   : this.searchForm.taiXeId,
        trangThai : this.searchForm.trangThai,
        tuyenId   : this.searchForm.tuyenId,
        lenhId     : this.searchForm.lenhId,
      };
      let taskRes = await this.$axios.post(`/dieudo/lenh/timkiem`, {
        ...criterials,
        paging: {
          page : this.pagination.page,
          size : this.pagination.rowsPerPage
        }
      });

      this.tasks = taskRes.data.content;
      this.pagination.totalItems = taskRes.data.totalElement;

      this.taskTableLoading = false;

      // // Create Interval
      // this.intervalCounter = 60;
      // if(this.interval == null) {
      //   // start interval
      //   let self = this;
      //   this.interval = setInterval(function() {
      //     self.intervalCounter -= 1;
      //     if(self.intervalCounter == 0) {
      //       self.intervalCounter = 60;
      //       self.search();
      //     }
      //   }, 1000)
      // }
    },

    editLenh(lenhId,taiXeTen){
      this.taiXeTen = taiXeTen;
      this.lenhId = lenhId;
      this.commandDetailDialog = true;
    },

    confirmDelete(lenhId) {
      this.lenhId = lenhId;
      this.confirmDialog = true;
    },

    async deleteCommand() {
      try{
        await this.$axios.post('/dieudo/huylenhdadieu',{lenhId:this.lenhId,lyDoHuy:this.lyDoHuy});
        this.confirmDialog = false;
        this.lenhId = null;
      } catch(err) {
        console.log(err);
      }
      this.search();
    },

    async reloadDsMaLenh(){
      this.isLoading = true;
      let dsLenhRes = await this.$axios.post(`/dieudo/lenh/malenh`, {
        kieuLenh  : this.searchForm.kieuLenh,
        ngayTao   : this.searchForm.ngayTao.value,
        taiXeId   : this.searchForm.taiXeId,
        trangThai : this.searchForm.trangThai,
        tuyenId   : this.searchForm.tuyenId,
        paging: {
          page: 1,
          size: 99999
        }
      });

      this.dsLenh = dsLenhRes.data;

      this.isLoading = false;
    },

    onCommandDetailClosed(message){
      if(message) this.showMessage('green', message);
      this.commandDetailDialog = false
    },

    showMessage(color, message) {
      this.snackbar.active  = true;
      this.snackbar.color   = color;
      this.snackbar.text    = message;
    },
    async changeStatusDieuHanh(){
      let status = this.switches.defaultOn ? 1 : 0;
      await this.$axios.get('/lenhconfig/set/'+status);
    }
  },
  async mounted() {
    let [dsTaiXeRes,statusDieuLenhRes, dsLenhRes] = await Promise.all([
      this.$axios.get(`/lichtruc/danhsachtaixe`),
      this.$axios.get('/lenhconfig'),
      this.$axios.post(`/dieudo/lenh/malenh`, {
        "ngayTao": this.searchForm.ngayTao.value,
        "paging": {
          "page": 1,
          "size": 99999
        }
      })
    ]);
    this.switches.defaultOn = statusDieuLenhRes.data == 1 ? true: false;
    this.dsTaiXe  = dsTaiXeRes.data;
    this.dsLenh   = dsLenhRes.data;
  },
  watch : {
    pagination : {
      deep : true,
      handler : async function(value) {
        await this.search()
      }
    }
  }
}
</script>


<style>
/*  #FFFFFF	    #FF9900	  #8BC34A	      #B7B7B7	      #FFD966
    Chưa điều	  Đã điều	  Đang đi đón	  Đã đón	      Đã hủy */
.dadieu{background-color: #FF9900;}
.dangdidon{background-color: #8BC34A;}
.dadon{background-color: #B7B7B7;}
.dahuy, .xetuyenhuy{background-color: #FFD966;}
.xetuyendon{background-color: #0074d9;}
.xengoaidon{background-color: #28a745;}
</style>
