<template>
  <v-dialog v-model="active" persistent fullscreen hide-overlay transition="dialog-bottom-transition">
    <v-card v-if="lenh">
      <v-toolbar dark color="primary">
        <v-toolbar-title class="white--text">Chi tiết lệnh</v-toolbar-title>
        <v-spacer></v-spacer>
        <v-btn class="red white--text" small @click="$emit('close')">Đóng</v-btn>
      </v-toolbar>

      <v-card-text>
        <v-layout row wrap style="font-size: 18px; font-weight: bold; line-height: 68px;">
          <v-flex xs4>
            <div>
              <div style="display: inline-block; margin-right: 20px;">Chọn lái xe:</div>
              <span v-if="lenh.trangThai != 5">{{this.taiXeTen ? this.taiXeTen : 'N/A'}}</span>
              <v-autocomplete
                ref="driverSelect"
                v-if="lenh.trangThai == 5"
                :items="dsTaiXe"
                item-value="taiXeId"
                item-text="taiXeTen"
                v-model="selectedDriver"
                style="display: inline-block; width: 250px;"
              ></v-autocomplete>
            </div>
          </v-flex>
          <v-flex v-if="lenh.trangThai !== 5" xs4>
            Mã lệnh: {{lenh.maLenh}}
          </v-flex>
          <v-flex v-else xs4>
            <div>
              <div style="display: inline-block; margin-right: 20px;">Chọn xe:</div>
              <v-autocomplete
                :items="listXeTCDen"
                v-model="selectedVihicle"
                item-value="xeId"
                :item-text="itemText"
                label="Chọn xe"
                tabindex="5"
                style="display: inline-block; width: 250px;"
              ></v-autocomplete>
            </div>
          </v-flex>
          <v-flex xs4>
            Ngày tạo: {{lenh.createdDate}}
          </v-flex>
          <v-flex xs4>
            Trạng thái: <span
                class="px-3 py-1"
                :class="{
                  'dadieu' : lenh.trangThai == 1,
                  'dangdidon' : lenh.trangThai == 2,
                  'dahuy' : lenh.trangThai == 3,
                  'dadon' : lenh.trangThai == 4
                }"
              >{{ lenh.trangThaiText }}</span>
          </v-flex>
          <v-flex xs4>
            Kiểu lệnh: {{lenh.kieuLenhText}}
          </v-flex>
          <v-flex xs4>
            Người tạo: {{lenh.nguoiTao}}
          </v-flex>
        </v-layout>

        <!-- Danh sách khách cần đón -->
        <v-card class="mt-3 pa-0 elevation-2">
          <v-card-title class="pa-3">Danh sách khách cần đón</v-card-title>
          <p class="chu_thich">* Thông tin của khách có thể đã bị thay đổi do vé mới đã được đặt, vui lòng kiểm tra lịch sử bên ERP nếu cần thiết.</p>
          <v-divider class="ma-0"></v-divider>
          <v-card-text class="pa-3">
            <v-data-table
              :headers="dsKhachHeaders"
              :items="dsKhach"
              :rows-per-page-items="rowPerPageItems"
              :pagination="pagination_data"
              no-data-text="Không có dữ liệu"
              rows-per-page-text="Số bản ghi"
              class="elevation-1"
            >
              <template v-slot:items="props">
                <td class="text-xs-center">{{ props.index + 1 }}</td>
                <td>{{ props.item.ten }}</td>
                <td>{{ props.item.sdt }}</td>
                <td class="text-xs-center" v-if="lenh.trangThai != 3">{{ props.item.soKhach }}</td>
                <td v-else>1</td>
                <td v-if="props.item.kieuLenh == 1">{{ props.item.diaChi }}</td>
                <td v-else>{{ props.item.diaChi }}</td>
                <td class="text-xs-center" v-if="lenh.trangThai != 3">{{ props.item.thuTuDon }}</td>
                <td v-else>{{ props.item.lyDoHuy }}</td>
                <td class="text-xs-center">
                  <v-btn small icon @click="deleteTicket(props.index)" v-if="lenh.trangThai == 5">
                    <v-icon>delete</v-icon>
                  </v-btn>
                </td>
              </template>
            </v-data-table>
          </v-card-text>
        </v-card>

        <!-- Actions -->
        <v-layout class="py-3">
          <!-- <v-btn small class="ml-0">Lưu</v-btn> -->
          <v-btn
            small
            class="green white--text ma-0"
            v-if="lenh.trangThai==5"
            @click="createCommand()"
          >Điều lệnh</v-btn>
        </v-layout>
      </v-card-text>


      <!-- Snackbar -->
      <v-snackbar
        v-model="snackbar.active"
        :color="snackbar.color"
        :multi-line="false"
        :timeout="5000"
        top
        right
        style="z-index: 1000;"
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

    </v-card>
  </v-dialog>
