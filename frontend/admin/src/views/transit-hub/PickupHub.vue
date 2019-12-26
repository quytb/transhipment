<template>
  <v-layout column>

    <!-- Tìm kiếm -->
    <v-card class="pa-0 elevation-1">

      <v-card-text class="pa-3">
        <v-container
          grid-list-xl
          class="pa-0"
          fluid
        >
          <el-collapse class="width100">
            <el-collapse-item title="Tìm kiếm">
              <v-divider class="ma-0"></v-divider>
              <v-layout row wrap>
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
                    label="Trước giờ xuất bến"
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
                    clearable
                  ></v-autocomplete>
                </v-flex>
                <v-flex xs3>
                  <v-autocomplete
                    v-model="searchForm.hubLamViec"
                    :items="[{id:'0',ten:'----------- Tất cả -----------'}].concat(dsBenXe)"
                    label="Hub làm việc hiện tại"
                    item-value="id"
                    item-text="ten"
                    tabindex="2"
                    clearable
                  ></v-autocomplete>
                  <v-text-field
                    v-model="searchForm.sdt"
                    label="Số điện thoại khách"
                    append-icon="search"
                    type="number"
                  ></v-text-field>
                  <v-autocomplete
                    v-model="searchForm.listTuyen"
                    :items="dsTuyen"
                    label="Chọn tuyến"
                    item-value="tuyId"
                    item-text="tenTuyen"
                    tabindex="2"
                    multiple
                    chips
                    clearable
                    @change="loadNodes()"
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
                    v-model="searchForm.notIdA"
                    :items="nodes"
                    item-value="notId"
                    item-text="MixData"
                    label="Chọn giờ A"
                    tabindex="4"
                    :disabled="[0, 2].includes(searchForm.timeOption)"
                    multiple
                    chips
                    clearable
                  ></v-autocomplete>

                  <v-autocomplete
                    :items="nodes"
                    v-model="searchForm.notIdB"
                    item-value="notId"
                    item-text="MixData"
                    label="Chọn giờ B"
                    tabindex="5"
                    multiple
                    chips
                    :disabled="[0, 1].includes(searchForm.timeOption)"
                    clearable
                  ></v-autocomplete>
                </v-flex>
                <v-flex xs3>
                  <v-btn medium class="green white--text ml-0" @click="loadData(true)">Tìm kiếm</v-btn>
                </v-flex>
              </v-layout>
            </el-collapse-item>
          </el-collapse>
        </v-container>
      </v-card-text>
    </v-card>

    <!-- Danh sách trung chuyển trả -->
    <v-card class="mt-4 pa-0 elevation-1">
      <el-tabs type="border-card" v-model="activeName">
        <el-tab-pane name="tab1" label="Danh sách khách hàng">
          <v-card-title class="pa-3">
            <v-layout row wrap>
              <v-flex>Danh sách trung chuyển đón</v-flex>
              <v-spacer></v-spacer>
              <v-flex class="text-xs-right"><v-btn medium class="blue white--text ml-0" @click="loadData(true)"><v-icon small>ti-reload</v-icon>&nbsp;Tải lại</v-btn></v-flex>
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
                <tr>
                  <td class="text-xs-center">
                    <v-checkbox
                      v-model="props.item.selected"
                      :disabled="cbdisalble[props.index]"
                      v-if="[0,4,11].includes(props.item.status)"
                      class="align-center justify-center"
                      primary
                      hide-details
                    ></v-checkbox>
                  </td>
                  <td class="text-xs-center">{{ props.index + 1 }}</td>
                  <td style="min-width: 200px;">{{ props.item.tuyen }}</td>
                  <td>{{ props.item.giodieuhanh }}</td>
                  <td>{{ props.item.tenHanhkhach }}</td>
                  <td>{{ props.item.sdtHanhkhach }}</td>
                  <td>{{ props.item.soluongKhach }}</td>
                  <td>
                    <v-img
                      :src="props.item.icon ? props.item.icon : ''" aspect-ratio="1"
                      :alt="props.item.rank"
                      :title="props.item.rank"
                      max-height="45px">
                    </v-img>
                  </td>
                  <td style="min-width: 200px;">{{ props.item.diachiHanhkhach }}</td>
                  <td style="min-width: 200px;">{{ props.item.lastLocation }}</td>
                  <td>{{ props.item.taiXeDon }}</td>
                  <td class="text-xs-center" style="min-width: 160px;">
                    <div
                      class="px-3 py-1"
                      :class="{
                        'dadieu' : props.item.status == 1,
                        'dangdidon' : props.item.status == 2,
                        'dadon' : props.item.status == 3,
                        'dahuy' : props.item.status == 4,
                        'xetuyendon' : props.item.status == 10,
                        'xetuyenhuy' : props.item.status == 11,
                        'xengoaidon' : props.item.status == 12,
                      }"
                    >{{ mappingStatuses[props.item.status] }}</div>
                  </td>
                  <td>{{ props.item.lyDo }}</td>
                </tr>
              </template>
            </v-data-table>

            <v-layout row wrap class="mt-2">
              <v-spacer></v-spacer>
              <v-btn small class="green white--text" @click="lenXe()">Lên xe</v-btn>
              <v-btn small class="green white--text" @click="dieuDon()">Điều đón</v-btn>
              <v-btn small class="green white--text" @click="showUpdateCommandDialog()">Điều bổ sung</v-btn>
            </v-layout>
          </v-card-text>
        </el-tab-pane>
        <el-tab-pane name="tab2"  label="Tạo lệnh">
          <v-card-title class="pa-3">
            <v-layout row wrap>
              <v-flex xs12>Khách đang được chọn</v-flex>
              <v-flex xs3 class="padding_10">
                <v-autocomplete
                  :items="listDiemTCDen"
                  v-model="diemTCDen"
                  item-value="id"
                  item-text="ten"
                  label="Điểm TC đến"
                  tabindex="5"
                  @change="onChangeDiemTCDen()"
                ></v-autocomplete>
              </v-flex>
              <v-flex xs3 class="padding_10">
                <v-autocomplete
                  :items="listXeTCDen"
                  v-model="xeTCDen"
                  item-value="xeId"
                  :item-text="itemText"
                  label="Chọn xe"
                  tabindex="5"
                ></v-autocomplete>
              </v-flex>
              <v-flex xs3 class="padding_10">
                <v-autocomplete
                  :items="listLaiXeTCDen"
                  v-model="laiXeTCDen"
                  item-value="taiXeId"
                  item-text="taiXeTen"
                  label="Chọn lái xe"
                  tabindex="5"
                ></v-autocomplete>
              </v-flex>
            </v-layout>
          </v-card-title>
          <v-divider class="ma-0"></v-divider>
          <v-card-text>
            <v-data-table
              :headers="taskHeadersLenXe"
              :items="transitDataLenXe"
              no-data-text="Không có dữ liệu"
              rows-per-page-text="Số bản ghi"
              :rows-per-page-items="itemPerPage"
              class="elevation-1 no-p-margin table-tc"
            >
              <template v-slot:items="props">
                <tr>
                  <td class="text-xs-center">{{ props.index + 1 }}</td>
                  <td>{{ props.item.tuyen }}</td>
                  <td>{{ props.item.giodieuhanh }}</td>
                  <td>{{ props.item.tenHanhkhach }}</td>
                  <td>{{ props.item.sdtHanhkhach }}</td>
                  <td>{{ props.item.soluongKhach }}</td>
                  <td>
                    <v-img
                      :src="props.item.icon ? props.item.icon : ''" aspect-ratio="1"
                      :alt="props.item.rank"
                      :title="props.item.rank"
                      max-height="45px">
                    </v-img>
                  </td>
                  <td style="min-width: 200px;">{{ props.item.diachiHanhkhach }}</td>
                  <td>
                      <v-autocomplete
                        :items="dsBenXeEdit"
                        v-model="props.item.hubDiemDonId"
                        ref="cbArrHubGiaoKhach"
                        item-value="id"
                        item-text="ten"
                        label="Hub giao khách"
                        tabindex="5"
                      ></v-autocomplete>
                  </td>
                  <td class="text-xs-center" style="min-width: 160px;">
                    <div
                      class="px-3 py-1"
                      :class="{
                          'dadieu' : props.item.status == 1,
                          'dangdidon' : props.item.status == 2,
                          'dadon' : props.item.status == 3,
                          'dahuy' : props.item.status == 4,
                          'xetuyendon' : props.item.status == 10,
                          'xetuyenhuy' : props.item.status == 11,
                          'xengoaidon' : props.item.status == 12,
                        }"
                    >{{ mappingStatuses[props.item.status] }}</div>
                  </td>
                  <td>
                    <v-btn small icon class="gray" @click="deleteLenXe(props.item)">
                      <v-icon>delete</v-icon>
                    </v-btn>
                  </td>
                </tr>
              </template>
            </v-data-table>

            <v-layout row wrap class="mt-2">
              <v-spacer></v-spacer>
              <v-btn small class="green white--text" @click="setCommand()">Điều lệnh</v-btn>
              <v-btn small class="green white--text" @click="setCommandDraft()">Tạo lệnh tạm</v-btn>
              <v-btn small class="red white--text" @click="confirmXoaLenh()">Xóa</v-btn>
            </v-layout>
          </v-card-text>
        </el-tab-pane>
      </el-tabs>
    </v-card>

    <CommandDialog
      :active="commandDialog"
      :transitData="selectedTasks"
      :dsLaiXe="dsLaiXe"
      :dsChiNhanh="dsChiNhanh"
      :dsVungHoatDong="dsVungTC"
      :ngayTimKiem="searchForm.ngayXuatBen.value"
      :dsHub="dsBenXe"
      type="tcd"
      @close="onCommandDialogClosed"
    />
    <UpdateCommandDialog
      :active="updateDataDialog"
      :transitData="selectedTasks"
      :ngayTimKiem="searchForm.ngayXuatBen.value"
      type="tcd"
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
            @click="confirmDialog = false;
            loadData()"
          >
            Đóng
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog
      v-model="confirmDialogXoaLenh"
      max-width="500"
    >
      <v-card>
        <v-card-title class="pa-3">
          <v-layout row class="pa-0">
            <v-flex xs12>Bạn có chắc muốn xóa lệnh này ?</v-flex>
            <v-spacer></v-spacer>
          </v-layout>
        </v-card-title>
        <v-divider class="ma-0"></v-divider>

        <v-card-actions>
          <v-spacer></v-spacer>

          <v-btn
            color="red white--text"
            small
            @click="xoaLenh()"
          >
            Đồng ý
          </v-btn>

          <v-btn
            color=""
            small
            @click="confirmDialogXoaLenh = false"
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
    export default {
        name: "PickupHub",
        components : {
            CommandDialog,
            UpdateCommandDialog
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
                confirmDialog       : false,
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
                dsLaiXe :[],
                dsChiNhanh:[],
                dsBenXe:[],
                dsBenXeEdit:[],
                activeName:'tab1',
                nodes   : [],
                dsVungTC : [],
                mappingStatuses : {
                    0 : "Chưa điều",
                    1 : "Đã điều",
                    2 : "Đang đi đón",
                    3 : "Đã đón",
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
                tcTrangThaiDon:[0,1,2,3,4],

                // Tasks
                taskHeaders: [
                    { text: '', align: 'center', sortable: false, width: "10" },
                    { text: 'STT', align: 'center', sortable: false },
                    { text: 'Tuyến', align: 'left', sortable: false },
                    { text: 'Giờ xuất bến', align: 'left', value: 'd.didGioDieuHanh'},
                    { text: 'Tên khách', align: 'left', sortable: false },
                    { text: 'SĐT', align: 'left', sortable: false },
                    { text: 'Số khách', align: 'left', sortable: false },
                    { text: 'Hạng', align: 'left', sortable: false },
                    { text: 'Điểm đón', align: 'left', sortable: false },
                    { text: 'Vị trí hiện tại', align: 'left', sortable: false,width: "100" },
                    { text: 'Người đón', align: 'left', sortable: false },
                    { text: 'Trạng thái', align: 'center', sortable: false },
                    { text: 'Lý do', align: 'left', sortable: false }
                ],
                taskHeadersLenXe: [
                    { text: 'STT', align: 'center', sortable: false },
                    { text: 'Tuyến', align: 'left', sortable: false },
                    { text: 'Giờ xuất bến', align: 'left', value: 'd.didGioDieuHanh'},
                    { text: 'Tên khách', align: 'left', sortable: false },
                    { text: 'SĐT', align: 'left', sortable: false },
                    { text: 'Số khách', align: 'left', sortable: false },
                    { text: 'Hạng', align: 'left', sortable: false },
                    { text: 'Điểm đón', align: 'left', sortable: false },
                    { text: 'Hub TC đến', align: 'center', sortable: false },
                    { text: 'Trạng thái', align: 'center', sortable: false },
                    { text: 'Bỏ', align: 'left', sortable: false }
                ],
                transitData : [],
                tableLoading : false,
                isWatchingPagination : false,
                // Intervals
                interval : null,
                intervalCounter : 60,

                // variable notification
                bvvIds:[],

                // Pagination
                rowPerPageItems       : [10, 20, 30, 50, 100, 200, 500],
                itemPerPage :[100],
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
                    khoangThoiGian  : 120,
                    notIdA          :[],
                    notIdB          :[],
                    vungTC: 0,
                    hubId: 0,
                    diemKDs:[],
                    hubLamViec:0,
                    sdt:''
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
                transitDataSelected:[],
                transitDataLenXeSelected:[],
                confirmDialogXoaLenh: false,
                transitDataLenXe:[],
                cbdisalble:[],
                diemTCDen:0,
                xeTCDen:0,
                laiXeTCDen:0,
                listDiemTCDen:[],
                listXeTCDen:[],
                listLaiXeTCDen:[]
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
                let criterials = {
                    sdtHanhkhach:this.searchForm.sdt,
                    // loaiGio : this.searchForm.timeOption,
                    currentHub : this.searchForm.hubLamViec
                };
                if(this.searchForm.timeOption == 0) criterials.notIds = null;
                if(this.searchForm.timeOption == 1) criterials.notIds = this.searchForm.notIdA ? this.searchForm.notIdA:null;
                if(this.searchForm.timeOption == 2) criterials.notIds = this.searchForm.notIdB ? this.searchForm.notIdB:null;
                if(this.searchForm.listTuyen.length > 0) criterials.tuyenIds = this.searchForm.listTuyen?this.searchForm.listTuyen:null;
                criterials.thoigianGiokhoihanh = this.searchForm.khoangThoiGian;
                if(this.searchForm.ngayXuatBen) criterials.ngayXuatben = this.searchForm.ngayXuatBen.value;


                let page = this.pagination.page;
                let size = this.pagination.rowsPerPage;
                let transitRes = await this.$axios.post(`/dieu-hanh-hub/trung-chuyen-don/tim-kiem?offset=`+ page*size +`&pageNumber=` +page+`&pageSize=` +size,
                {
                    ...criterials
                });
                this.transitData = transitRes.data.content;

                for(let i = 0;i < this.transitData.length; i++) {
                    let e = this.transitData[i];
                    if(this.transitDataLenXe.find(ele => this.findOne(ele.bvvIds,e.bvvIds))) {
                        this.cbdisalble[i] = true;
                    }else{
                        this.cbdisalble[i] = false;
                    }
                }

                this.pagination.totalItems = transitRes.data.totalElement;
                this.tableLoading = false;

                // Create Interval
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

            async setCommand(){
                if(this.diemTCDen == 0){
                    this.showSnackbar('red', 'Vui lòng chọn điểm cần TC đến');
                    return;
                }

                if(this.laiXeTCDen == 0){
                    this.showSnackbar('red', 'Vui lòng chọn lái xe');
                    return;
                }

                if(this.xeTCDen == 0){
                    this.showSnackbar('red', 'Vui lòng chọn xe');
                    return;
                }
                this.processing = true;
                try {
                    let saveRes = await this.$axios.post('/trung-chuyen-hub/tao-lenh', {
                        khachHubToHubForms : this.transitDataLenXe.map(ts => {
                            return {bvvIds : ts.bvvIds, status : ts.status, versions: ts.versions}
                        }),
                        typeCmd   : 1,
                        diemgiaokhach: this.diemTCDen,
                        trangthaiLenh: 1,
                        txId:this.laiXeTCDen,
                        xeId:this.xeTCDen
                    });
                    this.processing = false;
                } catch(err) {
                    this.snackbar.active = true;
                    this.snackbar.text = err.response.data.message;
                    this.processing = false;
                }

            },

            async setCommandDraft(){
                if(this.diemTCDen == 0){
                    this.showSnackbar('red', 'Vui lòng chọn điểm cần TC đến');
                    return;
                }
                if (this.processing === true) {
                    return;
                }
                this.processing = true;
                try {
                    let saveRes = await this.$axios.post('/trung-chuyen-hub/tao-lenh', {
                        khachHubToHubForms : this.transitDataLenXe.map(ts => {
                            return {bvvIds : ts.bvvIds, status : ts.status, versions: ts.versions}
                        }),
                        typeCmd   : 1,
                        diemgiaokhach: this.diemTCDen,
                        trangthaiLenh: 0,
                        txId:this.laiXeTCDen,
                        xeId:this.xeTCDen
                    });

                    this.showSnackbar('green', 'Tạo lệnh thành công');
                    this.processing = false;
                } catch(err) {
                    this.snackbar.active = true;
                    this.snackbar.text = err.response.data.message;
                    this.processing = false;
                }
            },

            onChangeHubDon(e,ids){
                this.confirmDialog = true;
                this.hubIdToChange = e;
                this.bvvIdsToChangeHubId = ids;
            },

            async changeHubDon(){
                try {
                    let update = await this.$axios.post('/dieudo/updatehub/tcd', {
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

            async onTimeOptionChanged() {
                if(this.searchForm.timeOption == 0) return;
                else await this.loadNodes();
            },

            // Command dialog
            showCommandDialog(message) {
                if(message) this.showMessage('green', message);
                this.commandDialog = true;
                this.selectedTasks = this.transitData.filter(transit => transit.selected);
            },

            showUpdateCommandDialog(message){
                if(message) this.showMessage('green', message);
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
                    let saveRes = await this.$axios.post('/dieudo/tcd/dieulenhlaixetuyen', {
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
            async setCommandForGrab(){
                if (this.processing === true) {
                    return;
                }
                this.processing = true;
                this.selectedTasks = this.transitData.filter(transit => transit.selected);
                try {
                    let saveRes = await this.$axios.post('/dieudo/tcd/dieulenhngoai', {
                        danhSachVe : this.selectedTasks.map(ts => {
                            return {bvvIds : ts.bvvIds, ghiChu : ts.ghiChu}
                        }),
                        taiXeId   : this.taiXeId,
                    });
                    this.loadData();
                    this.showMessage('green','Điều xe ngoài thành công');
                    this.processing = false;
                } catch(err) {
                    this.snackbar.active = true;
                    this.snackbar.text = err.response.data.message;
                    this.processing = false;
                }
            },
            loadBvvId() {
                if(this.$route.query.bvvids) {
                    this.bvvIds = this.$route.query.bvvids;
                }
            },
            lenXe(){
                this.transitDataSelected = this.transitData.filter(transit => transit.selected);
                for(let i = 0;i < this.transitDataSelected.length; i++) {
                    let e = this.transitDataSelected[i];
                    if(!this.transitDataLenXe.find(ele => this.findOne(ele.bvvIds,e.bvvIds))) {
                        if(this.diemTCDen != 0) {
                            e.hubDiemDonId = this.diemTCDen;
                        }
                        e.status = 3;
                        this.transitData = this.arrayRemove(this.transitData,e);
                        this.transitDataLenXe = this.transitDataLenXe.concat(e);
                    }
                }
                // this.activeName = 'tab2';
            },

            dieuDon(){
                this.transitDataSelected = this.transitData.filter(transit => transit.selected);
                for(let i = 0;i < this.transitDataSelected.length; i++) {
                    let e = this.transitDataSelected[i];
                    if(!this.transitDataLenXe.find(ele => this.findOne(ele.bvvIds,e.bvvIds))) {
                        if(this.diemTCDen != 0) {
                            e.hubDiemDonId = this.diemTCDen;
                        }
                        e.status = 1;
                        this.transitData = this.arrayRemove(this.transitData,e);
                        this.transitDataLenXe = this.transitDataLenXe.concat(e);
                    }
                }
            },

            itemText(item) { return item.bks + ' - đã chọn '+this.transitDataLenXe.length +'/' + item.seats },

            deleteLenXe(item){
                this.transitDataLenXeSelected = item;
                this.transitDataLenXe = this.arrayRemove(this.transitDataLenXe,item);
                this.loadData();
            },

            confirmXoaLenh(){
                this.confirmDialogXoaLenh = true;
            },

            xoaLenh(){
                this.transitDataLenXe = [];
                this.confirmDialogXoaLenh = false;
                this.loadData();
            },

            onChangeDiemTCDen() {
                for (let i = 0; i<this.transitDataLenXe.length; i++){
                    this.transitDataLenXe[i].hubDiemDonId = this.diemTCDen;
                    this.$refs.cbArrHubGiaoKhach.focus();
                }
            },

            arrayRemove(arr, value) {
                return arr.filter(function (ele) {
                    return ele != value;
                });
            },

            findOne (haystack, arr) {
                return arr.some(function (v) {
                    return haystack.indexOf(v) >= 0;
                });
            },

            showSnackbar(color, text){
                this.snackbar.active  = true;
                this.snackbar.text    = text;
                this.snackbar.color   = color;
            },

    },

        async mounted() {
            await this.loadBvvId();
            await this.loadData();
            this.isWatchingPagination = true;
            let [dsTuyenRes, dsLaiXeRes,dsChiNhanhRes,dsVungTCRes, dsBenXeRest] = await Promise.all([
                this.$axios.get('/dieudo/dstuyen'),
                this.$axios.get(`/lichtruc/danhsachtaixe`),
                this.$axios.get(`/chinhanh/danhsach`),
                this.$axios.get(`/vungtrungchuyen/list`),
                this.$axios.get(this.diemkinhdoanh ? '/benxe/danhsach?diemdontra=1' : '/benxe/danhsach')
            ]);

            this.dsTuyen = dsTuyenRes.data;
            this.dsLaiXe = dsLaiXeRes.data;
            this.dsChiNhanh = dsChiNhanhRes.data;
            this.dsVungTC = dsVungTCRes.data;
            this.dsBenXe = dsBenXeRest.data;
            this.dsBenXeEdit = dsBenXeRest.data;

            let [listDiemTCDenRes, listXeTCDenRes,listLaiXeTCDenRes] = await Promise.all([
                this.$axios.get('/dieu-hanh-hub/trung-chuyen-don/danh-sach-hub'),
                this.$axios.get(`/dieu-hanh-hub/trung-chuyen-don/danh-sach-xe`),
                this.$axios.get(`/dieu-hanh-hub/trung-chuyen-don/danh-sach-lai-xe`)
            ]);

            this.listDiemTCDen = listDiemTCDenRes.data;
            this.listXeTCDen = listXeTCDenRes.data;
            this.listLaiXeTCDen = listLaiXeTCDenRes.data;
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

        },
        beforeDestroy() {
            console.log('Distroy tcd');
            clearInterval(this.interval);
        }
    }
</script>

<style scoped>
  .padding_10{
    padding: 20px;
  }
</style>
