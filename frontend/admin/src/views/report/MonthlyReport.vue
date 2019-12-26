<template>
  <v-layout column>
    <v-card class="pa-0 elevation-1">
      <v-card-title class="px-3 py-0">

        <div style="display: inline-block;">
          <div style="display: inline-block; font-size: 18px; font-weight: bold; margin-right: 20px;">Bảng báo cáo chấm công</div>

          <v-menu
            v-model="showReportPicker"
            min-width="290px"
            v-if="reportMonth"
          >
            <template v-slot:activator="{ on }">
              <v-text-field
                :value="reportMonth | formatDate"
                readonly
                v-on="on"
                style="display: inline-block; margin-right: 20px; width: 100px;"
              ></v-text-field>
            </template>
            <v-date-picker v-model="reportMonth" no-title type="month" scrollable/>
          </v-menu>
        </div>

      </v-card-title>

      <v-divider class="ma-0"></v-divider>

      <v-layout row wrap>
        <v-flex xs3 class="py-0 px-3">

          <v-autocomplete
            :items="[{id : '0', name : 'Tất cả'}].concat(dsChiNhanh)"
            item-value="id"
            item-text="name"
            label="Điểm làm việc"
            v-model="chiNhanh"
          ></v-autocomplete>

        </v-flex>
        <v-flex xs3 class="py-0 px-3">

          <v-btn class="green white--text" style="display: inline-block; margin-left: 20px;margin-top: 20px" small @click="recall=true; fetchReport()">Tải dữ liệu</v-btn>
        </v-flex>
      </v-layout>

      <v-divider class="ma-0"></v-divider>

      <v-card-text class="pa-0">
        <v-data-table
          :headers="reportTableHeaders"
          :items="reportData"
          no-data-text="Không có dữ liệu"
          rows-per-page-text="Số bản ghi"
          hide-actions
          class="elevation-1"
        >
          <template v-slot:items="props">
            <td class="text-xs-center">{{ props.index + 1 }}</td>
            <td class="text-xs-left" style="min-width: 200px;">{{ props.item.taiXeTen }}</td>
            <td class="text-xs-center" v-for="dom in daysOfMonth" :key="dom">
              {{ props.item[dom] }}
            </td>
            <td class="text-xs-center">{{ props.item.tongCong }}</td>
          </template>
        </v-data-table>

        <v-btn small class="ma-3 green white--text" @click="download()">In File</v-btn>
      </v-card-text>
    </v-card>
  </v-layout>
</template>

<script>
  import moment from "moment";

  export default {
    data() {
      return {
        reportMonth : '',
        showReportPicker : false,

        // Table
        fakeTableHeaders : [
          { text: 'STT', align: 'center', sortable: false, width: "20" },
          { text: 'Tài xế', align: 'left', sortable: false, width: "200" }
        ],
        fakeData : [],
        daysOfMonth : [],

        reportTableHeaders : [],
        reportData          : [],

        // Pagination

        dsChiNhanh : [],
        chiNhanh:0,
        recall : false
      }
    },
    methods : {
      async fetchReport(){
        let fromDate = moment(this.reportMonth).startOf('month');
        let toDate   = moment(this.reportMonth).endOf('month').startOf('day');
        this.buildTableHeaders(fromDate, toDate)


        this.reportData = [];
        let res = await this.$axios.post(`/baocaothang/danhsach`, {thang : moment(this.reportMonth).format('MM/YYYY'),chiNhanh : this.chiNhanh});

        // Loop
        res.data.forEach(data => {
          let row = {taiXeTen : data.taiXeTen};

          let i = 1;
          for (let m = moment(fromDate); m.diff(moment(toDate), 'days') <= 0; m.add(1, 'days')) {
            let dateValue = data.duLieuThang.find(dl => dl.ngay == m.format('YYYY-MM-DD'));
            row[`n${i}`] = dateValue ? dateValue.cong : 0;
            i++;
          }

          row.tongCong = data.tongCong;
          this.reportData.push(row);
        });
      },

      buildTableHeaders(fromDate, toDate) {
        this.daysOfMonth = [];
        this.reportTableHeaders = [];

        this.reportTableHeaders = [
          {text : 'STT', align: 'center', width: "50", sortable: false},
          {text : 'Tài xế', align: 'left', width: "200", sortable: false},
        ]

        let i = 1;
        for (let m = moment(fromDate); m.diff(moment(toDate), 'd') <= 0; m.add(1, 'd')) {
          this.daysOfMonth.push(`n${i}`);
          this.reportTableHeaders.push({text : m.format('D'), align: 'center', sortable : false});
          i++;
        }

        this.reportTableHeaders.push({text : 'Tổng cộng', align: 'center', sortable : false})
      },

      async download() {
        this.$axios.post(`/baocaothang/xuatbaocao`, {thang : moment(this.reportMonth).format('MM/YYYY'), chiNhanh : this.chiNhanh } , {responseType: 'blob'})
          .then(response => {
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', `report_${moment(this.reportMonth).format('MM/YYYY')}.xlsx`);
            document.body.appendChild(link);
            link.click();
          }).catch(error => {
          console.error(error)
        })
      },
    },
    filters: {
      formatDate(date) {
        return moment(date).format('MM/YYYY').toString();
      }
    },
    async mounted() {
      this.reportMonth = moment().toISOString();
      await this.fetchReport();
      let [ dsChiNhanhRes] = await Promise.all([
        this.$axios.get(`/chinhanh/danhsach`)
      ]);

      this.dsChiNhanh  = dsChiNhanhRes.data;
    }
  }
</script>

<style>

</style>