</template>

<script>
export default {
  props : {
    active : Boolean,
    lenhId : null,
    taiXeTen: "",
    dsTaiXe : null
  },
  data : () => ({
    // Variables
    lenh : null,
    selectedDriver : null,
    selectedVihicle : null,
    listXeTCDen:[],
    dsDiemTC: [],

    // Table
    dsKhachHeaders: [
      { text: 'STT', align: 'center', sortable: false },
      { text: 'Tên khách', align: 'left', sortable: false },
      { text: 'SĐT', align: 'left', sortable: false },
      { text: 'Số khách', align: 'left', sortable: false },
      { text: 'Điểm đón', align: 'left', sortable: false },
      { text: 'Thứ tự đón', align: 'center', sortable: false },
      { text: 'Xoá', align: 'center', sortable: false },
    ],
    dsKhach : [],

    // Pagination
    rowPerPageItems : [100000],
    pagination_data : {
      page        : 1,
      rowsPerPage : 100000
    },

    // Message snackbar
    snackbar : {
      active  : false,
      text    : '',
      color   : 'red'
    }
  }),
  methods : {
    async createCommand() {
      try {
        if(!this.selectedDriver) {
          this.$refs.driverSelect.error = true;
          this.showSnackbar('red', 'Vui lòng chọn tài xế');
          return;
        }

        if(!this.selectedVihicle) {
            this.$refs.driverSelect.error = true;
            this.showSnackbar('red', 'Vui lòng chọn xe');
            return;
        }

        let data = {
          danhSachVe : this.lenh.veTrungChuyenDTOList.map(dsVe => ({bvvIds : dsVe.listBvvId})),
          lenhId : this.lenh.tcLenhId,
          hubId : this.lenh.diemgiaokhach,
          xeId: this.selectedVihicle,
          taixeId : this.selectedDriver
        };
        let createCommandRes = await this.$axios.post('/trung-chuyen-hub/xac-nhan-lenh', data);

          this.showSnackbar('green', 'Điều lệnh thành công');
      } catch(err) {
        console.log(err);
        this.showSnackbar('red', err.response.data.message);
      }
    },

    deleteTicket(ticketIndex){
      this.dsKhach = this.dsKhach.filter((ticket, index) => index != ticketIndex);
    },

    showSnackbar(color, text){
      this.snackbar.active  = true;
      this.snackbar.text    = text;
      this.snackbar.color   = color;
    },
    createHeader(){
        this.dsKhachHeaders = [
            { text: 'STT', align: 'center', sortable: false },
            { text: 'Tên khách', align: 'left', sortable: false },
            { text: 'SĐT', align: 'left', sortable: false },
            { text: 'Số khách', align: 'left', sortable: false },
            { text: 'Điểm đón', align: 'left', sortable: false },
            { text: this.lenh.trangThai != 3 ? 'Thứ tự đón' : 'Lý do hủy', align: 'center', sortable: false },
            { text: 'Xoá', align: 'center', sortable: false },
        ]
    },
    itemText(item) { return item.bks + ' - '+ item.seats +' chỗ'},
  },
  watch : {
    active : async function(isActive) {
      if(isActive) {
        this.lenh = null;
        this.selectedDriver = null;
        let [ lenhRes, dsKhachRes, listXeTCDenRes] = await Promise.all([
          this.$axios.get(`/dieudo/lenh/chitiet/${this.lenhId}`),
          this.$axios.get(`/dieudo/tcd/dskhachdangdon/${this.lenhId}`),
          this.$axios.get(`/dieu-hanh-hub/trung-chuyen-don/danh-sach-xe`)
        ]);

        this.dsKhach  = dsKhachRes.data.veTrungChuyenDTOList;
        this.lenh     = lenhRes.data;
        this.listXeTCDen = listXeTCDenRes.data;
      }
      this.createHeader();
    }
  },
  mounted() {
    this.createHeader();
  },
}
</script>

<style>
  .chu_thich{
    margin: 0 20px;
    padding: 0;
    color: red;
    font-size: 12.5px;
    font-style: italic;
  }
</style>
