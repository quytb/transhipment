<template>
  <v-layout column>
    <!-- Quản lý cộng tác viên -->
    <v-card class="pa-0 elevation-1">
      <v-card-text class="px-3 py-0">
        <v-container grid-list-xl class="pa-0" fluid>
          <v-layout row wrap>
            <v-flex xs4>
              <v-text-field label="Mã vùng" v-model="searchForm.codeVung" tabindex="1" required></v-text-field>
            </v-flex>
            <v-flex xs3>
              <v-btn small class="green white--text ma-3" @click="search()">Tìm kiếm</v-btn>
            </v-flex>
            <!-- Tạo mới vùng Btn -->
            <v-flex>
              <v-btn
                small
                class="green white--text ma-3"
                style="float: right"
                @click="dialogForm={}; dialogFormType='create'; createDialog = true"
              >Tạo vùng hoạt động</v-btn>
            </v-flex>
          </v-layout>
        </v-container>
      </v-card-text>
    </v-card>

    <!-- Danh sách vùng trung chuyển -->
    <v-card class="mt-3 pa-0 elevation-1">
      <v-card-title class="pa-3">Danh sách vùng</v-card-title>
      <v-divider class="ma-0"></v-divider>
      <v-card-text>
        <v-data-table
          :headers="taskHeaders"
          :items="areas"
          :rows-per-page-items="rowPerPageItems"
          :pagination.sync="pagination"
          :total-items="pagination.totalItems"
          :loading="tableLoading"
          no-data-text="Không có dữ liệu"
          rows-per-page-text="Số bản ghi"
          class="elevation-1"
          w="100%"
        >
          <template v-slot:items="props">
            <td class="text-xs-center">{{ props.index + 1 }}</td>
            <td>{{ props.item.tenCtv }}</td>
            <td>{{ props.item.codeVung }}</td>
            <td
              style="text-align:center"
            >({{ props.item.coordinateLat }} , {{ props.item.coordinateLong }})</td>
            <td style="text-align:center">{{ props.item.xeBks }}</td>

            <td @click="confirmChangeStatus(props.item)">
              <div
                :class="{
                  'px-2 yellow lighten-4 black--text' : props.item.status == 0,
                  'px-2 green lighten-4 black--text' : props.item.status == 1
                }"
              >{{ mappingStatuses[props.item.status] }}</div>
            </td>
            <td>{{ props.item.note}}</td>
            <td>{{ props.item.createdDate | datetime}}</td>
            <td>{{ props.item.creatorName}}</td>

            <td class="text-xs-center">
              <v-btn small icon class="gray" @click="edit(props.item)">
                <v-icon>edit</v-icon>
              </v-btn>
            </td>
          </template>
        </v-data-table>
      </v-card-text>
    </v-card>
    <!-- Create collaborator-area -->
    <v-dialog v-model="createDialog" max-width="800">
      <v-card>
        <v-card-title class="pa-3">
          <v-layout row class="pa-0">
            <v-flex xs12 style=" font-size:20px">Tạo vùng hoạt động cho cộng tác viên</v-flex>            
          </v-layout>
        </v-card-title>
        <v-divider class="ma-0"></v-divider>
        <v-card-text>
          <v-container grid-list-xl class="pa-0" fluid>
            <v-layout row wrap>
              <v-flex xs12>
                <v-select
                  v-model="dialogForm.ctvId"
                  :items="dsCtv"
                  item-text="name"
                  item-value="id"
                  label="Cộng tác viên"
                  tabindex="2"
                  required
                ></v-select>
                <v-select
                  v-model="dialogForm.vungId"
                  :items="dsCodeVung"
                  item-text="vtcCode"
                  item-value="vtcId"
                  label="Mã vùng"
                  tabindex="2"
                  required
                ></v-select>
                <v-text-field id="pac-input" label="Địa chỉ cộng tác viên" tabindex="2" required></v-text-field>
                <v-select
                  v-model="dialogForm.xeId"
                  :items="dsBks"
                  item-text="xeBks"
                  item-value="xeId"
                  label="Biển kiểm soát"
                  tabindex="2"
                  required
                ></v-select>
                <v-textarea
                  v-model="dialogForm.tcVttNote"
                  tabindex="5"
                  cols="100"
                  rows="3"
                  label="Ghi chú"
                  required
                ></v-textarea>
                <v-radio-group class="mt-3" row v-model="dialogForm.status">
                  <label>Trạng thái:</label>
                  <v-radio
                    v-for="r in radioOpions"
                    :key="r.value"
                    :label="r.label"
                    :value="r.value"
                  ></v-radio>
                </v-radio-group>
              </v-flex>
            </v-layout>
          </v-container>
        </v-card-text>

        <v-divider class="ma-0"></v-divider>
        <v-card-actions style="float:center">
          <v-spacer></v-spacer>
          <v-btn
            color="green darken-1 white--text"
            small           
            @click="create()"
          >Tạo mới</v-btn>     

          <v-btn color="red darken-1 white--text" small @click="createDialog = false">Đóng</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- Update collaborator-area -->
    <v-dialog v-model="updateDialog" max-width="800">
      <v-card>
        <v-card-title class="pa-3">
          <v-layout row class="pa-0">
            <v-flex xs12 style=" font-size:20px">Cập nhật vùng hoạt động cho cộng tác viên</v-flex>            
          </v-layout>
        </v-card-title>
        <v-divider class="ma-0"></v-divider>
        <v-card-text>
          <v-container grid-list-xl class="pa-0" fluid>
            <v-layout row wrap>
              <v-flex xs12>
                <v-select
                  v-model="dialogFormUpdate.ctvId"
                  :items="dsCtv"
                  item-text="name"
                  item-value="id"
                  label="Cộng tác viên"
                  tabindex="2"
                  required
                ></v-select>
                <v-select
                  v-model="dialogFormUpdate.vtcId"
                  :items="dsCodeVung"
                  item-text="vtcCode"
                  item-value="vtcId"
                  label="Mã vùng"
                  tabindex="2"
                  required
                ></v-select>
                <v-text-field id="pac-input-update" label="Địa chỉ cộng tác viên" tabindex="2" required></v-text-field>
                <v-select
                  v-model="dialogFormUpdate.xeId"
                  :items="dsBks"
                  item-text="xeBks"
                  item-value="xeId"
                  label="Biển kiểm soát"
                  tabindex="2"
                  required
                ></v-select>
                <v-textarea
                  v-model="dialogFormUpdate.tcVttNote"
                  tabindex="5"
                  cols="100"
                  rows="3"
                  label="Ghi chú"
                  required
                ></v-textarea>
                <v-radio-group class="mt-3" row v-model="dialogFormUpdate.status">
                  <label>Trạng thái:</label>
                  <v-radio
                    v-for="r in radioOpions"
                    :key="r.value"
                    :label="r.label"
                    :value="r.value"
                  ></v-radio>
                </v-radio-group>
              </v-flex>
            </v-layout>
          </v-container>
        </v-card-text>

        <v-divider class="ma-0"></v-divider>
        <v-card-actions style="float:center">
          <v-spacer></v-spacer>          
          <v-btn
            color="green darken-1 white--text"
            small            
            @click="update()"
          >Cập nhật</v-btn>

          <v-btn color="red darken-1 white--text" small @click="updateDialog = false">Đóng</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- Confirm change status area transport dialog -->
    <v-dialog v-model="confirmStatusDialog" max-width="500" v-if="updatingArea">
      <v-card>
        <v-card-title class="pa-3">
          <v-layout row class="pa-0">
            <v-flex
              xs12
            >{{ `Cập nhật trạng thái vùng trung chuyển ${this.updatingArea.codeVung} thành "${this.updatingArea.status == 1 ? 'Không hoạt động' : 'Đang hoạt động'}"?` }}</v-flex>
            <v-spacer></v-spacer>
          </v-layout>
        </v-card-title>
        <v-divider class="ma-0"></v-divider>

        <v-card-actions>
          <v-spacer></v-spacer>

          <v-btn
            color="green darken-1 white--text"
            small
            @click="confirmStatusDialog = false; changeStatus()"
          >Cập nhật</v-btn>

          <v-btn color="red darken-1 white--text" small @click="confirmStatusDialog = false">Đóng</v-btn>
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
      <v-btn dark flat @click="snackbar.active = false">Đóng</v-btn>
    </v-snackbar>
  </v-layout>
