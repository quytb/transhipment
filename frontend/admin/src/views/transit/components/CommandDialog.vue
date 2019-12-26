<template>
  <v-dialog v-model="active" persistent fullscreen hide-overlay transition="dialog-bottom-transition">
    <v-card>
      <v-toolbar dark color="primary">
        <v-toolbar-title class="white--text">Thông tin điều lệnh</v-toolbar-title>
        <v-spacer></v-spacer>
        <v-btn class="red white--text" small @click="$emit('close')">Đóng</v-btn>
      </v-toolbar>

      <v-card-text>
        <div style="display: inline-block;">
          <v-layout row wrap>
            <v-flex xs3 class="py-0 px-3">
              <v-select
                :items="[{id : '0', name : 'Tất cả'}].concat(dsChiNhanhData)"
                item-value="id"
                item-text="name"
                label="Điểm làm việc"
                v-model="chiNhanh"
                @change="onChangeChiNhanh($event)"
              ></v-select>
            </v-flex>

            <v-flex xs3 class="py-0 px-3">
              <v-select
                :items="[{tcVttId : '0', tcVttName : 'Tất cả'}].concat(dsVungHoatDong)"
                item-value="tcVttId"
                item-text="tcVttName"
                label="Vùng hoạt động"
                v-model="vungId"
                @change="onChangeVungHoatDong($event)"
              ></v-select>
            </v-flex>

            <v-flex xs3 class="py-0 px-3">
              <v-autocomplete
                :items="dsTaiXeData"
                v-model="taiXeId"
                item-value="taiXeId"
                item-text="taiXeTen"
                label="Chọn lái xe"
                @change="onChangeLaiXe()"
              ></v-autocomplete>
              <span>Số khách/số ghế: 0/{{seats}}</span>
            </v-flex>
          </v-layout>
        </div>

        <!-- Danh sách khách cần đón -->
        <v-card class="mt-3 pa-0 elevation-2">
          <v-card-title class="pa-3">{{type == 'tcd' ? 'Danh sách khách cần đón' : 'Danh sách khách cần trả'}}</v-card-title>
          <v-divider class="ma-0"></v-divider>
          <v-card-text class="pa-3">
            <v-data-table
              :headers="tableHeaders"
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
                <td>{{ props.item.rank }}</td>
                <td>{{ props.item.soKhach }}</td>
                <td>{{ type == 'tcd' ? props.item.diemDonKhach : props.item.diemTraKhach}}</td>
                <td>{{ props.item.bvvGhiChu }}</td>
                <td>
                  <div v-if="editingHub != props.index" @click="edithub(props.index,props.item.didId)">{{ type == 'tcd' ? props.item.hubDiemDon : props.item.hubDiemTra}}</div>
                  <div v-if="editingHub == props.index">
                    <v-autocomplete
                      :items="dsBenXeEdit"
                      v-model="type == 'tcd' ? props.item.hubDiemDonId :  props.item.hubDiemTraId"
                      item-value="id"
                      item-text="ten"
                      label="Hub trả khách"
                      tabindex="5"
                      @change="onChangeHubDon(type == 'tcd' ? props.item.hubDiemDonId :  props.item.hubDiemTraId,props.item.bvvIds)"
                    ></v-autocomplete>
                  </div>
                </td>
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

        <!-- Actions -->
        <v-layout class="py-3" style="float: right">
          <!-- <v-btn
            small
            class="ml-0"
            @click="saveCommand()"
            v-if="this.taiXeId"
          >Lưu</v-btn> -->

          <v-btn
            small
            class="green white--text ma-0"
            @click="setCommand()"
            v-if="this.taiXeId"
          >Điều lệnh</v-btn>
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
    <v-dialog
      v-model="confirmDialog"
      max-width="500"
    >
      <v-card>
        <v-card-title class="pa-3">
          <v-layout row class="pa-0">
            <v-flex xs12>{{ 'Bạn có chắc muốn thay đổi hub đón?' }}</v-flex>
            <v-spacer></v-spacer>
          </v-layout>
        </v-card-title>
        <v-divider class="ma-0"></v-divider>

        <v-card-actions>
          <v-spacer></v-spacer>

          <v-btn
            color="green white--text"
            small
            @click="changeHubDon()"
          >
            Đồng ý
          </v-btn>

          <v-btn
            color=""
            small
            @click="confirmDialog = false"
          >
            Đóng
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-dialog>
</template>

