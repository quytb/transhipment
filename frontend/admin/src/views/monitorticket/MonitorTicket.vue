<template>
  <div>
    <v-card class="pa-0 elevation-1">
      <v-card-title class="px-12 py-0">
        <v-layout row wrap>
          <v-flex xs3 class="py-0 px-3" style="font-weight: normal">
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
                  label="Ngày"
                  append-icon="event"
                  readonly
                  style="display: inline-block;"
                  v-on="on"
                ></v-text-field>
              </template>
              <v-date-picker v-model="selectedDate.value" no-title scrollable />
            </v-menu>
          </v-flex>
          <v-flex xs3>
            <v-radio-group v-model="trangThai"  row>
              <v-radio
                v-for="r in radioOpions"
                :key="r.value"
                :label="r.label"
                :value="r.value"
              ></v-radio>
            </v-radio-group>
          </v-flex>

          <v-flex xs2 class="py-0 px-3">
            <v-btn
              class="green white--text"
              style="display: inline-block; margin-left: 20px;margin-top: 20px"
              small
              @click="loadMonitorTicketData()"
            >Kiểm tra</v-btn>
          </v-flex>
        </v-layout>
      </v-card-title>

      <v-divider class="ma-0"></v-divider>

      <!-- Monitor transfer ticket -->
      <v-card class="mt-3 pa-0 elevation-1">
        <v-card-title class="pa-3">GIÁM SÁT HOẠT ĐỘNG VÉ GIỮA ERP & TRUNG CHUYỂN</v-card-title>
        <v-divider class="ma-0"></v-divider>
        <v-card-text>
          <v-data-table
            :headers="monitorticketTableHeaders"
            :items="monitorticketData"
            :rows-per-page-items="rowPerPageItems"
            :pagination.sync="pagination"
            :total-items="pagination.totalItems"
            :loading="tableLoading"
            no-data-text="Không có dữ liệu"
            rows-per-page-text="Số bản ghi"
            class="elevation-0"
            w="100%"
          >
            <template v-slot:items="props">
              <td class="text-xs-center">{{ props.index + 1 }}</td>
              <td>{{ props.item.monitorTrip }}</td>
              <td class="text-xs-center">{{ props.item.monitorTuyen }}</td>
              <td class="text-xs-center">{{ props.item.monitorBks }}</td>
              <td class="text-xs-center">{{ props.item.monitorChieu }}</td>
              <td class="text-xs-center">{{ props.item.monitorGxb }}</td>
              <td class="text-xs-center">{{ props.item.monitorCountVeErpDon }}</td>
              <td class="text-xs-center">{{ props.item.monitorCountVeTcDon }}</td>
              <td class="text-xs-center">{{ props.item.monitorCountVeErpTra }}</td>
              <td class="text-xs-center">{{ props.item.monitorCountVeTcTra }}</td>
              <td class="text-xs-center">
                <v-btn
                  class="green white--text"
                  style="display: inline-block"
                  small
                  @click="edit(props.item)"
                  :disabled="[-1, 1].includes(props.item.status)"
                >Cập nhật</v-btn>
              </td>
            </template>
          </v-data-table>
        </v-card-text>
      </v-card>
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
      <v-btn dark flat @click="snackbar.active = false">Đóng</v-btn>
    </v-snackbar>
  </div>
</template>

<script>
  import moment from "moment";
  export default {
    data() {
      return {

        selectedDate: {
          menu: false,
          value: new Date().toISOString().substr(0, 10)
        },
        radioOpions : [
          {value : -1, label : "Tất cả"},
          {value : 1, label : "Cân khớp vé"},
          {value : 0, label : "Lệch vé"}
        ],

        monitorticketTableHeaders: [
          { text: "STT", align: "center", sortable: false },
          { text: "Mã chuyến đi", align: "left", sortable: false },
          { text: "Tuyến", align: "center", sortable: false },
          { text: "Biển kiểm soát", align: "center", sortable: false },
          { text: "Chiều xe chạy", align: "center", sortable: false },
          { text: "Giờ xuất bến", align: "center", sortable: false },
          { text: "Vé đón (ERP)", align: "center", sortable: false },
          { text: "Vé đón (Trung chuyển)", align: "center", sortable: false },
          { text: "Vé trả (ERP)", align: "center", sortable: false },
          { text: "Vé trả (Trung chuyển)", align: "center", sortable: false },
          { text: "Action", align: "left", sortable: false }
        ],
        monitorticketData: [],
        tableLoading: false,
        rowPerPageItems: [10, 20, 50, 100, 500],
        pagination_data: {
          page: 0,
          rowsPerPage: 10
        },
        isWatchingPagination: false,
        trangThai: -1,

        // Message snackbar
        snackbar: {
          active: false,
          text: "",
          color: "red"
        }
      };
    },
    computed: {
      pagination: {
        get() {
          return this.pagination_data;
        },
        set(value) {
          this.pagination_data = value;
        }
      }
    },

    methods: {
      async loadMonitorTicketData() {
        this.snackbar.active = false;
        this.monitorticketData = [];
        this.tableLoading = true;
        try {
          let monitorTicketRes = await this.$axios.get(
            `/monitor/transferTicket/${moment(this.selectedDate.value).format('YYYY-MM-DD')}/-1`
          );
          this.monitorticketData = monitorTicketRes.data;
          if(this.trangThai ==0){
            this.monitorticketData = monitorTicketRes.data.filter(function(items){
              return items.status === 0;
            });
          }else if( this.trangThai == 1){
            this.monitorticketData = monitorTicketRes.data.filter(function(items){
              return items.status === 1;
            });
          }else{
            this.monitorticketData = monitorTicketRes.data;
          }
        } catch (err) {
          this.snackbar.active = true;
          this.snackbar.text = err.response.data.message;
        }
        this.tableLoading = false;
      },

      async edit(monitor) {
        try {
          await this.$axios.get( `/monitor/updateTransferTicket/${monitor.monitorTrip}`);
          await this.loadMonitorTicketData();
        } catch (err){
          this.snackbar.active = true;
          this.snackbar.text = err.response.data.message;
        }

      },

      parseTime(value) {
        let [hour, minute] = value.toString().split(".");
        minute = minute ? (minute.length == 2 ? minute : minute + "0") : "00";
        return `${hour}:${minute}`;
      }
    },

    async mounted() {
      await this.loadMonitorTicketData();
      this.isWatchingPagination = true;
    },

    watch: {
      pagination: {
        async handler() {
          if (this.isWatchingPagination) await this.loadMonitorTicketData();
        },
        deep: true
      }
    }
  };
</script>
<style>
</style>
