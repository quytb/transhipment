<template>
  <v-dialog v-model="active" persistent fullscreen hide-overlay transition="dialog-bottom-transition">
    <v-card>
      <v-toolbar dark color="primary">
        <v-toolbar-title class="white--text">Thông tin điều bổ sung</v-toolbar-title>
        <v-spacer></v-spacer>
        <v-btn class="red white--text" small @click="$emit('close')">Đóng</v-btn>
      </v-toolbar>

      <v-card-text>
        <div>
          <div style="display: inline-block; font-size: 18px; font-weight: bold; margin-right: 20px;">Chọn lái xe</div>

            <v-flex xs12 md3 class="py-0 px-3">
              <v-autocomplete
                :items="[{laiXeId : '0', laiXeTen : '-------------- Các lệnh đã tạo ------------'}].concat(dsLenhLaiXe)"
                v-model="taiXeId"
                item-value="laiXeId"
                item-text="laiXeTen"
                style="display: inline-block; width: 100%"
                @change="fetchCustomers($event)"
              ></v-autocomplete>
            </v-flex>
        </div>

<!--        <div>-->
<!--          <div style="display: inline-block; font-size: 18px; font-weight: bold; margin-right: 20px;" v-if="taiXeId">-->
<!--            Tài xế này đang có {{dsLenhDangCo.length}} lệnh:-->
<!--          </div>-->
<!--        </div>-->

        <!-- Danh sách khách bổ sung -->
        <v-card class="mt-3 pa-0 elevation-2">
          <v-card-title class="pa-3">Danh sách khách bổ sung</v-card-title>
          <v-divider class="ma-0"></v-divider>
          <v-card-text class="pa-3">
            <v-data-table
              :headers="[...customerDataTableHeaders, { text: 'Xoá', align: 'center', sortable: false }]"
              :items="dialogTransitData"
              :rows-per-page-items="rowPerPageItems"
              :pagination.sync="pagination"
              no-data-text="Không có dữ liệu"
              rows-per-page-text="Số bản ghi"
              class="elevation-1"
            >
              <template v-slot:items="props">
                <td class="text-xs-center">{{ props.index + 1 }}</td>
                <td>{{ props.item.tenKhach }}</td>
                <td>{{ props.item.sdtKhach }}</td>
                <td>{{ props.item.soKhach }}</td>
                <td>{{ props.item.diemDonKhach }}</td>
                <td>{{ props.item.bvvGhiChu }}</td>
                <td><v-text-field
                  label="Ghi chú trung chuyển"
                  v-model="props.item.ghiChu"
                  tabindex="1"
                  required
                ></v-text-field></td>
                <td class="text-xs-center">{{ props.item.thuTuDon }}</td>
                <td class="text-xs-center px-0" style="max-width: 50px; width: 50px; text-align: center;">
                  <v-btn icon small @click="removeRow(props.index)"><v-icon>delete</v-icon></v-btn>
                </td>
              </template>
            </v-data-table>
          </v-card-text>
        </v-card>

        <div class="card width100">
          <div class="card-header">
            <h4 class="card-title">Danh sách các lệnh của tài xế:</h4>
            <p></p>
          </div>
          <div class="card-content" style="width: 100%">
            <div class="width100">
            <v-radio-group v-model="ex7" class="width100" column>
              <el-collapse v-for="lenh in dsLenhDangCoCopy" :key="lenh.lenhId" class="width100">
                <v-card class="mt-3 pa-0 elevation-2">
                  <v-card-text class="pa-3">
                    <v-radio
                      :label="`Lệnh id: ${lenh.lenhId} - ${lenh.trangThai} - Số khách/ghế: ${lenh.soKhach}/${lenh.seats ? lenh.seats : 'Chưa phân xe'}`"
                      color="red"
                      :value="lenh.lenhId"
                    ></v-radio>
                    <el-collapse-item title="Danh sách khách" :name="lenh.lenhId">
                      <div>
                        <v-data-table
                          :headers="customerDataTableHeaders"
                          :items="lenh.veTrungChuyenDTOList"
                          :rows-per-page-items="rowPerPageItems"
                          :pagination.sync="pagination"
                          no-data-text="Không có dữ liệu"
                          rows-per-page-text="Số bản ghi"
                          class="elevation-1"
                        >
                          <template v-slot:items="props">
                            <td class="text-xs-center">{{ props.index + 1 }}</td>
                            <td>{{ props.item.ten }}</td>
                            <td>{{ props.item.sdt }}</td>
                            <td>{{ props.item.soKhach }}</td>
                            <td>{{ props.item.diaChi }}</td>
                            <td>{{ props.item.bvvGhiChu }}</td>
                            <td></td>
                            <td class="text-xs-center">{{ props.item.thuTuDon }}</td>
                          </template>
                        </v-data-table>
                        <p></p>
                      </div>
                    </el-collapse-item>
                  </v-card-text>
                </v-card>
              </el-collapse>
            </v-radio-group>
            </div>
          </div>
        </div>
        <!-- Actions -->
        <v-layout class="py-3" style="float: right">
          <v-btn
            small
            class="green white--text ma-0"
            @click="setCommand()"
          >Điều bổ sung</v-btn>
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

  </v-dialog>
</template>