<script>
  export default {
    props : {
      active      : Boolean,
      transitData : null,
      dsLaiXe     : null,
      dsChiNhanh : null,
      dsVungHoatDong : null,
      ngayTimKiem: null,
      type        : null,
      dsHub:null
    },
    data : () => ({
      // Message snackbar
      snackbar : {
        color: "red",
        active : false,
        text : ''
      },
      editingHub : -1,
      confirmDialog: false,
      tableHeaders: [
        { text: 'STT', align: 'center', sortable: false },
        { text: 'Tên khách', align: 'left', sortable: false },
        { text: 'SĐT', align: 'left', sortable: false },
        { text: 'Số khách', align: 'left', sortable: false },
        { text: 'Điểm đón/trả', align: 'left', sortable: false },
        { text: 'Ghi chú vé', align: 'left', sortable: false },
        { text: 'Hub đón/trả', align: 'left', sortable: false },
        { text: 'Ghi chú', align: 'left', sortable: false },
        { text: 'Thứ tự đón/trả', align: 'center', sortable: false },
        { text: 'Xoá', align: 'center', sortable: false }
      ],
      dialogTransitData : [],
      rowPerPageItems : [10000],
      pagination      : {
        page        : 1,
        rowsPerPage : 10
      },
      dsTaiXeData: new Array(),
      dsChiNhanhData:  new Array(),
      // FORMS
      taiXeId : null,
      seats: 0,
      chiNhanh:0,
      vungId:0,
      processing: false,
      hubIdToChange: 0,
      bvvIdsToChangeHubId:[],
      dsBenXeEdit:[]
    }),

    methods : {
      async saveCommand() {
        try {
          let saveRes = await this.$axios.post(`/dieudo/${this.type}/luu`, {
            danhSachVe : this.dialogTransitData.map(ts => {
              return {bvvIds : ts.bvvIds}
            }),
            taiXeId   : this.taiXeId,
            lenhId    : this.dsLenhLaiXe.find(lenh => lenh.laiXeId == this.taiXeId).lenhId
          });

          // Response error handle
          if(saveRes.data.status == "error") throw new Error(saveRes.data.message)
        } catch(err) {
          this.snackbar.active = true;
          this.snackbar.text = err.message;
          console.log(err);
        }
      },

      async setCommand(){
          if (this.processing === true) {
              return;
          }
          this.processing = true;
        try {
          let saveRes = await this.$axios.post(`/dieudo/${this.type}/dieulenh`, {
            danhSachVe : this.dialogTransitData.map(ts => {
              return {bvvIds : ts.bvvIds, ghiChu : ts.ghiChu}
            }),
            taiXeId   : this.taiXeId,
          });

          this.$emit('close', 'Điều lệnh thành công');
          this.processing = false;
        } catch(err) {
          this.snackbar.active = true;
          this.snackbar.text = err.response.data.message;
          this.processing = false;
        }
      },

      removeRow(rowIndex) {
        this.dialogTransitData = this.dialogTransitData.filter((row, index) => index != rowIndex);
        if(this.dialogTransitData.length == 0) this.$emit('close');
      },

      onChangeChiNhanh : async function(value){
        let driversRes = await this.$axios.post('/lichtruc/danhsachtaixe/chinhanh',{chiNhanh: [value]});
        this.dsTaiXeData = driversRes.data;
      },
      onChangeVungHoatDong: async function(value){
          let driversRes = await this.$axios.get(`/lichtruc/danhsachtaixe/vunghoatdong/`+value);
          this.dsTaiXeData = driversRes.data;
      },
      onChangeLaiXe : async function(){
        let idLaiXe = this.taiXeId;
        let ngayTimKiem = this.ngayTimKiem;

        try {
          let trangThaiTaiXe = await this.$axios.get(`/dieudo/gettrangthai/` + idLaiXe+"/"+ngayTimKiem);
          let xetrungchuyenRes = await this.$axios.get(`/dieudo/xetrungchuyen/` + idLaiXe+"/"+ngayTimKiem);
          let xetrungchuyen = xetrungchuyenRes.data;
          this.seats = xetrungchuyen.seats;
        } catch (err) {
          this.snackbar.active = true;
          this.snackbar.text = err.response.data.message;
          console.log(err);
        }
      },

      onChangeHubDon(e,ids){
          this.confirmDialog = true;
          this.hubIdToChange = e;
          this.bvvIdsToChangeHubId = ids;
      },
      async edithub(seletectedRowIndex,tripId) {
          this.editingHub = seletectedRowIndex;
          try {
              console.log(tripId)
              let update = await this.$axios.get('/benxe/danhsachtheotrip/'+tripId);
              let dsBenXeEdit = [];
              update.data.forEach(function (e) {
                  if(e.is_business==1 || e.is_point ==1){
                      dsBenXeEdit.push({id: e.id ,ten:e.station});
                  }
              });
              this.dsBenXeEdit = dsBenXeEdit;
          }catch (e) {
              this.snackbar.active = true;
              this.snackbar.text = e.response.data.message;
          }
      },
     async changeHubDon(){
        try {
            let arr = [];
            this.dialogTransitData.forEach(function(a) {
                arr = arr.concat(a.bvvIds);
            });
            let update = await this.$axios.post('/dieudo/updatehub/'+this.type, {
                veIds : arr,
                hubId: this.hubIdToChange,
            });
            this.showMessage('green','Cập nhật thành công');
        }catch (e) {
            this.snackbar.active = true;
            this.snackbar.text = e.response.data.message;
        }
        this.confirmDialog = false;
      },
        showMessage(color, message) {
            this.snackbar.active  = true;
            this.snackbar.color   = color;
            this.snackbar.text    = message;
        },
        tableHeaderConfig () {
          this.tableHeaders = [
            { text: 'STT', align: 'center', sortable: false },
            { text: 'Tên khách', align: 'left', sortable: false },
            { text: 'SĐT', align: 'left', sortable: false },
            { text: 'Hạng', align: 'left', sortable: false },
            { text: 'Số khách', align: 'left', sortable: false },
            { text: this.type == 'tcd' ? 'Điểm đón' : 'Điểm trả', align: 'left', sortable: false },
            { text: 'Ghi chú vé', align: 'left', sortable: false },
            { text: this.type == 'tcd' ? 'Hub đón' : 'Hub trả', align: 'left', sortable: false },
            { text: 'Ghi chú', align: 'left', sortable: false },
            { text: this.type == 'tcd' ? 'Thứ tự đón' : 'Thứ tự trả', align: 'center', sortable: false },
            { text: 'Xoá', align: 'center', sortable: false }
          ]
        }

    },

    mounted() {
      this.dsTaiXeData = this.$props.dsLaiXe;
      this.dsChiNhanhData = this.$props.dsChiNhanh;
      this.dsHub = this.$props.dsHub;
      this.dsBenXeEdit = this.$props.dsHub;
      this.dialogTransitData = [...this.transitData];
      this.tableHeaderConfig();
    },

    watch : {
      active : function(isActive) {
        console.log(isActive)
        if(isActive){
          this.dialogTransitData = [...this.transitData];
        }
      }
    }
  }
  console.log(a);
</script>

<style>

</style>
