<template>
  <v-layout column>

    <!-- Quản lý ca trực -->
    <v-card class="pa-0 elevation-1">
      <v-card-text class="pa-3">
        <v-container
          grid-list-xl
          class="pa-0"
          fluid
        >
          <v-layout row wrap>
            <v-flex xs3>
              <v-menu
                ref="fromDate"
                v-model="fromDate.menu"
                lazy
                transition="scale-transition"
                offset-y
                full-width
                min-width="290px"
              >
                <template v-slot:activator="{ on }">
                  <v-text-field
                    v-model="fromDate.value"
                    label="Từ ngày"
                    prepend-icon="event"
                    readonly
                    v-on="on"
                  ></v-text-field>
                </template>
                <v-date-picker v-model="fromDate.value" no-title scrollable/>
              </v-menu>
            </v-flex>

            <v-flex xs3>
              <v-menu
                ref="toDateMenu"
                v-model="toDate.menu"
                lazy
                transition="scale-transition"
                offset-y
                full-width
                min-width="290px"
              >
                <template v-slot:activator="{ on }">
                  <v-text-field
                    v-model="toDate.value"
                    label="Đến ngày"
                    prepend-icon="event"
                    readonly
                    v-on="on"
                  ></v-text-field>
                </template>
                <v-date-picker v-model="toDate.value" no-title scrollable :allowed-dates="allowedToDates">
                </v-date-picker>
              </v-menu>
            </v-flex>

            <v-flex xs3>
              <!-- filter -->
              <v-autocomplete
                :items="dsChiNhanh"
                item-value="id"
                item-text="name"
                label="Điểm làm việc"
                v-model="chiNhanh"
                @change="onChangeChiNhanh($event)"
                multiple
                chips
                deletable-chips
                clearable
              ></v-autocomplete>
            </v-flex>

            <v-flex xs3 class="text-xs-center">
              <v-btn class="green white--text ma-3" small @click="load()">Xem</v-btn>
            </v-flex>

            <v-divider class="ma-0"></v-divider>

            <v-flex xs9>
              <!-- filter -->
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
            <v-flex xs12 class="pa-0">
              <v-data-table
                :headers="calendarHeaders"
                :items="calendarData"
                :rows-per-page-items="rowPerPageItems"
                :pagination.sync="pagination"
                no-data-text="Không có dữ liệu"
                rows-per-page-text="Số bản ghi"
                class="elevation-1 calendar-table"
              >
                <template v-slot:headers="props">
                  <tr>
                    <th
                      v-for="header in props.headers"
                      :key="header.text"
                      v-html="header.text"
                      class="text-xs-center"
                      :style="{
                        'min-width': header.text == 'Tài xế' ? '200px !important' : '',
                        'text-align': header.text == 'Tài xế' ? 'left !important' : ''
                      }"
                    >
                    </th>
                  </tr>
                </template>
                <template v-slot:items="props">
                  <td class="text-xs-left">{{ getDriverName(props.item.taiXeId) }}</td>

                  <td
                    class="text-xs-center"
                    v-for="index in calendarHeaderContents"
                    :key="index"
                    @click="editOneDay(index, props.item)"
                  >{{ props.item[index] }}</td>

                  <td class="text-xs-center">

                    <v-btn icon @click="editCalendarRow(props.item)">
                      <v-icon>edit</v-icon>
                    </v-btn>
                  </td>
                </template>
              </v-data-table>

              <!-- Dialog -->
              <v-dialog v-model="editDialog" fullscreen hide-overlay transition="dialog-bottom-transition">
                <v-card v-if="editDialog">
                  <v-toolbar dark color="primary">
                    <v-toolbar-title class="white--text">Phân lịch cho tài xế: {{getDriverName(dialogForm.taiXeId)}}</v-toolbar-title>
                    <v-spacer></v-spacer>
                    <v-btn class="red white--text" small @click="closeDialog()">Đóng</v-btn>
                  </v-toolbar>

                  <v-layout column class="pa-3">

                    <!-- Thông tin lịch trực -->
                    <v-card class="pa-0 elevation-2">
                      <v-card-title class="pa-3">Thông tin lịch trực</v-card-title>
                      <v-divider class="ma-0"></v-divider>

                      <v-card-text class="pa-3">
                        <v-container
                          grid-list-xl
                          text-xs-center
                          class="pa-0"
                          fluid
                        >
                          <v-layout row wrap>
                            <v-flex xs3 class="py-0 px-3">
                              <v-menu
                                v-model="dialogForm.fromDate.menu"
                                lazy
                                transition="scale-transition"
                                offset-y
                                full-width
                                min-width="290px"
                              >
                                <template v-slot:activator="{ on }">
                                  <v-text-field
                                    v-model="dialogForm.fromDate.value"
                                    label="Từ ngày"
                                    prepend-icon="event"
                                    readonly
                                    v-on="on"
                                  ></v-text-field>
                                </template>
                                <v-date-picker v-model="dialogForm.fromDate.value" no-title scrollable />
                              </v-menu>
                            </v-flex>

                            <v-flex xs3 class="py-0 px-3">
                              <v-menu
                                v-model="dialogForm.toDate.menu"
                                lazy
                                transition="scale-transition"
                                offset-y
                                full-width
                                min-width="290px"
                              >
                                <template v-slot:activator="{ on }">
                                  <v-text-field
                                    v-model="dialogForm.toDate.value"
                                    label="Đến ngày"
                                    prepend-icon="event"
                                    readonly
                                    v-on="on"
                                  ></v-text-field>
                                </template>
                                <v-date-picker v-model="dialogForm.toDate.value" no-title scrollable />
                              </v-menu>
                            </v-flex>

                            <v-flex xs3 class="py-0 px-3">
                              <v-autocomplete
                                ref="dialogXeId"
                                label="Biển kiểm soát"
                                :items="cars"
                                item-value="xeId"
                                item-text="bks"
                                v-model="dialogForm.xeId"
                              >
                                <template v-slot:item="props">
                                  <span>
                                    {{props.item.bks}}<span v-if="props.item.bks"> - Loại xe:{{props.item.seats}} chỗ</span>
                                  </span>
                                </template>
                                <template v-slot:selection="props">
                                  <span>
                                    {{props.item.bks}}<span v-if="props.item.bks"> - Số ghế:{{props.item.seats}}</span>
                                  </span>
                                </template>
                              </v-autocomplete>
                              
                            </v-flex>

                            <v-flex xs3 class="py-0 px-3">
                              <v-autocomplete
                                ref="dialogCaId"
                                label="Chọn ca"
                                :items="sessions"
                                item-value="tcCaId"
                                item-text="maCa"
                                v-model="dialogForm.tcCaId"
                              ></v-autocomplete>
                            </v-flex>

                            <v-flex xs12 class="py-0 px-3">
                              <v-text-field
                                name="input-7-4"
                                label="Ghi chú"
                                v-model="dialogForm.ghiChu"
                              ></v-text-field>
                            </v-flex>

                            <v-flex xs12 class="text-xs-left pa-0 px-3">
                              <v-btn small class="green white--text ml-0" @click="genTasks()">Phân lịch</v-btn>
                            </v-flex>

                          </v-layout>
                        </v-container>
                      </v-card-text>
                    </v-card>

                    <!-- Danh sách lịch trực -->
                    <v-card class="mt-4 pa-0 elevation-1" v-if="isDataGenerated">
                      <v-card-title class="pa-3">Danh sách ca làm việc</v-card-title>
                      <v-divider class="ma-0"></v-divider>

                      <v-card-text>
                        <v-data-table
                          :headers="dialogTableHeaders"
                          :items="dialogTableData"
                          no-data-text="Không có dữ liệu"
                          rows-per-page-text="Số bản ghi"
                          hide-actions
                          class="elevation-1"
                        >
                          <template v-slot:items="props">
                            <tr @click="editRow(props.index)">
                              <td class="text-xs-center">{{ props.index + 1 }}</td>
                              <td class="text-xs-left">
                                {{ getDriverName(props.item.taiXeId) }}
                              </td>
                              <td class="text-xs-center">
                                <div class="text" v-if="editingRow != props.index">{{ getLicensePlate(props.item.xeId)}}</div>
                                <div class="text" v-if="editingRow == props.index">
                                  <v-autocomplete
                                    label="Biển kiểm soát"
                                    :items="cars"
                                    item-value="xeId"
                                    item-text="bks"
                                    v-model="dialogTableData[props.index].xeId"
                                  ></v-autocomplete>
                                </div>
                              </td>                             
                              <td class="text-xs-center">
                                {{ props.item.ngayTruc }}
                              </td>
                              <td class="text-xs-center">
                                <div class="text" v-if="editingRow != props.index">{{ getSessionCode(props.item.tcCaId) }}</div>
                                <div class="text" v-if="editingRow == props.index">
                                  <v-autocomplete
                                    ref="dialogTcCaId"
                                    label="Ca"
                                    :items="sessions"
                                    item-value="tcCaId"
                                    item-text="maCa"
                                    v-model="dialogTableData[props.index].tcCaId"
                                  ></v-autocomplete>
                                </div>
                              </td>
                              <td class="text-xs-left">
                                <div class="text" v-if="editingRow != props.index">{{ props.item.ghiChu }}</div>
                                <div class="text" v-if="editingRow == props.index">
                                  <v-text-field
                                    label="Ghi chú"
                                    v-model="dialogTableData[props.index].ghiChu"
                                  ></v-text-field>
                                </div>
                              </td>
                              <td>
                                <v-btn
                                  small
                                  icon
                                  v-if="deleteAble"
                                  @click="confirmDelete(props.item.tcCaTrucId)"
                                ><v-icon>delete</v-icon></v-btn>
                              </td>
                            </tr>
                          </template>
                        </v-data-table>

                        <v-btn small class="green white--text mt-3 ml-0" @click="saveTasks()">Lưu</v-btn>
                        <v-btn small class="red white--text mt-3 ml-1" @click="editDialog = false">Đóng</v-btn>
                      </v-card-text>
                    </v-card>
                  </v-layout>
                </v-card>

                <!-- Dialog Snackbar -->
                <v-snackbar
                  v-model="dialogSnackbar.active"
                  :color="dialogSnackbar.color"
                  :multi-line="false"
                  :timeout="5000000"
                  top
                  right
                  style="z-index: 9999999;"
                >
                  <span class="white--text">{{ dialogSnackbar.text }}</span>
                  <v-btn
                    dark
                    flat
                    @click="dialogSnackbar.active = false"
                  >
                    Đóng
                  </v-btn>
                </v-snackbar>
              </v-dialog>

            </v-flex>
          </v-layout>
        </v-container>
      </v-card-text>
    </v-card>

    <!-- Snackbar -->
    <v-snackbar
      v-model="snackbar.active"
      :color="snackbar.color"
      :multi-line="false"
      :timeout="5000000"
      top
      right
      style="z-index: 9999999;"
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
            <v-flex xs12>Bạn có chắc muốn xóa ca trực này?</v-flex>
            <v-spacer></v-spacer>
          </v-layout>
        </v-card-title>
        <v-divider class="ma-0"></v-divider>

        <v-card-actions>
          <v-spacer></v-spacer>

          <v-btn
            color="red white--text"
            small
            @click="deleteCaTruc()"
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
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-layout>
</template>