</template>

<script>
export default {
  data() {
    return {
      createDialog: false,
      updateDialog: false,
      mappingStatuses: {
        0: "Không hoạt động",
        1: "Đang hoạt động"
      },
      criterials: {
        codeVung: ""
      },

      radioOpions: [
        { value: 0, label: "Không hoạt động" },
        { value: 1, label: "Hoạt động" }
      ],

      // Form
      searchForm: {
        codeVung: ""
      },
      dialogForm: {},
      dialogFormUpdate: {},
      dialogFormType: "create",
      dsCtv: [],
      dsCodeVung: [],
      dsBks: [],

      // Tasks
      taskHeaders: [
        { text: "STT", align: "center", sortable: false },
        { text: "Cộng tác viên", align: "center", sortable: false },
        { text: "Mã vùng", align: "center", sortable: false },
        { text: "Vị trí cộng tác viên", align: "center", sortable: false },
        { text: "Xe cộng tác viên", align: "center", sortable: false },
        { text: "Trạng thái", align: "center", sortable: false },
        { text: "Ghi chú", align: "center", sortable: false },
        { text: "Ngày tạo", align: "center", sortable: false },
        { text: "Người tạo", align: "left", sortable: false },
        { text: "Edit", align: "center", sortable: false }
      ],
      areas: [],
      tableLoading: false,
      rowPerPageItems: [10, 20, 50, 100, 500],
      pagination_data: {
        page: 0,
        rowsPerPage: 10
      },
      isWatchingPagination: false,

      coordinate: null,
      coordinateUpdate: null,

      // Update status
      updatingArea: null,
      confirmStatusDialog: false,
      confirmDeleteDialog: false,

      // Message snackbar
      snackbar: {
        active: false,
        text: "default message",
        color: "red"
      },
      file: null
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
  filters: {
    time: value => {
      let [hour, minute] = value.toString().split(".");
      minute = minute ? (minute.length == 2 ? minute : minute + "0") : "00";
      return `${hour}:${minute}`;
    },
    datetime(value) {
      let [date, time] = value.toString().split("T");
      return date;
    },
    content(value) {
      let [point1, point2, point3] = value.toString().split(",");
      // let content =value.toString().substr(0,30) + " ...";
      return `${point1} ${point2} ${point3}` + "...";
    }
  },

  methods: {
    async fetchAreas() {
      try {
        console.log(
          Object.assign(
            {
              page: this.pagination.page,
              size: this.pagination.rowsPerPage
            },
            this.criterials
          )
        );
        let areaRes = await this.$axios.get(`/vtc-ctv/list`, {
          params: Object.assign(
            {
              page: this.pagination.page,
              size: this.pagination.rowsPerPage
            },
            this.criterials
          )
        });

        this.areas = areaRes.data.content;
        this.pagination.totalItems = areaRes.data.totalElement;
      } catch (err) {
        // TODO: Handle error
        console.log(err);
      }
    },

    async create() {
      try {
        let areaRes = await this.$axios.post(`/vtc-ctv/create-update`, {
          ctvId: this.dialogForm.ctvId,
          note: this.dialogForm.tcVttNote,
          status: this.dialogForm.status,
          vtcId: this.dialogForm.vungId,
          xeId: this.dialogForm.xeId,
          coordinateLong: this.coordinate.lng,
          coordinateLat: this.coordinate.lat
        });
        this.createDialog = false;
        this.showMessage(
          "green",
          "Tạo mới vùng trung chuyển - cộng tác viên thành công"
        );
        await this.fetchAreas();
      } catch (err) {
        // TODO: Error handle
        this.createDialog = false;
        console.log(err);
      }
    },

    async edit(area) {
      // this.dialogFormUpdate = Object.assign({}, area);
      this.dialogFormUpdate.vtcCtvId = area.id;
      this.dialogFormUpdate.vtcId = area.vungId;
      this.dialogFormUpdate.ctvId = area.ctvId;
      this.dialogFormUpdate.tcVttNote = area.note;
      this.dialogFormUpdate.xeId = area.xeId;
      this.dialogFormUpdate.coordinateLong = area.coordinateLong;
      this.dialogFormUpdate.coordinateLat = area.coordinateLat;
      this.dialogFormUpdate.status = area.status;
      this.updateDialog = true;      
    },

    async update() {        
      try {
        this.tableLoading = true;        
        if(this.coordinateUpdate !=null){
          this.dialogFormUpdate.coordinateLong= this.coordinateUpdate.lngUpdate;
          this.dialogFormUpdate.coordinateLat= this.coordinateUpdate.latUpdate;
        } 
        let areaRes = await this.$axios.post(`/vtc-ctv/create-update`, {
          vtcCtvId: this.dialogFormUpdate.vtcCtvId,
          ctvId: this.dialogFormUpdate.ctvId,
          note: this.dialogFormUpdate.tcVttNote,
          status: this.dialogFormUpdate.status,
          vtcId: this.dialogFormUpdate.vtcId,
          xeId: this.dialogFormUpdate.xeId,
          coordinateLong: this.dialogFormUpdate.coordinateLong,
          coordinateLat: this.dialogFormUpdate.coordinateLat
        });
        
        await this.fetchAreas();
        this.updateDialog = false;
        this.tableLoading = false;
        this.showMessage(
          "green",
          "Cập nhật vùng trung chuyển - cộng tác viên thành công"
        );
      } catch (err) {
        // TODO: Error handle
        this.updateDialog = false;
        this.tableLoading = false;
        this.snackbar.active = true;
        this.snackbar.text = `LỖI: ${err.response.data.message}`;
        console.log(err);
      }
    },

    confirmChangeStatus(area) {
      this.updatingArea = area;
      this.confirmStatusDialog = true;
    },

    async changeStatus() {
      this.tableLoading = true;
      await this.$axios.post(`/vtc-ctv/create-update/`, {
        vtcCtvId: this.updatingArea.id,
        ctvId: this.updatingArea.ctvId,
        note: this.updatingArea.note,
        vtcId: this.updatingArea.vungId,
        xeId: this.updatingArea.xeId,
        coordinateLong: this.updatingArea.coordinateLong,
        coordinateLat: this.updatingArea.coordinateLat,
        status: this.updatingArea.status == 1 ? 0 : 1
      });
      await this.fetchAreas();
      this.tableLoading = false;
      this.showMessage(
        "green",
        "Cập nhật trạng thái vùng trung chuyển - cộng tác viên thành công"
      );
    },

    async search() {
      try {
        this.criterials = { ctvName: this.searchForm.codeVung };

        this.fetchAreas();
      } catch (err) {
        console.log(err);
      }
    },
    showMessage(color, message) {
      this.snackbar.active = true;
      this.snackbar.color = color;
      this.snackbar.text = message;
    },
    async initialize() {
      var address = document.getElementById("pac-input");
      document.getElementById("pac-input").placeholder = "";
      var autocomplete = new google.maps.places.Autocomplete(address);
      
      let seft = this;
      autocomplete.setTypes(["geocode"]);
      google.maps.event.addListener(autocomplete, "place_changed", function() {
        var place = autocomplete.getPlace();
        if (!place.geometry) {
          return;
        }
        seft.coordinate = {
          lng: place.geometry.location.lng(),
          lat: place.geometry.location.lat()
        };
      });

      var addressUpdate = document.getElementById("pac-input-update");
      document.getElementById("pac-input-update").placeholder = "";
      var autocompleteUpdate = new google.maps.places.Autocomplete(addressUpdate);
      autocompleteUpdate.setTypes(["geocode"]);
      google.maps.event.addListener(autocompleteUpdate, "place_changed", function() {
        var placeUpdate = autocompleteUpdate.getPlace();
        if (!placeUpdate.geometry) {
          return;
        }

        seft.coordinateUpdate = {
          lngUpdate: placeUpdate.geometry.location.lng(),
          latUpdate: placeUpdate.geometry.location.lat()
        };
      });
    }
  },

  async mounted() {
    let [dsCtvRes, dsCodeVungRes, dsBksRes] = await Promise.all([
      this.$axios.get("/vtc-ctv/ctv-list"),
      this.$axios.get(`/vtc-ctv/vtc-list`),
      this.$axios.get(`/vtc-ctv/xe-list`)
    ]);

    this.dsCtv = dsCtvRes.data;
    this.dsCodeVung = dsCodeVungRes.data;
    this.dsBks = dsBksRes.data;
    await this.fetchAreas();
    await this.initialize();
    this.isWatchingPagination = true;
  },
  watch: {
    pagination: {
      async handler() {
        if (this.isWatchingPagination) await this.fetchAreas();
      },
      deep: true
    }
  }
};
</script>
<style>
</style>
