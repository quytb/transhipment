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

            <v-flex xs9>
              <!-- filter -->
              <v-autocomplete
                v-model="selectedDriver"
                :items="[{id : null, name: 'Tất cả', ma_nhan_vien: ''}, ...users]"
                item-value="id"
                item-text="name"
                label="Lọc nhân viên"
                @change="filterUser()"
              >
                <template v-slot:item="props">
                  <span>
                    {{props.item.name}}<span v-if="props.item.id"> - MÃ NHÂN VIÊN: {{props.item.ma_nhan_vien}}</span>
                  </span>
                </template>
                <template v-slot:selection="props">
                  <span>
                    {{props.item.name}}<span v-if="props.item.id"> - MÃ NHÂN VIÊN: {{props.item.ma_nhan_vien}}</span>
                  </span>
                </template>
              </v-autocomplete>
            </v-flex>

            <v-divider class="ma-0"></v-divider>

            <v-flex xs3 class="text-xs-center">
              <v-btn class="green white--text ma-3" small @click="load()">Xem</v-btn>
            </v-flex>

            <v-flex xs12 class="pa-0">
              <v-data-table
                :headers="userTableHeaders"
                :items="userTableData"
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
                        'min-width': header.text === 'Tài xế' ? '200px !important' : '',
                        'text-align': header.text === 'Tài xế' ? 'left !important' : ''
                      }"
                    >
                    </th>
                  </tr>
                </template>
                <template v-slot:headers="props">
                  <tr>
                    <th
                      v-for="header in props.headers"
                      :key="header.text"
                      v-html="header.text"
                      class="text-xs-left"
                      :style="{
                        'min-width': header.text === 'Tài xế' ? '200px !important' : '',
                        'text-align': header.text === 'Tài xế' ? 'left !important' : ''
                      }"
                    >
                    </th>
                  </tr>
                </template>
                <template v-slot:items="props">
                  <td class="text-xs-left">{{ props.item.name }}</td>
                  <td class="text-xs-left">{{ props.item.maNhanvien }}</td>
                  <td class="text-xs-left">
                    <v-btn icon @click="editUserRow(props.item)">
                      <v-icon>edit</v-icon>
                    </v-btn>
                  </td>

                </template>

              </v-data-table>

              <!-- Dialog -->
              <v-dialog v-model="editDialog" fullscreen hide-overlay transition="dialog-bottom-transition">
                <v-card v-if="editDialog">
                  <v-toolbar dark color="primary">
                    <v-toolbar-title class="white--text">Phân quyền cho nhân viên: {{dialogForm.userName}} - {{dialogForm.maNhanvien}}</v-toolbar-title>
                    <v-spacer></v-spacer>
                    <v-btn class="red white--text" small @click="closeDialog()">Đóng</v-btn>
                  </v-toolbar>

                  <v-layout column class="pa-3">

                    <!-- Permission List -->
                    <v-card class="mt-4 pa-0 elevation-1">
                      <v-card-title class="pa-3">Danh sách quyền hạn</v-card-title>

                      <v-divider class="ma-0"></v-divider>

                      <!-- TODO -->
                      <v-card-text class="pa-3">
                        <v-container
                          grid-list-xl
                          text-xs-center
                          class="pa-0"
                          fluid>

                          <v-layout row wrap>

                            <v-flex xs1>
                              <v-checkbox v-for="p in permissionListMaster" :value="p.id"
                                          v-model="permissionListRequest" hide-details :label="p.name"></v-checkbox>
                            </v-flex>

                          </v-layout>

                        </v-container>

                      </v-card-text>

                      <v-card-text>
                        <v-btn small class="green white--text mt-3 ml-0" @click="saveUserPermission()">Lưu</v-btn>
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

  </v-layout>
</template>

<script>
    import moment from 'moment';

    export default {
        data() {
            return {

                users: [],
                permissionListMaster: [],
                permissionListRequest: [],

                // Variables
                selectedDriver: null,

                // Tasks
                userTableHeaders: [
                    {text: 'Tên nhân viên', align: 'left', sortable: false, width: "200"},
                    {text: 'Mã nhân viên', align: 'left', sortable: false, width: "50"},
                    {text: 'Thêm/Sửa', align: 'center', sortable: false}
                ],

                userTableHeaderContents: [],
                userTableDataTmp: [],
                userTableData: [],
                userRawData: [],
                rowPerPageItems: [10, 20, 30, 50, 100, 200, 500],
                pagination: {
                    page: 1,
                    rowsPerPage: 10
                },

                // Dialog
                editDialog: false,
                editingRow: {},
                dialogForm: {
                    userId: null,
                    userName: "",
                    maNhanvien: ""
                },

                // Message snackbar
                snackbar: {
                    active: false,
                    text: '',
                    color: 'red'
                },

                dialogSnackbar: {
                    active: false,
                    text: '',
                    color: 'red'
                },
                processing: false,
                confirmDialog: false
            }
        },
        methods: {
            saveUserPermission() {
                let permissionDataUpdate = [];

                this.permissionListRequest.forEach(item => {
                   permissionDataUpdate.push({id: item});
                });

                this.$axios.post(`/admin/users/${this.dialogForm.userId}/permissions`, permissionDataUpdate);

                this.closeDialog();
            },


            editRow(rowIndex) {
                this.editingRow = rowIndex;
            },

            load() {
                this.permissionListRequest = [];
                this.tableLoading = true;
                this.selectedDriver = null;
                this.fetchUserTableData();
                this.tableLoading = false;
            },

            async fetchUserTableData() {
                this.userTableData = [];
                this.users.forEach(user => {
                    let row = {userId: user.id, maNhanvien: user.ma_nhan_vien, name: user.name};
                    this.userTableData.push(row);
                });

                this.userTableDataTmp = this.userTableData;
            },

            filterUser() {
                if (this.selectedDriver) {
                    this.userTableData = this.userTableDataTmp.filter(cd => cd.userId === this.selectedDriver);
                } else {
                    this.userTableData = this.userTableDataTmp;
                }
            },

            // Dialog functions
            async editUserRow(row) {
                // Reset form
                this.permissionListRequest = [];
                this.dialogForm.userId = row.userId;
                this.dialogForm.userName = row.name;
                this.dialogForm.maNhanvien = row.maNhanvien;

                this.editingRow = -1;

                let userPermissionsRes = await this.$axios.get(`/admin/users/${this.dialogForm.userId}/permissions`);

                let userPermissions = userPermissionsRes.data;

                for (let i = 0; i < userPermissions.length; i++) {
                    let userPermission = userPermissions[i];
                    this.permissionListRequest.push(userPermission.id);
                }

                this.editDialog = true;
            },

            closeDialog() {
                this.editDialog = false;
                this.load();
            }
        },
        async mounted() {
            moment.locale('vi');
            let [permissionRes, userRes] = await Promise.all([
                this.$axios.get(`/admin/permissions`),
                this.$axios.get(`/admin/users`)
            ]);
            this.permissionListMaster = permissionRes.data;
            this.users = userRes.data;

            await this.load();
        },
    }
</script>


<style>
</style>
