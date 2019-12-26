<template>
  <v-layout column>

    <!-- Tìm kiếm -->
    <v-card class="pa-0 elevation-1">
      <v-card-title class="pa-3">Tìm kiếm</v-card-title>
      <v-divider class="ma-0"></v-divider>

      <v-card-text class="pa-3">
        <v-container
          grid-list-xl
          class="pa-0"
          fluid
        >
          <v-layout row wrap>
            <UILoading :active="isLoading" ></UILoading>

            <v-flex xs3>
              <v-menu
                ref="ngayXuatBen"
                v-model="searchForm.ngayXuatBen.menu"
                lazy
                transition="scale-transition"
                offset-y
                full-width
                min-width="290px"
              >
                <template v-slot:activator="{ on }">
                  <v-text-field
                    v-model="searchForm.ngayXuatBen.value"
                    label="Ngày"
                    append-icon="event"
                    readonly
                    v-on="on"
                  ></v-text-field>
                </template>
                <v-date-picker v-model="searchForm.ngayXuatBen.value" no-title scrollable @change="loadNodes()">
                </v-date-picker>
              </v-menu>

              <v-select
                v-model="searchForm.khoangThoiGian"
                :items="khoangThoiGian"
                item-text="text"
                item-value="value"
                label="Trước giờ về bến"
                tabindex="1"
              ></v-select>
               <v-autocomplete
                :items="trangThaiVe"
                v-model="searchForm.trangThaiVe"
                item-value="value"
                item-text="trangThai"
                label="Trạng thái vé"
                tabindex="5"
                multiple
                chips
              ></v-autocomplete>
            </v-flex>
            <v-flex xs3>
              <v-autocomplete
                v-model="searchForm.listTuyen"
                :items="dsTuyen"
                label="Chọn tuyến"
                item-value="tuyId"
                item-text="tenTuyen"
                tabindex="2"
                multiple
                @change="loadNodes()"
              ></v-autocomplete>
              <v-autocomplete
                :items="[{tcVttId : '0', tcVttName : 'Tất cả'}].concat(dsVungTC)"
                v-model="searchForm.vungTC"
                item-value="tcVttId"
                item-text="tcVttName"
                label="Vùng điều hành"
                tabindex="5"
              ></v-autocomplete>
              <v-autocomplete v-if="diemkinhdoanh"
                              :items="dsBenXe"
                              v-model="searchForm.diemKDs"
                              item-value="id"
                              item-text="ten"
                              label="Điểm kinh doanh"
                              tabindex="5"
                              multiple
                              chips
                              deletable-chips
                              clearable
              ></v-autocomplete>
              <v-autocomplete v-else
                              :items="[{id : '0', ten : 'Tất cả'}].concat(dsBenXe)"
                              v-model="searchForm.hubId"
                              item-value="id"
                              item-text="ten"
                              label="Hub nhận khách"
                              tabindex="5"
              ></v-autocomplete>
            </v-flex>
            <v-flex xs3>
              <v-radio-group v-model="searchForm.timeOption" class="mt-3" row @change="onTimeOptionChanged()">
                <v-radio
                  v-for="r in radioOpions"
                  :key="r.value"
                  :label="r.label"
                  :value="r.value"
                ></v-radio>
              </v-radio-group>

              <v-autocomplete
                :items="nodes"
                v-model="searchForm.notIdA"
                item-value="notId"
                item-text="MixData"
                label="Chọn giờ A"
                tabindex="4"
                :disabled="[0, 2].includes(searchForm.timeOption)"
                multiple
                chips
              ></v-autocomplete>

              <v-autocomplete
                :items="nodes"
                v-model="searchForm.notIdB"
                item-value="notId"
                item-text="MixData"
                label="Chọn giờ B"
                tabindex="5"
                :disabled="[0, 1].includes(searchForm.timeOption)"
                multiple
                chips
              ></v-autocomplete>
            </v-flex>
            <v-flex xs3>
              <v-btn medium class="green white--text ml-0" @click="loadData(true)">Tìm kiếm</v-btn>
            </v-flex>
          </v-layout>
        </v-container>
      </v-card-text>
    </v-card>

    <!-- Danh sách  trung chuyển trả -->
    <v-card class="mt-4 pa-0 elevation-1">
      <v-card-title class="pa-3">
        <v-layout row wrap>
          <v-flex>Danh sách trung chuyển trả</v-flex>
          <v-spacer></v-spacer>
          <v-flex class="text-xs-right">Trang sẽ tự load lại trong <span class="red--text">{{this.intervalCounter}}</span> giây</v-flex>
        </v-layout>
      </v-card-title>
      <v-divider class="ma-0"></v-divider>

      <v-card-text>
        <v-data-table
          :headers="taskHeaders"
          :items="transitData"
          :rows-per-page-items="rowPerPageItems"
          :pagination.sync="pagination"
          :total-items="pagination.totalItems"
          :loading="tableLoading"
          no-data-text="Không có dữ liệu"
          rows-per-page-text="Số bản ghi"
          class="elevation-1 no-p-margin table-tc"
        >
          <template v-slot:items="props">
            <td class="text-xs-center">
              <v-checkbox
                v-model="props.item.selected"
                v-if="[0, 4, 11].includes(props.item.trangThaiTra)"
                class="align-center justify-center"
                primary
                hide-details
              ></v-checkbox>
            </td>
            <td class="text-xs-center">{{ props.index + 1 }}</td>
            <td style="min-width: 200px;">{{ props.item.tenTuyen }}</td>
            <td>{{ props.item.gioDieuHanh }}</td>
            <td>{{ props.item.gioXuatBen }}</td>
            <td>{{ props.item.tenKhach }}</td>
            <td>{{ props.item.sdtKhach }}</td>
            <td>{{ props.item.soKhach }}</td>
            <td>
              <v-img
                :src=props.item.icon aspect-ratio="1"
                :alt="props.item.rank"
                :title="props.item.rank"
                max-height="45px">
              </v-img>
            </td>
            <td style="min-width: 200px;">{{ props.item.diemTraKhach }}</td>
            <td style="min-width: 100px;">{{ props.item.vungTCTra }}</td>
            <td>{{ props.item.taiXeTra }}</td>
            <td @click="edithub(props.index,props.item.didId)">
              <div v-if="editingHub != props.index">{{props.item.hubDiemTra}}</div>
              <div v-if="editingHub == props.index">
                <v-autocomplete
                  :items="dsBenXeEdit"
                  v-model="props.item.hubDiemTraId"
                  item-value="id"
                  item-text="ten"
                  label="Hub nhận khách"
                  tabindex="5"
                  @change="onChangeHubTra(props.item.hubDiemTraId,props.item.bvvIds)"
                ></v-autocomplete>
              </div>
            </td>
            <td>{{ props.item.timeToHubTra }} phút</td>

            <td class="text-xs-center" style="min-width: 160px;">
              <div
                class="px-3 py-1"
                :class="{
                  'dadieu' : props.item.trangThaiTra == 1,
                  'dangdidon' : props.item.trangThaiTra == 2,
                  'dadon' : props.item.trangThaiTra == 3,
                  'dahuy' : props.item.trangThaiTra == 4,
                  'xetuyendon' : props.item.trangThaiTra == 10,
                  'xetuyenhuy' : props.item.trangThaiTra == 11,
                  'xengoaidon' : props.item.trangThaiTra == 12,
                }"
              >{{ mappingStatuses[props.item.trangThaiTra] }}</div>
            </td>
            <td>{{ props.item.lyDo }}</td>
          </template>
        </v-data-table>

        <v-layout row wrap class="mt-2">
          <v-spacer></v-spacer>
          <v-btn small class="blue white--text" @click="setCommandForGrab()" v-if="transitData && transitData.find(ts => ts.selected)">Điều ngoài</v-btn>
          <v-btn small class="blue white--text" @click="setCommandForLXTuyen()" v-if="transitData && transitData.find(ts => ts.selected)">Điều cho lái xe tuyến</v-btn>
          <v-btn small class="green white--text" @click="showUpdateCommandDialog()" v-if="transitData && transitData.find(ts => ts.selected)">Điều bổ sung</v-btn>
          <v-btn small class="green white--text" @click="showCommandDialog()" v-if="transitData && transitData.find(ts => ts.selected)">Đặt lệnh</v-btn>
        </v-layout>
      </v-card-text>
    </v-card>

    <CommandDialog
      v-if="selectedTasks.length > 0"
      :active="commandDialog"
      :transitData="selectedTasks"
      :dsLaiXe="dsLaiXe"
      :dsChiNhanh="dsChiNhanh"
      :dsVungHoatDong="dsVungTC"
      :ngayTimKiem="searchForm.ngayXuatBen.value"
      :dsHub="dsBenXe"
      type="tct"
      @close="onCommandDialogClosed"
    />
    <UpdateCommandDialog
      :active="updateDataDialog"
      :transitData="selectedTasks"
      :ngayTimKiem="searchForm.ngayXuatBen.value"
      type="tct"
      @close="onUpdateCommandDialogClosed"
    />

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
            <v-flex xs12>{{ 'Bạn có chắc muốn thay đổi hub trả?' }}</v-flex>
            <v-spacer></v-spacer>
          </v-layout>
        </v-card-title>
        <v-divider class="ma-0"></v-divider>

        <v-card-actions>
          <v-spacer></v-spacer>

          <v-btn
            color="green white--text"
            small
            @click="changeHubTra()"
          >
            Đồng ý
          </v-btn>

          <v-btn
            color=""
            small
            @click="confirmDialog = false;loadData()"
          >
            Đóng
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog
      v-model="confirmObject.isShow"
      max-width="500"
    >
      <v-card>
        <v-card-title class="pa-3">
          <v-layout row class="pa-0">
            <v-flex xs12>{{confirmObject.text}}</v-flex>
            <v-spacer></v-spacer>
          </v-layout>
        </v-card-title>
        <v-divider class="ma-0"></v-divider>

        <v-card-actions>
          <v-spacer></v-spacer>

          <v-btn
            color="green white--text"
            small
            ref="confirmBtn"
          >
            Đồng ý
          </v-btn>

          <v-btn
            color=""
            small
            @click="confirmObject.isShow = false"
          >
            Đóng
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-layout>
</template>

