<template>
  <v-layout column>
    <!-- Quản lý vùng điều hành -->
    <v-card class="pa-0 elevation-1">
      <v-card-text class="px-3 py-0">
        <v-container grid-list-xl class="pa-0" fluid>
          <v-layout row wrap>
            <v-flex xs4>
              <v-text-field label="Tên vùng" v-model="searchForm.tenVung" tabindex="1"
                            required></v-text-field>
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
              >Tạo vùng mới
              </v-btn>
            </v-flex>
          </v-layout>
        </v-container>
      </v-card-text>
    </v-card>

    <!-- Danh sách vùng điều hành -->
    <v-card class="mt-3 pa-0 elevation-1">
      <v-card-title class="pa-3">Danh sách vùng điều hành</v-card-title>
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
            <td>{{ props.item.tcVttName }}</td>
            <td>{{ props.item.tcVttCode }}</td>
            <td>{{ props.item.tcVttContent| content}}</td>
            <td @click="confirmChangeStatus(props.item)">
              <div
                :class="{
                  'px-2 yellow lighten-4 black--text' : props.item.status == 0,
                  'px-2 green lighten-4 black--text' : props.item.status == 1
                }"
              >{{ mappingStatuses[props.item.status] }}
              </div>
            </td>
            <td>{{ props.item.createdDate| datetime }}</td>
            <td>{{ props.item.createdBy }}</td>
            <td class="text-xs-center">
              <v-btn small icon class="gray" @click="edit(props.item)">
                <v-icon>edit</v-icon>
              </v-btn>
            </td>
          </template>
        </v-data-table>
      </v-card-text>
    </v-card>
    <!-- Create area -->
    <v-dialog v-model="createDialog" max-width="800">
      <v-card>
        <v-card-title class="pa-3">
          <v-layout row class="pa-0">
            <v-flex xs6>{{ dialogFormType == 'create' ? 'Tạo mới vùng điều hành' : 'Sửa vùng điều hành' }}
            </v-flex>
            <v-spacer></v-spacer>
          </v-layout>
        </v-card-title>
        <v-divider class="ma-0"></v-divider>

        <v-card-text>
          <v-container grid-list-xl class="pa-0" fluid>
            <v-layout row wrap>
              <v-flex xs6>
                <v-text-field
                  label="Tên vùng"
                  v-model="dialogForm.tcVttName"
                  tabindex="2"
                  required
                ></v-text-field>

                <v-text-field
                  label="Mã vùng"
                  v-model="dialogForm.tcVttCode"
                  tabindex="2"
                  required
                ></v-text-field>
                <v-text-field
                  type="number"
                  label="Vận tốc trung bình"
                  v-model="dialogForm.tcVAverageSpeed"
                  tabindex="2"
                  required
                ></v-text-field>
                <v-textarea
                  v-model="dialogForm.tcVttNode"
                  tabindex="5"
                  cols="100"
                  rows="2"
                  label="Ghi chú"
                  required
                ></v-textarea>
              </v-flex>
              <v-flex xs6>
                <br>
                <label>
                  Màu vùng:<input type="color" name="aaa" id="bbb" label="Chọn màu vùng"
                                  v-model="dialogForm.color">
                </label>

                <br/> <br/>
                <label>
                  Dữ liệu vùng: <input
                  type="file"
                  id="file"
                  ref="file"
                  v-on:change="handleFileUpload()"/>
                </label>
                <br/>
                <v-layout row>
                  <v-flex xs12 class="px-0 py-0 text-xs-left">
                    <v-radio-group xs3 class="pa-0" row v-model="dialogForm.status">
                      <label>Trạng thái</label>
                      <v-radio class="ma-0"
                               v-for="r in radioOpions"
                               :key="r.value"
                               :label="r.label"
                               :value="r.value"
                      ></v-radio>
                    </v-radio-group>
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
          >Tạo mới
          </v-btn>

          <v-btn
            color="green darken-1 white--text"
            small
            v-if="dialogFormType == 'edit'"
            @click="update()"
          >Cập nhật
          </v-btn>

          <v-btn color="red darken-1 white--text" small @click="createDialog = false">Đóng</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <!-- Confirm change status area transport dialog -->
    <v-dialog
      v-model="confirmStatusDialog"
      max-width="500"
      v-if="updatingArea"
    >
      <v-card>
        <v-card-title class="pa-3">
          <v-layout row class="pa-0">
            <v-flex xs12>{{ `Cập nhật trạng thái vùng điều hành ${this.updatingArea.tcVttName} thành
              "${this.updatingArea.status == 1 ? 'Không hoạt động' : 'Đang hoạt động'}"?` }}
            </v-flex>
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
          >
            Cập nhật
          </v-btn>

          <v-btn
            color="red darken-1 white--text"
            small
            @click="confirmStatusDialog = false"
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
      <v-btn dark flat @click="snackbar.active = false">Đóng</v-btn>
    </v-snackbar>
  </v-layout>
