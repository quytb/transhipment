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

            <v-flex xs6>
              <v-menu
                ref="selectedDate"
                v-model="searchForm.tuNgay.menu"
                lazy
                transition="scale-transition"
                offset-y
                full-width
                min-width="290px"
                tabindex="6"
              >
                <template v-slot:activator="{ on }">
                  <v-text-field
                    v-model="searchForm.tuNgay.value"
                    append-icon="event"
                    readonly
                    label="Từ ngày"
                    v-on="on"
                  ></v-text-field>
                </template>
                <v-date-picker v-model="searchForm.tuNgay.value" @change="onChangeFromDate" no-title scrollable>
                </v-date-picker>
              </v-menu>

              <v-autocomplete
                :items="dsChiNhanh"
                item-value="id"
                item-text="name"
                label="Điểm làm việc"
                v-model="searchForm.chiNhanh"
                @change="onChangeChiNhanh($event)"
                multiple
                chips
                deletable-chips
                clearable
              ></v-autocomplete>
            </v-flex>

            <v-flex xs6>
              <v-menu
                ref="selectedDate"
                v-model="searchForm.denNgay.menu"
                lazy
                transition="scale-transition"
                offset-y
                full-width
                min-width="290px"
                tabindex="6"
              >
                <template v-slot:activator="{ on }">
                  <v-text-field
                    v-model="searchForm.denNgay.value"
                    append-icon="event"
                    readonly
                    label="Đến ngày"
                    v-on="on"
                  ></v-text-field>
                </template>
                <v-date-picker v-model="searchForm.denNgay.value" no-title scrollable :allowed-dates="allowedToDates">
                </v-date-picker>
              </v-menu>

              <v-autocomplete
                :items="[{taiXeId : '0', taiXeTen : 'Tất cả'}].concat(dsTaiXe)"
                item-value="taiXeId"
                item-text="taiXeTen"
                label="Tài Xế"
                v-model="searchForm.taiXeId"
              ></v-autocomplete>

              <v-btn small class="green white--text ml-0" style="float: right" @click="search()">Tìm kiếm</v-btn>
            </v-flex>


          </v-layout>
        </v-container>
      </v-card-text>
    </v-card>

    <!-- Danh sách ca làm việc -->
    <v-card class="mt-4 pa-0 elevation-1">
      <v-card-title class="pa-3">
        <v-layout row wrap>
          <v-flex>Bảng báo cáo tổng hợp</v-flex>
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
          class="elevation-1"
        >
          <template v-slot:items="props">
            <td class="text-xs-center">{{ props.index + 1 }}</td>
            <td style="min-width: 200px;">{{ props.item.taiXeTen }}</td>
            <td>{{ props.item.maNV }}</td>
            <td>{{ props.item.noiLamViec }}</td>
            <td style="text-align: center">{{ props.item.tongLenhDon }}</td>
            <td style="text-align: center">{{ props.item.tongLenhTra }}</td>
            <td style="text-align: center">{{ props.item.tongLenhThanhCong }}</td>
            <td style="text-align: center">{{ props.item.tongLenhHuy }}</td>
            <td style="text-align: center">{{ props.item.tongKhachDon }}</td>
            <td style="text-align: center">{{ props.item.tongKhachTra }}</td>
            <td style="text-align: center">{{ props.item.tongKhachDon + props.item.tongKhachTra }}</td>
          </template>
        </v-data-table>
         <v-btn small class="ma-3 green white--text" @click="download()">In File</v-btn>
      </v-card-text>
    </v-card>
  </v-layout>
</template>

<script>
  import moment from 'moment';
  export default {
    computed : {
      pagination : {
        get(){ return this.pagination_data; },
        set(value) { this.pagination_data = value; }
      }
    },
    data() {
      return {
        isLoading : false,
        dsChiNhanh : [],
        dsTaiXe : [],

        // Tasks
        taskHeaders: [
          { text: 'STT', align: 'center', sortable: false },
          { text: 'Lái xe', align: 'left', sortable: false },
          { text: 'Mã số nhân viên', align: 'left', sortable: false },
          { text: 'Nơi làm việc', align: 'left', sortable: false },
          { text: 'Lệnh đón', align: 'center', sortable: false },
          { text: 'Lệnh trả', align: 'center', sortable: false },
          { text: 'Lệnh hoàn thành', align: 'center', sortable: false },
          { text: 'Lệnh hủy', align: 'center', sortable: false },
          { text: 'Số khách đón', align: 'center', sortable: false },
          { text: 'Số khách trả', align: 'center', sortable: false },
          { text: 'Tổng số khách', align: 'center', sortable: false},
        ],
        tasks : [],
        taskTableLoading : false,

        // Pagination
        rowPerPageItems     : [10, 20, 30, 50, 100, 200, 500],
        pagination_data     : {
          page        : 1,
          rowsPerPage : 500
        },

        // Search form
        searchForm : {
          tuNgay : {
            menu : false,
            value : new Date().toISOString().substr(0, 10)
          },
          denNgay : {
            menu : false,
            value : new Date().toISOString().substr(0, 10)
          },
          chiNhanh : [],
          idLaiXe  : 0
        },

      }
    },
    methods : {
      async search() {
        this.taskTableLoading = true;
        let criterials = {
          fromDate   : this.searchForm.tuNgay.value,
          endDate   : this.searchForm.denNgay.value,
          // endDate   : this.searchForm.tuNgay.value,
          chiNhanhId   : this.searchForm.chiNhanh,
          taiXeId   : this.searchForm.taiXeId
        };
        let taskRes = await this.$axios.post(`/baocaothang/danhsachlenh`, {
          ...criterials
        });

        this.tasks = taskRes.data;
        this.pagination.totalItems = 1000;

        this.taskTableLoading = false;

        // Create Interval
      },
      onChangeChiNhanh : async function(value){
        this.isLoading = true;
        let dsTaiXeRes = await this.$axios.post('/lichtruc/danhsachtaixe/chinhanh',{chiNhanh: value});
        this.dsTaiXe = dsTaiXeRes.data;
        this.isLoading = false;
      },
      allowedToDates(date) {
          return moment(this.searchForm.denNgay.value) <= moment(date) && moment(date) < moment(this.searchForm.denNgay.value).add(31, 'd');
      },
      onChangeFromDate(){
          this.searchForm.denNgay.value = moment(this.searchForm.tuNgay.value).add(0, 'd').format('YYYY-MM-DD');
      },
      async download() {
        let criterials = {
          fromDate   : this.searchForm.tuNgay.value,
          endDate   : this.searchForm.denNgay.value,
          // endDate   : this.searchForm.tuNgay.value,
          chiNhanhId   : this.searchForm.chiNhanh,
          taiXeId   : this.searchForm.taiXeId
        };
        this.$axios.post(`/baocaothang/baocaotonghop`, {...criterials} , {responseType: 'blob'})
          .then(response => {
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', `report_tonghop.xlsx`);
            document.body.appendChild(link);
            link.click();
          }).catch(error => {
          console.error(error)
        })
      }
    },
    async mounted() {
      this.searchForm.denNgay.value = moment().add(0, 'd').format('YYYY-MM-DD');
      let [dsTaiXeRes, dsChiNhanhRes] = await Promise.all([
        this.$axios.get(`/lichtruc/danhsachtaixe`),
        this.$axios.get(`/chinhanh/danhsach`)
      ]);

      this.dsTaiXe  = dsTaiXeRes.data;
      this.dsChiNhanh  = dsChiNhanhRes.data;
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
</style>