<script>
  import CommandDialog from './components/CommandDialog.vue';
  import UpdateCommandDialog from './components/UpdateCommandDialog.vue';
  import UILoading from '@/components/Loading.vue';

  export default {
    components : {
      CommandDialog,
      UpdateCommandDialog,
      UILoading
    },
    props : {
      isInterval : {
          default: true,
          type: Boolean
      },
    },
    data() {
      return {
        // Dialogs
        commandDialog     : false,
        updateDataDialog  : false,
        confirmDialog : false,
        // Data
        selectedTasks : [],
        isLoading   : false,
        radioOpions : [
          {value : 0, label : "Tất cả"},
          {value : 1, label : "Giờ A"},
          {value : 2, label : "Giờ B"}
        ],
        khoangThoiGian : [
           { value: 0, text: 'Không chọn'},
          { value: 30, text: "30'"},
          { value: 60, text: "1h"},
          { value: 90, text: "1h30'"},
          { value: 120, text: '2h'},
          { value: 150, text: "2h30'"},
          { value: 180, text: '3h'},
          { value: 210, text: "3h30'"},
          { value: 240, text: '4h'},
          { value: 270, text: "4h30'"},
          { value: 300, text: '5h'},
          { value: 330, text: "5h30'"},
          { value: 360, text: '6h'}
        ],
        dsTuyen : [],
        dsLenhLaiXe : [],
        nodes   : [],
          dsVungTC : [],
        mappingStatuses : {
          0 : "Chưa điều",
          1 : "Đã điều",
          2 : "Đang đi trả",
          3 : "Đã trả",
          4 : "Đã huỷ",
          5 : "Đã gán",
          10: "Xe tuyến đón",
          11: "Xe tuyến hủy",
          12: "Xe ngoài đón"
        },
        trangThaiVe:[
          {value: 0, trangThai:"Chưa điều"},
          {value: 1, trangThai:"Đã điều"},
          {value: 2, trangThai:"Đang đi đón"},
          {value: 3, trangThai:"Đã đón"},
          {value: 4, trangThai:"Đã hủy"},
          {value: 10, trangThai:"Xe tuyến đón"},
          {value: 11, trangThai:"Xe tuyến hủy"},
          {value: 12, trangThai:"Xe ngoài đón"}
        ],
        tcTrangThaiTra:[0,1,2,3,4],

        // Tasks
        taskHeaders: [
          { text: '', align: 'center', sortable: false, width: "10" },
          { text: 'STT', align: 'center', sortable: false },
          { text: 'Tuyến', align: 'left', sortable: false },
          { text: 'Giờ điều hành', align: 'left', sortable:false },
          { text: 'Giờ về bến', align: 'left', value : 'gioveben' },
          { text: 'Tên khách', align: 'left', sortable: false },
          { text: 'SĐT', align: 'left', sortable: false },
          { text: 'Số khách', align: 'left', sortable: false },
          { text: 'Hạng', align: 'left', sortable: false },
          { text: 'Điểm trả', align: 'left', sortable: false },
          { text: 'Vùng', align: 'left', sortable: false,width: "100" },
          { text: 'Người trả', align: 'left', sortable: false },
          { text: 'Hub nhận khách', align: 'center', sortable: false },
          { text: 'TG ra hub', align: 'center', sortable: false },
          { text: 'Trạng thái', align: 'center', sortable: false },
          { text: 'Lý do', align: 'left', sortable: false }
        ],
        transitData : [],
        tableLoading : false,
        dsChiNhanh:[],
        isWatchingPagination : false,
        // Intervals
        interval : '',
        intervalCounter : 60,
        //filter bvvId
        bvvIds: [],
        // Pagination
        rowPerPageItems       : [10, 20, 30, 50, 100, 200, 500],
        pagination_data       : {
          page        : 1,
          rowsPerPage : 10,
          totalItems  : 0
        },

        // Form
        searchForm : {
          timeOption      : 0,
          listTuyen       : [],
          ngayXuatBen     : {
            menu : false,
            value : new Date().toISOString().substr(0, 10)
          },
          khoangThoiGian  : null,
          notIdA          : null,
          notIdB          : null,
          vungTC: 0,
          hubId: 0,
          diemKDs:[]
        },

        // Message snackbar
        snackbar : {
          color: 'green',
          active : false,
          text : ''
        },
        hubIdToChange: 0,
        bvvIdsToChangeHubId:[],
        editingHub : -1,
        isWatchingPagination: false,
        dsBenXe: null,
        dsBenXeEdit: null,
        confirmObject: {
            text:'',
            isShow:false
        }
      }
    },
    computed : {
      pagination : {
        get() { return this.pagination_data; },
        set(value) { this.pagination_data = value }
      },
      diemkinhdoanh : () => {
          return process.env.VUE_APP_FILTER_CHINHANH == 'hasonhaivan';
      }
    },
    methods : {
      async loadData(isResetPagination) {
        this.isWatchingPagination = true;
        this.tableLoading = true;
        if(isResetPagination) {
          this.pagination.page = 1;
        }
        if (this.searchForm.trangThaiVe && this.searchForm.trangThaiVe.length >0){
            this.tcTrangThaiTra = this.searchForm.trangThaiVe
        } else {
            this.tcTrangThaiTra=[0,1,2,3,4,10,11,12];
        }

        let criterials = {
          loaiGio : this.searchForm.timeOption,
          vungTC: this.searchForm.vungTC,
          trangThaiTC: this.tcTrangThaiTra,
          hubId: this.searchForm.hubId,
          diemKDs: this.searchForm.diemKDs
        };

        if(this.searchForm.timeOption == 1) criterials.notIds = this.searchForm.notIdA ? this.searchForm.notIdA:null;
        if(this.searchForm.timeOption == 2) criterials.notIds = this.searchForm.notIdB ? this.searchForm.notIdB:null;
        if(this.searchForm.khoangThoiGian) criterials.khoangThoiGian = this.searchForm.khoangThoiGian;
        if(this.searchForm.ngayXuatBen) criterials.ngayXuatBen = this.searchForm.ngayXuatBen.value;
        if(this.searchForm.listTuyen.length > 0) criterials.tuyIds = this.searchForm.listTuyen;
        if(this.bvvIds) {
              if(!Array.isArray(this.bvvIds) ){
                  this.bvvIds = [this.bvvIds]
              }
              Object.assign(criterials, {bvvIds: this.bvvIds});
          }

        let transitRes = await this.$axios.post(`/dieudo/tct/timkiem`, {
          ...criterials,
          page : this.pagination.page,
          size : this.pagination.rowsPerPage,
          sortBy: this.pagination.sortBy,
          sortType: this.pagination.descending ? 'DESC' : 'ASC'
        });

        this.transitData = transitRes.data.content;
        this.pagination.totalItems = transitRes.data.totalElement;
        this.tableLoading = false;

        // Create Interval
        if(this.isInterval) {
          this.intervalCounter = 60;
          if (this.interval == null) {
              // start interval
              let self = this;
              this.interval = setInterval(function () {
                  self.intervalCounter -= 1;
                  if (self.intervalCounter == 0) {
                      self.intervalCounter = 60;
                      self.loadData();
                  }
              }, 1000)
          }
        }
      },

      async loadNodes() {
        let criterials = {
          listTuyen     : this.searchForm.listTuyen,
          ngayXuatBen   : this.searchForm.ngayXuatBen.value,
          chieuXeChay   : 0
        };

        if(this.searchForm.timeOption == 0)
          return;
        else
          criterials.chieuXeChay = this.searchForm.timeOption;

        this.isLoading = true;
        let dataRes   = await this.$axios.post(`/dieudo/dsnottuyen`, criterials);

        this.nodes    = dataRes.data;

        this.searchForm.notIdA = null;
        this.searchForm.notIdB = null;

        this.isLoading = false;
      },

      async onTimeOptionChanged() {
        if(this.searchForm.timeOption == 0) return;
        else await this.loadNodes();
      },

      // Command dialog
      showCommandDialog() {
        this.commandDialog = true;
        this.selectedTasks = this.transitData.filter(transit => transit.selected);
      },

      showUpdateCommandDialog(){
        this.updateDataDialog = true
        this.selectedTasks    = this.transitData.filter(transit => transit.selected);
      },

      onCommandDialogClosed(message){
        if(message) this.showMessage('green', message);
        this.commandDialog = false
        this.loadData();
      },

      onUpdateCommandDialogClosed(message) {
        if(message) this.showMessage('green', message);
        this.updateDataDialog = false;
        this.loadData();
      },

      showMessage(color, message) {
        this.snackbar.active  = true;
        this.snackbar.color   = color;
        this.snackbar.text    = message;
      },
      onChangeHubTra(e,ids){
          this.confirmDialog = true;
          this.hubIdToChange = e;
          this.bvvIdsToChangeHubId = ids;
      },
      async changeHubTra(){
          try {
              let update = await this.$axios.post('/dieudo/updatehub/tct', {
                  veIds: this.bvvIdsToChangeHubId,
                  hubId: this.hubIdToChange,
              });
              this.showCommandDialog('Cập nhật thành công');
              this.loadData();
          }catch (e) {
              this.snackbar.active = true;
              this.snackbar.text = e.response.data.message;
          }
          this.confirmDialog = false;
      },
      async edithub(seletectedRowIndex,tripId) {
          this.editingHub = seletectedRowIndex;
          try {
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
      async setCommandForLXTuyen(){
          if (this.processing === true) {
              return;
          }
          this.processing = true;
          this.selectedTasks = this.transitData.filter(transit => transit.selected);
          try {
              let saveRes = await this.$axios.post('/dieudo/tct/dieulenhlaixetuyen', {
                  danhSachVe : this.selectedTasks.map(ts => {
                      return {bvvIds : ts.bvvIds, ghiChu : ts.ghiChu}
                  }),
                  taiXeId   : this.taiXeId,
              });
              this.loadData();
              this.showMessage('green','Điều xe tuyến thành công');
              this.processing = false;
          } catch(err) {
              this.snackbar.active = true;
              this.snackbar.text = err.response.data.message;
              this.processing = false;
          }
      },
      async setCommandForGrab() {
          if (this.processing === true) {
              return;
          }
          this.processing = true;
          this.selectedTasks = this.transitData.filter(transit => transit.selected);
          try {
              let saveRes = await this.$axios.post('/dieudo/tct/dieulenhngoai', {
                  danhSachVe: this.selectedTasks.map(ts => {
                      return {bvvIds: ts.bvvIds, ghiChu: ts.ghiChu}
                  }),
                  taiXeId: this.taiXeId,
              });
              this.loadData();
              this.showMessage('green', 'Điều xe ngoài thành công');
              this.processing = false;
          } catch (err) {
              this.snackbar.active = true;
              this.snackbar.text = err.response.data.message;
              this.processing = false;
          }
      },
        loadBvvId() {
            if(this.$route.query.bvvids) {
                this.bvvIds = this.$route.query.bvvids;
            }
        }
    },
    async mounted(){
      await this.loadBvvId();
      await this.loadData();
      this.isWatchingPagination = true;
      let [dsTuyenRes, dsLaiXeRes,dsChiNhanhRes,dsVungTCRes,dsBenXeRest] = await Promise.all([
        this.$axios.get('/dieudo/dstuyen'),
        this.$axios.get(`/lichtruc/danhsachtaixe`),
        this.$axios.get(`/chinhanh/danhsach`),
        this.$axios.get(`/vungtrungchuyen/list`),
        this.$axios.get(`/benxe/danhsach`)
      ]);

      this.dsTuyen = dsTuyenRes.data;
      this.dsLaiXe = dsLaiXeRes.data;
      this.dsChiNhanh = dsChiNhanhRes.data;
      this.dsVungTC = dsVungTCRes.data;
      this.dsBenXe = dsBenXeRest.data;

      this.dsBenXeEdit = dsBenXeRest.data;
    },
    watch : {
      pagination : {
        async handler () {
          if (this.isWatchingPagination) await this.loadData()
        },
        deep: true
      }
    },
    created : function(){
      if(this.isInterval) {
          this.intervalCounter = 60;
          // start interval
          let self = this;
          this.interval = setInterval(() => {
              self.intervalCounter -= 1;
              if (self.intervalCounter === 0) {
                  self.intervalCounter = 60;
                  self.loadData();
              }
          }, 1000)
      }
    },
    beforeDestroy() {
        clearInterval(this.interval);
    }
  }
</script>
<style>
  .table-tc td, .table-tc th{
    padding: 0 8px !important;
  }
</style>