</template>

<script>

    export default {
        data() {
            return {
                createDialog: false,
                mappingStatuses: {
                    0: "Không hoạt động",
                    1: "Đang hoạt động"
                },
                criterials: {
                    tcVttName: ""
                },

                radioOpions: [
                    {value: 0, label: "Không hoạt động"},
                    {value: 1, label: "Hoạt động"}
                ],

                // Form
                searchForm: {
                    tcVttName: ""
                },
                dialogForm: {},
                dialogFormType: "create",


                // Tasks
                taskHeaders: [
                    {text: "STT", align: "center", sortable: false},
                    {text: "Tên vùng", align: "center", sortable: false},
                    {text: "Mã vùng", align: "center", sortable: false},
                    {text: "Dữ liệu vùng", align: "center", sortable: false},
                    {text: "Trạng thái", align: "center", sortable: false},
                    {text: "Ngày tạo", align: "center", sortable: false},
                    {text: "Người tạo", align: "left", sortable: false},
                    {text: "Edit", align: "center", sortable: false}
                ],
                areas: [],
                tableLoading: false,
                rowPerPageItems: [10, 20, 50, 100, 500],
                pagination_data: {
                    page: 0,
                    rowsPerPage: 10
                },
                isWatchingPagination: false,

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
            },
        },
        filters: {
            time: value => {
                let [hour, minute] = value.toString().split(".");
                minute = minute ? (minute.length == 2 ? minute : minute + "0") : "00";
                return `${hour}:${minute}`;
            },
            datetime(value) {
                let [date, time] = value.toString().split(" ");
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
                    console.log(Object.assign(
                        {
                            page: this.pagination.page,
                            size: this.pagination.rowsPerPage
                        }, this.criterials));
                    let areaRes = await this.$axios.get(
                        `/vungtrungchuyen/list`, {
                            params:

                                Object.assign(
                                    {
                                        page: this.pagination.page,
                                        size: this.pagination.rowsPerPage
                                    }, this.criterials)

                        });

                    this.areas = areaRes.data;
                    this.pagination.totalItems = areaRes.data.totalElement;
                } catch (err) {
                    // TODO: Handle error
                    console.log(err);
                }
            },

            async create() {
                try {
                    let formData = new FormData();
                    formData.append("file", this.file);
                    formData.append("ten", this.dialogForm.tcVttName);
                    formData.append("code", this.dialogForm.tcVttCode);
                    formData.append("status", this.dialogForm.status ? 1 : 0);
                    formData.append("averageSpeed", this.dialogForm.tcVAverageSpeed);
                    formData.append("color", this.dialogForm.color);

                    let areaRes = await this.$axios
                        .post(`/vungtrungchuyen/add`,
                            formData
                            , {
                                headers: {'Content-Type': 'multipart/mixed;boundary=gc0p4Jq0M2Yt08jU534c0p'}
                            }).then(function () {
                            console.log("Success");
                        }).catch(function () {
                            console.log("Failure");
                        });
                    this.createDialog = false;
                    this.showMessage('green', 'Tạo vùng điều hành thành công');
                    this.$refs.file.file[0] = null;
                    await this.fetchAreas();

                } catch (err) {
                    // TODO: Error handle
                    this.createDialog = false;
                    console.log(err);
                }
            },
            handleFileUpload() {
                this.file = this.$refs.file.files[0];
            },

            edit(area) {
                this.dialogForm = Object.assign({}, area);
                this.dialogForm.tcVttId = area.tcVttId;
                this.dialogFormType = "edit";
                this.createDialog = true;
            },

            async update() {
                try {
                    this.tableLoading = true;
                    let formData = new FormData();
                    formData.append("id", this.dialogForm.tcVttId);
                    formData.append("file", this.file ? this.file : '');
                    formData.append("ten", this.dialogForm.tcVttName);
                    formData.append("code", this.dialogForm.tcVttCode);
                    formData.append("status", this.dialogForm.status ? 1 : 0);
                    formData.append("averageSpeed", this.dialogForm.tcVAverageSpeed);
                    let areaRes = await this.$axios.post(`/vungtrungchuyen/update`,
                        formData,
                        {
                            headers: {'Content-Type': 'multipart/mixed;boundary=gc0p4Jq0M2Yt08jU534c0p'}
                        }).then(function () {
                    }).catch(function () {
                        console.log("Failure");
                    });

                    this.createDialog = false;
                    this.showMessage('green', 'Cập nhật vùng điều hành thành công');
                    this.$refs.file.file[0] = null;
                    await this.fetchAreas();
                    this.tableLoading = false;

                } catch (err) {
                    // TODO: Error handle
                    this.createDialog = false;
                    this.tableLoading = false;
                    this.snackbar.active = true;
                    this.snackbar.text = `LỖI: ${err.response.data.message}`;
                    console.log(err);
                }
            },

            confirmDelete(tcVttId) {
                this.tcVttId = tcVttId;
                // this.tcVttName = tcVttName;
                this.confirmDeleteDialog = true;
            },

            async deleteArea() {
                try {
                    await this.$axios.post(`/vungtrungchuyen/delete/${this.tcVttId}`);
                    this.confirmDeleteDialog = false;
                    this.tcVttId = null;
                } catch (err) {
                    console.log(err);
                }
            },

            // parseTime(value) {
            //   let [hour, minute] = value.toString().split(".");
            //   minute = minute ? minute.length == 2 ? minute : minute + '0' : '00';
            //   return `${hour}:${minute}`;
            // },

            confirmChangeStatus(area) {
                this.updatingArea = area;
                this.confirmStatusDialog = true;
            },

            async changeStatus() {
                this.tableLoading = true;
                let formData = new FormData();
                formData.append("id", this.updatingArea.tcVttId);
                formData.append("file", this.file ? this.file : '');
                formData.append("ten", this.updatingArea.tcVttName);
                formData.append("code", this.updatingArea.tcVttCode);
                formData.append("status", this.updatingArea.status == 1 ? 0 : 1);
                formData.append("averageSpeed", this.updatingArea.tcVAverageSpeed);
                await this.$axios.post(`/vungtrungchuyen/update`,
                    formData,
                    {
                        headers: {'Content-Type': 'multipart/mixed;boundary=gc0p4Jq0M2Yt08jU534c0p'}
                    }).then(function () {
                }).catch(function () {
                    console.log("Failure");
                });

                await this.fetchAreas();
                this.tableLoading = false;
                this.showMessage('green', 'Cập nhật trạng thái vùng điều hành thành công');
            },

            async search() {
                try {
                    this.criterials = {tenVung: this.searchForm.tenVung};

                    this.fetchAreas();
                } catch (err) {
                    console.log(err);
                }
            },
            showMessage(color, message) {
                this.snackbar.active = true;
                this.snackbar.color = color;
                this.snackbar.text = message;
            }
        },


        async mounted() {
            await this.fetchAreas();
            this.isWatchingPagination = false;
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