<script>
export default {
  props : {
    active      : Boolean,
    transitData : null,
    ngayTimKiem: null,
    type        : null
  },

  data : () => ({
    // Message snackbar
    snackbar : {
      color: "red",
      active : false,
      text : ''
    },
    ex7: 0,
    // Models
    taiXeId : '',
    lenhId  : '',
    seats: 0,

    // Data
    customerDataTableHeaders: [
        { text: 'STT', align: 'center', sortable: true },
        { text: 'Tên khách', align: 'left', sortable: false },
        { text: 'SĐT', align: 'left', sortable: false },
        { text: 'Số khách', align: 'left', sortable: false },
        { text: 'Điểm đón', align: 'left', sortable: false },
        { text: 'Ghi chú vé', align: 'left', sortable: false },
        { text: 'Ghi chú', align: 'left', sortable: false },
        { text: 'Thứ tự đón', align: 'center', sortable: false },
    ],
    customerData : [],
    dialogTransitData : [],
    dsLenhLaiXe : [],
    dsLenhDangCo:[],
    dsLenhDangCoCopy:[],
    dsKhach:[],
    soKhach: 0,
    selected: true,
    // Pagination
    rowPerPageItems : [10000],
    pagination      : {
      page        : 1,
      rowsPerPage : 10
    }
  }),
  methods : {
    async fetchCustomers(e) {
      // if(e != 0) {
      //   this.dsLenhDangCoCopy = null;
      //   this.dsLenhDangCo = this.dsLenhLaiXe.filter(lenh => lenh.laiXeId == this.taiXeId);
      //
      //   for (let item of this.dsLenhDangCo) {
      //       let customerRes = await this.$axios.get(`/dieudo/tcd/dskhachdang${self.type == 'tcd' ? 'don' : 'tra'}/${item.lenhId}`);
      //       item.dsKhach = customerRes.data.veTrungChuyenDTOList;
      //       let xetrungchuyenRes = item.idXe ? await this.$axios.get(`/dieudo/xetrungchuyen/v2/` + item.idXe) : null;
      //       let xetrungchuyen = xetrungchuyenRes ? xetrungchuyenRes.data : {};
      //       item.seats = xetrungchuyen.seats;
      //       item.soKhach = customerRes.data.tongKhach;
      //   }
      //
      //   this.ex7 = this.dsLenhDangCo[0].lenhId;
      //   this.dsLenhDangCoCopy = this.dsLenhDangCo;
      // }else{
      //   let customerRes = await this.$axios.get('/dieudo/tcd/dslenhlaixedatao/');
      //   console.log(customerRes);
      //   this.dsLenhDangCoCopy = customerRes.data;
      //   this.ex7 = this.dsLenhDangCoCopy[0].lenhId;
      // }
        let customerRes = await this.$axios.get(`/dieudo/tcd/dslenhlaixedatao/${e}`);
        console.log(customerRes);
        this.dsLenhDangCoCopy = customerRes.data;
        this.ex7 = this.dsLenhDangCoCopy[0].lenhId;
    },

    // Main operation
    async setCommand() {
        if (this.processing === true) {
            return;
        }
        this.processing = true;
      try {
        let saveRes = await this.$axios.post(`/dieudo/${this.type}/dieubosung`, {
          danhSachVe : this.dialogTransitData.map(ts => {
            return {bvvIds : ts.bvvIds, ghiChu : ts.ghiChu}
          }),
          taiXeId   : this.taiXeId,
          lenhId    : this.ex7
        });

        // Response error handle
        if(saveRes.data.status == "error") throw new Error(saveRes.data.message)

        this.$emit('close', 'Điều lệnh bổ sung thành công');
        this.processing = false;
      } catch(err) {
        this.snackbar.active = true;
        this.snackbar.text = err.message;
        this.processing = false;
        console.log(err);
      }
    },

    removeRow(rowIndex) {
      this.dialogTransitData = this.dialogTransitData.filter((row, index) => index != rowIndex);
      if(this.dialogTransitData.length == 0) this.$emit('close');
    },
      tableHeaderConfig(){
        this.customerDataTableHeaders = [
          { text: 'STT', align: 'center', sortable: true },
          { text: 'Tên khách', align: 'left', sortable: false },
          { text: 'SĐT', align: 'left', sortable: false },
          { text: 'Số khách', align: 'left', sortable: false },
          { text: this.type == 'tcd' ? 'Điểm đón' : 'Điểm trả', align: 'left', sortable: false },
          { text: 'Ghi chú vé', align: 'left', sortable: false },
          { text: 'Ghi chú', align: 'left', sortable: false },
          { text: 'Thứ tự đón', align: 'center', sortable: false },
        ]
      }
  },
  mounted() {
    this.dialogTransitData = [...this.transitData];
    console.log(this.transitData);
  },
  computed: {
  },

  watch : {
    active : async function(value) {
      if(value){
        this.dialogTransitData = [...this.transitData];

        try {
          let dsLenhLaiXeRes  = await this.$axios.get(`/dieudo/${this.type}/dslenhlaixe`);
          this.dsLenhLaiXe    = dsLenhLaiXeRes.data;

          this.taiXeId        = null;
          this.customerData   = [];
        } catch(err){
          console.log(err)
        }
      }
    },
      taiXeId: function() {
        console.log(this.taiXeId);
        if(!this.taiXeId){
            this.dsLenhDangCoCopy = []
        };
      }
  }
}
</script>

<style>
  .width100{
    width: 100%;
  }
  .width100 .v-input__control{
    width: 100% !important;
  }
</style>
