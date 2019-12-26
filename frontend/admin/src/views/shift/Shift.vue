<template>
  <v-layout column>

    <!-- Quản lý ca trực -->
    <v-card class="pa-0 elevation-1">
      <v-card-title class="pa-3">
        <v-layout row class="pa-0">
          <v-flex xs6>Tìm kiếm ca trực</v-flex>
        </v-layout>
      </v-card-title>
      <v-divider class="ma-0"></v-divider>

      <v-card-text class="px-3 py-0">
        <v-container
          grid-list-xl
          class="pa-0"
          fluid
        >
          <v-layout row wrap>
            <v-flex xs2>
              <v-text-field
                label="Tên ca"
                v-model="searchForm.tenCa"
                tabindex="1"
                required
              ></v-text-field>
            </v-flex>

            <v-flex xs2>
              <v-text-field
                label="Mã ca"
                v-model="searchForm.maCa"
                tabindex="1"
                required
              ></v-text-field>
            </v-flex>

            <v-flex xs2>
              <v-menu
                ref="startTime"
                v-model="searchForm.startTimeDialog"
                :close-on-content-click="false"
                lazy
                transition="scale-transition"
                offset-y
                full-width
                max-width="250px"
                min-width="250px"
              >
                <template v-slot:activator="{ on }">
                  <v-text-field
                    v-model="searchForm.gioBatDau"
                    label="Giờ bắt đầu"
                    v-on="on"
                  ></v-text-field>
                </template>
                <v-time-picker
                  v-if="searchForm.startTimeDialog"
                  v-model="searchForm.gioBatDau"
                  full-width
                  format="24hr"
                  :allowed-minutes="allowedStep"
                  @click:minute="$refs.startTime.save(searchForm.gioBatDau)"
                ></v-time-picker>
              </v-menu>
            </v-flex>

            <v-flex xs2>
              <v-menu
                ref="endTime"
                v-model="searchForm.endTimeDialog"
                :close-on-content-click="false"
                lazy
                transition="scale-transition"
                offset-y
                full-width
                max-width="250px"
                min-width="250px"
              >
                <template v-slot:activator="{ on }">
                  <v-text-field
                    v-model="searchForm.gioKetThuc"
                    label="Giờ kết thúc"
                    v-on="on"
                  ></v-text-field>
                </template>
                <v-time-picker
                  v-if="searchForm.endTimeDialog"
                  v-model="searchForm.gioKetThuc"
                  full-width
                  format="24hr"
                  :allowed-minutes="allowedStep"
                  @click:minute="$refs.endTime.save(searchForm.gioKetThuc)"
                ></v-time-picker>
              </v-menu>
            </v-flex>
            <v-flex xs2>
              <v-select
                :items="[{value : -1, text: 'Tất cả'}, {value: 1,  text : 'Hoạt động'}, {value: 0,  text : 'Không hoạt động'}]"
                item-value="value"
                item-text="text"
                :value="-1"
                label="Trạng thái"
                v-model="searchForm.trangThai"
              ></v-select>
            </v-flex>

            <v-flex xs2>
              <v-btn small class="green white--text ma-3" @click="search()">Tìm kiếm</v-btn>
            </v-flex>

          </v-layout>
        </v-container>
      </v-card-text>
    </v-card>

    <!-- Tạo ca Btn -->
    <v-flex>
      <v-btn
        small
        class="green white--text ma-0 mt-3 elevation-1"
        @click="dialogForm={}; dialogFormType='create'; createDialog = true"
      >Tạo ca</v-btn>
    </v-flex>


    <!-- Danh sách ca làm việc -->
    <v-card class="mt-3 pa-0 elevation-1">
      <v-card-title class="pa-3">Danh sách ca làm việc</v-card-title>
      <v-divider class="ma-0"></v-divider>

      <v-card-text>
        <v-data-table
          :headers="taskHeaders"
          :items="shifts"
          :rows-per-page-items="rowPerPageItems"
          :pagination.sync="pagination"
          :total-items="pagination.totalItems"
          :loading="tableLoading"
          no-data-text="Không có dữ liệu"
          rows-per-page-text="Số bản ghi"
          class="elevation-1"
        >
          <template v-slot:items="props">
            <td class="text-xs-center">{{ props.index + 1 }}</td>
            <td>{{ props.item.tenCa }}</td>
            <td>{{ props.item.maCa }}</td>
            <td>{{ props.item.gioBatDau | time }}</td>
            <td>{{ props.item.gioKetThuc | time }}</td>
            <td @click="confirmChangeStatus(props.item)">
              <div :class="
                props.item.trangThai ? 'px-2 green lighten-4 black--text' : 'px-2 yellow lighten-4 black--text'
              ">
                {{ mappingStatuses[props.item.trangThai] }}
              </div>
            </td>
            <td>{{ props.item.ghiChu }}</td>
            <td class="text-xs-center">
              <v-btn small icon class="gray" @click="edit(props.item)">
                <v-icon>edit</v-icon>
              </v-btn>
            </td>
          </template>
        </v-data-table>
      </v-card-text>
    </v-card>


    <!-- Create shift -->
    <v-dialog
      v-model="createDialog"
      max-width="800"
    >
      <v-card>
        <v-card-title class="pa-3">
          <v-layout row class="pa-0">
            <v-flex xs6>{{ dialogFormType == 'create' ? 'Thêm mới ca trực' : 'Chỉnh sửa ca trực' }}</v-flex>
            <v-spacer></v-spacer>
          </v-layout>
        </v-card-title>
        <v-divider class="ma-0"></v-divider>

        <v-card-text>
          <v-container
            grid-list-xl
            class="pa-0"
            fluid
          >
            <v-layout row wrap>
              <v-flex xs6>
                <v-text-field
                  label="Tên ca"
                  v-model="dialogForm.tenCa"
                  tabindex="2"
                  required
                ></v-text-field>

                <v-text-field
                  label="Mã ca"
                  v-model="dialogForm.maCa"
                  tabindex="2"
                  required
                ></v-text-field>

                <v-textarea
                  name="input-7-4"
                  v-model="dialogForm.ghiChu"
                  label="Ghi chú"
                ></v-textarea>
              </v-flex>

              <v-flex xs6>
                <v-menu
                  ref="startTime"
                  v-model="dialogForm.startTimeDialog"
                  :close-on-content-click="false"
                  lazy
                  transition="scale-transition"
                  offset-y
                  full-width
                  max-width="250px"
                  min-width="250px"
                >
                  <template v-slot:activator="{ on }">
                    <v-text-field
                      v-model="dialogForm.gioBatDau"
                      label="Giờ bắt đầu"
                      prepend-icon="access_time"
                      readonly
                      v-on="on"
                    ></v-text-field>
                  </template>
                  <v-time-picker
                    v-if="dialogForm.startTimeDialog"
                    v-model="dialogForm.gioBatDau"
                    full-width
                    format="24hr"
                    :allowed-minutes="allowedStep"
                    @click:minute="$refs.startTime.save(dialogForm.gioBatDau)"
                  ></v-time-picker>
                </v-menu>

                <v-menu
                  ref="endTime"
                  v-model="dialogForm.endTimeDialog"
                  :close-on-content-click="false"
                  lazy
                  transition="scale-transition"
                  offset-y
                  full-width
                  max-width="250px"
                  min-width="250px"
                >
                  <template v-slot:activator="{ on }">
                    <v-text-field
                      v-model="dialogForm.gioKetThuc"
                      label="Giờ kết thúc"
                      prepend-icon="access_time"
                      readonly
                      format="24hr"
                      v-on="on"
                    ></v-text-field>
                  </template>
                  <v-time-picker
                    v-if="dialogForm.endTimeDialog"
                    v-model="dialogForm.gioKetThuc"
                    full-width
                    :allowed-minutes="allowedStep"
                    @click:minute="$refs.endTime.save(dialogForm.gioKetThuc)"
                  ></v-time-picker>
                </v-menu>

                <v-layout row>
                  <v-flex xs5 class="px-0 py-0 text-xs-left" style="font-size: 16px; line-height: 28px; color: rgba(0,0,0,.54);">
                    Trạng thái
                  </v-flex>
                  <v-flex xs7 class="pa-0">
                    <v-checkbox
                      label="Hoạt động"
                      v-model="dialogForm.trangThai"
                      class="ma-0"
                      required
                    ></v-checkbox>
                  </v-flex>
                </v-layout>
              </v-flex>

            </v-layout>
          </v-container>
        </v-card-text>
        <v-divider class="ma-0"></v-divider>


        <v-card-actions>
          <v-spacer></v-spacer>

          <v-btn
            color="green darken-1 white--text"
            small
            v-if="dialogFormType == 'create'"
            @click="create()"
          >
            Tạo ca
          </v-btn>

          <v-btn
            color="green darken-1 white--text"
            small
            v-if="dialogFormType == 'edit'"
            @click="update()"
          >
            Cập nhật
          </v-btn>

          <v-btn
            color="red darken-1 white--text"
            small
            @click="createDialog = false"
          >
            Đóng
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- Confirm update status -->
    <v-dialog
      v-model="confirmDialog"
      max-width="500"
      v-if="updatingShift"
    >
      <v-card>
        <v-card-title class="pa-3">
          <v-layout row class="pa-0">
            <v-flex xs12>{{ `Cập nhật trạng thái ca ${this.updatingShift.maCa} thành "${this.updatingShift.trangThai == 1 ? 'Không hoạt động' : 'Đang hoạt động'}"?` }}</v-flex>
            <v-spacer></v-spacer>
          </v-layout>
        </v-card-title>
        <v-divider class="ma-0"></v-divider>

        <v-card-actions>
          <v-spacer></v-spacer>

          <v-btn
            color="green darken-1 white--text"
            small
            @click="confirmDialog = false; changeStatus()"
          >
            Cập nhật
          </v-btn>

          <v-btn
            color="red darken-1 white--text"
            small
            @click="confirmDialog = false"
          >
            Đóng
          </v-btn>
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
export default {
  data() {
    return {
      createDialog : false,
      mappingStatuses : {
        false : 'Không hoạt động',
        true : 'Đang hoạt động'
      },
      criterials : {
        trangThai : -1
      },

      // Form
      searchForm : {
        trangThai   : -1
      },
      dialogForm : {},
      dialogFormType : 'create',


      // Tasks
      taskHeaders: [
        { text: 'STT', align: 'center', sortable: false },
        { text: 'Tên ca', align: 'left', sortable: false },
        { text: 'Mã ca', align: 'left', sortable: false },
        { text: 'Giờ bắt đầu', align: 'left', sortable: false },
        { text: 'Giờ kết thúc', align: 'left', sortable: false },
        { text: 'Trạng thái', align: 'left', sortable: false, width: "170" },
        { text: 'Ghi chú', align: 'left', sortable: false },
        { text: '', align: 'center', sortable: false, width: "50" },
      ],
      shifts : [],
      tableLoading : false,
      rowPerPageItems : [10, 20, 50, 100, 500],
      pagination_data      : {
        page        : 0,
        rowsPerPage : 10
      },
      isWatchingPagination : false,

      // Update status
      updatingShift : null,
      confirmDialog : false,

      // Message snackbar
      snackbar : {
        active  : false,
        text    : 'default message',
        color   : 'red'
      },
      processing: false
    }
  },
  computed : {
    pagination : {
      get() { return this.pagination_data; },
      set(value) { this.pagination_data = value }
    }
  },
  filters : {
    time : value =>  {
      let [hour, minute] = value.toString().split(".");
      minute = minute ? minute.length == 2 ? minute : minute + '0' : '00';
      return `${hour}:${minute}`;
    }
  },
  methods : {
    async fetchShifts(){
      try {
        let shiftRes = await this.$axios.post(`/catruc/timkiem`, Object.assign({
          "page": this.pagination.page,
          "size": this.pagination.rowsPerPage
        }, this.criterials));

        this.shifts = shiftRes.data.content;
        this.pagination.totalItems = shiftRes.data.totalElement;
      } catch (err) {
        // TODO: Handle error
        console.log(err);
      }
    },
    allowedStep: m => m % 5 === 0,
    async create() {
      if (this.processing === true) {
          return;
      }
      this.processing = true;
      try {
        let shiftRes = await this.$axios.post(`/catruc/taomoi`, {
          createdBy    : 1,
          ghiChu      : this.dialogForm.ghiChu,
          gioBatDau   : parseFloat(this.dialogForm.gioBatDau.replace(':', '.')),
          gioKetThuc  : parseFloat(this.dialogForm.gioKetThuc.replace(':', '.')),
          maCa        : this.dialogForm.maCa,
          tenCa       : this.dialogForm.tenCa,
          trangThai   : !!this.dialogForm.trangThai
        });

        this.createDialog = false;
        await this.fetchShifts();
        this.showMessage('green', 'Tạo ca thành công');
        this.processing = false;
      } catch(err) {
        // TODO: Error handle
        this.createDialog = false;
        this.processing = false;
        console.log(err);
      }
    },

    edit(shift) {
      this.dialogForm             = Object.assign({}, shift);
      this.dialogForm.gioBatDau   = this.parseTime(shift.gioBatDau);
      this.dialogForm.gioKetThuc  = this.parseTime(shift.gioKetThuc);
      this.dialogForm.tcCaId      = shift.tcCaId;
      this.dialogFormType         ='edit';
      this.createDialog           = true;
    },

    async update() {
      try {
        this.tableLoading = true;
        let shiftRes = await this.$axios.post(`/catruc/capnhat`, {
          tcCaId      : this.dialogForm.tcCaId,
          ghiChu      : this.dialogForm.ghiChu,
          gioBatDau   : parseFloat(this.dialogForm.gioBatDau.replace(':', '.')),
          gioKetThuc  : parseFloat(this.dialogForm.gioKetThuc.replace(':', '.')),
          maCa        : this.dialogForm.maCa,
          tenCa       : this.dialogForm.tenCa,
          trangThai   : this.dialogForm.trangThai ? 1 : 0
        });

        this.createDialog = false;
        await this.fetchShifts();
        this.tableLoading = false;
      } catch(err) {
        // TODO: Error handle
        this.createDialog = false;
        this.tableLoading = false;
        this.snackbar.active = true;
        this.snackbar.text = `LỖI: ${err.response.data.message}`;
        console.log(err);
      }
    },

    parseTime(value) {
      let [hour, minute] = value.toString().split(".");
      minute = minute ? minute.length == 2 ? minute : minute + '0' : '00';
      return `${hour}:${minute}`;
    },

    confirmChangeStatus(shift) {
      this.updatingShift = shift;
      this.confirmDialog = true;
    },

    async changeStatus() {
      this.tableLoading = true;
      await this.$axios.get(`/catruc/trangthaica/${this.updatingShift.tcCaId}/${this.updatingShift.trangThai == 1 ? 0 : 1}`);
      await this.fetchShifts();
      this.showMessage('green', 'Cập nhật trạng thái thành công');
      this.tableLoading = false;
    },

    async search() {
      try {
        this.criterials = {
          maCa        : this.searchForm.maCa,
          tenCa       : this.searchForm.tenCa,
          trangThai   : this.searchForm.trangThai
        }
        if(this.searchForm.gioBatDau)
          this.criterials.gioBatDau = parseFloat(this.searchForm.gioBatDau.replace(':', '.'))

        if(this.searchForm.gioKetThuc)
          this.criterials.gioKetThuc = parseFloat(this.searchForm.gioKetThuc.replace(':', '.'))

        this.fetchShifts();
      } catch(err){
        console.log(err);
      }
    },

    showMessage(color, message) {
      this.snackbar.active  = true;
      this.snackbar.color   = color;
      this.snackbar.text    = message;
    }
  },

  async mounted() {
    await this.fetchShifts();
    this.isWatchingPagination = true;
  },
  watch : {
    pagination : {
      async handler () {
        if(this.isWatchingPagination)
          await this.fetchShifts()
      },
      deep: true
    }
  }
}
</script>


<style>
</style>