<script>
import moment from 'moment';

export default {
  data() {
    return {
      // Variables
      cars      : [],
      sessions  : [],
      drivers   : [],
      selectedDriver : null,
      fromDate  : {
        menu : false,
        value : moment().format('YYYY-MM-DD')
      },
      toDate    : {
        menu : false,
        value : moment().add(7, 'd').format('YYYY-MM-DD')
      },

      // Tasks
      calendarHeaders: [],
      calendarHeaderContents : [],
      calendarDataTmp : [],
      calendarData    : [],
      carlendarRawData: [],
      rowPerPageItems : [10, 20, 30, 50, 100, 200, 500],
      pagination      : {
        page        : 1,
        rowsPerPage : 10
      },

      // Dialog
      editDialog        : false,
      editingRow        : {},
      isDataGenerated   : false,
      dialogTableHeaders: [
        { text: 'STT', align: 'center', sortable: false, width: "60" },
        { text: 'Tài xế', align: 'left', sortable: false, width: "160"  },
        { text: 'BKS', align: 'center', sortable: false, width: "160"  },
        { text: 'Ngày trực', align: 'center', sortable: false, width: "120"  },
        { text: 'Ca trực', align: 'center', sortable: false, width: "160"  },
        { text: 'Ghi chú', align: 'left', sortable: false },
        { text: 'Xóa', align: 'left', sortable: false,width: "100" }
      ],
      dialogTableData : [],
      dialogForm      : {
        fromDate : {
          menu : false,
          value : moment().format('YYYY-MM-DD')
        },
        toDate : {
          menu : false,
          value : moment().add(7, 'd').format('YYYY-MM-DD')
        },
      },
      dsChiNhanh:[],
      chiNhanh:[],

      // Message snackbar
      snackbar : {
        active  : false,
        text    : '',
        color   : 'red'
      },

      dialogSnackbar : {
        active  : false,
        text    : '',
        color   : 'red'
      },
      processing: false,
      confirmDialog: false,
      caTrucId : 0,
      deleteAble : false
    }
  },
  methods : {
    editRow(rowIndex) {
      this.editingRow   = rowIndex;
    },   

    load() {
      this.tableLoading   = true;
      this.selectedDriver = null;
      this.initTableHeaders();
      this.fetchCalendarData();
      this.tableLoading = false;
    },

    async fetchCalendarData(){
      let dataRes = await this.$axios.post(`/lichtruc/dslichtrucchinhanh`, {
        "page"      : 1,
        "size"      : 1000,
        "startDate" : this.fromDate.value,
        "endDate"   : this.toDate.value,
        "chiNhanh"  : this.chiNhanh
      });
      let driversRes = await this.$axios.post(`/lichtruc/danhsachtaixe/chinhanh`, {chiNhanh: this.chiNhanh});

      this.calendarData = [];
      this.carlendarRawData = dataRes.data.content ? dataRes.data.content : [];
      this.drivers = driversRes.data ? driversRes.data : [];
      this.drivers.forEach(driver => {
        // Data for this driver that fetch from server
        let data = this.carlendarRawData.find(sd => sd.taiXeId == driver.taiXeId);
        let row = {taiXeId : driver.taiXeId};

        let i = 1;
        for (let m = moment(this.fromDate.value); m.diff(moment(this.toDate.value), 'days') <= 0; m.add(1, 'days')) {
          if(data){
            let ca = data.lichTrucDTOS.find(lt => lt.ngayTruc == m.format('YYYY-MM-DD'));
            row[`n${i}`] = ca ? ca.maCa : '';
          } else {
            row[`n${i}`] = '';
          }
          i++;
        }

        this.calendarData.push(row);
      });

      this.calendarDataTmp = this.calendarData;
    },

    initTableHeaders() {
      this.calendarHeaders = [];
      this.calendarHeaderContents = [];

      this.calendarHeaders = [{ text: 'Tài xế', align: 'left', sortable: false, width: "200" }];

      let i = 1;
      for (let m = moment(this.fromDate.value); m.diff(moment(this.toDate.value), 'days') <= 0; m.add(1, 'days')) {
        this.calendarHeaders.push({
          text : `${m.format('dddd')}<br \>(${m.format('YYYY-MM-DD')})`,
          sortable : false,
          align : 'center',
          width: "100"
        });
        this.calendarHeaderContents.push(`n${i}`);
        i++;
      }

      this.calendarHeaders.push({ text: 'Thêm/Sửa', align: 'center', sortable: false });
    },

    filterDriver() {
      if(this.selectedDriver){
        this.calendarData = this.calendarDataTmp.filter(cd => cd.taiXeId == this.selectedDriver);
      } else {
        this.calendarData = this.calendarDataTmp;
      }
    },

    // Dialog functions
    async editCalendarRow(row) {
      // Reset form
      this.dialogForm.fromDate  = Object.assign( {}, this.fromDate );
      this.dialogForm.toDate    = Object.assign( {}, this.toDate );
      this.dialogForm.taiXeId   = row.taiXeId;
      this.dialogForm.xeId      = null;
      this.dialogForm.tcCaId    = null;
      this.dialogForm.ghiChu    = '';
      this.editingRow           = -1;

      // Reset data
      let rowData   = this.carlendarRawData.find(raw => raw.taiXeId == row.taiXeId)
        ? this.carlendarRawData.find(raw => raw.taiXeId == row.taiXeId).lichTrucDTOS
        : [];
      this.dialogTableData      = rowData.map(rd => ({
        taiXeId     : rd.taiXeId,
        xeId        : rd.xeId,
        ngayTruc    : rd.ngayTruc,
        tcCaId      : rd.tcCaId,
        ghiChu      : rd.ghiChu
      }));

      this.isDataGenerated      = this.dialogTableData.length ? true : false;


      this.editDialog           = true;
    },

    genTasks() {
      // Validate data
      this.$refs.dialogCaId.error = false;
      this.$refs.dialogXeId.error = false;

      let hasError = false;
      if(!this.dialogForm.tcCaId){
        hasError = true;
        this.$refs.dialogCaId.error = true;
      }

      if(!this.dialogForm.xeId){
        hasError = true;
        this.$refs.dialogXeId.error = true;
      }

      if(hasError) return;
      // Generate data
      this.isDataGenerated      = true;
      this.dialogTableData      = [];

      for (let m = moment(this.dialogForm.fromDate.value); m.diff(moment(this.dialogForm.toDate.value), 'days') <= 0; m.add(1, 'days')) {
        this.dialogTableData.push({
          taiXeId   : this.dialogForm.taiXeId,
          xeId      : this.dialogForm.xeId,
          ngayTruc  : m.format('YYYY-MM-DD'),
          tcCaId      : this.dialogForm.tcCaId,
          ghiChu    : this.dialogForm.ghiChu
        })
      }
    },

    async saveTasks() {
      if (this.processing === true) {
          return;
      }
      this.processing = true;
      try {
        let res = await this.$axios.post(`/lichtruc/taolich`, this.dialogTableData);
        this.dialogTableData = [];
        this.isDataGenerated = false;

        this.snackbar.active  = true;
        this.snackbar.color   = "green lighten-1";
        this.snackbar.text    = 'Phân lịch thành công';

      } catch(err) {
        this.dialogSnackbar.active  = true;
        this.dialogSnackbar.color   = "red";
        this.dialogSnackbar.text    = err.response.data.message;
      }
        this.processing = false;
        this.closeDialog();

    },

    editOneDay(key, row) {
      let keyIndex = Object.keys(row).findIndex(ha => ha === key);
      let ngayTruc = moment(this.fromDate.value).add(keyIndex - 1, 'd').format('YYYY-MM-DD');

      let rowData   = this.carlendarRawData.find(raw => raw.taiXeId == row.taiXeId)
        ? this.carlendarRawData.find(raw => raw.taiXeId == row.taiXeId).lichTrucDTOS.filter(lt => lt.ngayTruc === ngayTruc)
        : [];

      // Reset form
      this.dialogForm.fromDate.value  = ngayTruc;
      this.dialogForm.toDate.value    = ngayTruc;
      this.dialogForm.taiXeId   = row.taiXeId;
      this.dialogForm.xeId      = null;
      this.dialogForm.tcCaId    = null;
      this.dialogForm.ghiChu    = '';
      this.editingRow           = 0;
      this.isDataGenerated      = true;

      // Reset data


      if(rowData.length) {
        this.deleteAble = true;
        this.dialogTableData      = rowData.map(rd => ({
          taiXeId     : rd.taiXeId,
          xeId        : rd.xeId,
          ngayTruc    : rd.ngayTruc,
          tcCaId      : rd.tcCaId,
          ghiChu      : rd.ghiChu,
          tcCaTrucId    : rd.tcLichId
        }));
      } else {
        this.deleteAble = false;
        this.dialogTableData = [{
          taiXeId     : row.taiXeId,
          ngayTruc    : ngayTruc
        }]
      }


      this.editDialog           = true;
    },

    getLicensePlate(id) {
      let car = this.cars.find(d => d.xeId == id)
      return car ? car.seats : id;
    },  

    getSessionCode(id) {
      return this.sessions.find(d => d.tcCaId == id).maCa;
    },

    getDriverName(id) {
      let driver = this.drivers.find(d => d.taiXeId == id);

      return driver ? driver.taiXeTen : "";
    },

    closeDialog(){
      this.editDialog = false;
      this.load();
    },

    allowedFromDates(val) {
      return moment(val) > moment();
    },

    allowedToDates(date) {
      return moment(this.fromDate.value) <= moment(date) && moment(date) < moment(this.fromDate.value).add(31, 'd');
    },

    async onChangeChiNhanh(value){
      //console.log(value);
    },
    confirmDelete(caTrucId) {
      this.caTrucId = caTrucId;
      this.confirmDialog = true;
    },
    async deleteCaTruc(){
      try {
          let deleted = await this.$axios.delete('/lichtruc/delete/' + this.caTrucId);
          this.snackbar.active  = true;
          this.snackbar.color   = "green lighten-1";
          this.snackbar.text    = 'Phân lịch thành công';
      }catch (e) {
          this.snackbar.active  = true;
          this.snackbar.color   = "red";
          this.snackbar.text    = e.response.data.message;
      }
      this.confirmDialog = false;
      this.closeDialog();
    }
  },
  async mounted() {
    moment.locale('vi');
    this.fromDate.value   = moment().format('YYYY-MM-DD');
    this.toDate.value     = moment().add(7, 'd').format('YYYY-MM-DD');

    let [carRes, sessionRes, driverRes, dschinhanhRes] = await Promise.all([
      this.$axios.get(`/lichtruc/danhsachxe`),
      this.$axios.get(`/catruc/activeca`),
      this.$axios.get(`/lichtruc/danhsachtaixe`),
      this.$axios.get(`/chinhanh/danhsach`)
    ]);

    this.cars       = carRes.data;
    this.sessions   = sessionRes.data;
    this.drivers    = driverRes.data;
    this.dsChiNhanh = dschinhanhRes.data;

    await this.load();
  },
}
</script>


<style>
</style>
