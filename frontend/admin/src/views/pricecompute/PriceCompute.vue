<template>
  <v-layout column>
    <!-- Danh sách phương thức tính giá cước -->
    <v-card class="mt-3 pa-0 elevation-1">
      <v-card-title class="pa-3">
        Phương thức tính giá cước trung chuyển cộng tác viên
        <v-spacer></v-spacer>
        <v-btn
          small
          class="green white--text ma-0 mt-3 elevation-1"
          @click="dialogForm={}; dialogFormType='create'; createDialog = true"
        >Thêm mới</v-btn>
      </v-card-title>
      <v-divider class="ma-0"></v-divider>
      <v-card-text>
        <v-data-table
          :headers="taskHeaders"
          :items="prices"
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
            <td>{{ props.item.name }}</td>
            <td>{{ mappingPriceCompute[props.item.type] }}</td>
            <td>
              <v-btn small icon class="gray" @click="edit(props.item)">
                <v-icon>edit</v-icon>
              </v-btn>
              <v-btn small icon class="gray" @click="delete(props.item)">
                <v-icon>delete</v-icon>
              </v-btn>
            </td>
          </template>
        </v-data-table>
      </v-card-text>
    </v-card>

    <!-- Thêm mới phương thức tính giá  -->
    <v-dialog v-model="createDialog" max-width="800">
      <v-card>
        <v-card-title class="pa-3">
          Tạo mới phương thức tính giá
          <v-spacer></v-spacer>
        </v-card-title>
        <v-divider class="ma-0"></v-divider>
        <v-card-text>
          <v-container grid-list-xl class="pa-0" fluid>
            <v-layout row wrap>
              <v-flex xs5>
                <v-text-field
                  label="Tên phương thức"
                  v-model="dialogForm.tenPhuongThuc"
                  tabindex="2"
                  required
                ></v-text-field>
              </v-flex>
              <v-flex xs12>
                <v-radio-group xs3 class="pa-0" row v-model="priceCompute">
                  <v-radio :key="1" label="Giá mở cửa" :value="1"></v-radio>
                </v-radio-group>
              </v-flex>
              <!-- <v-flex xs1></v-flex>
                <v-flex xs3>
                    <v-text-field
                    label="Từ 0 Km"
                    v-model="dialogForm.distanceFrom"
                    readonly
                    :disabled="[2].includes(priceCompute)"
                    ></v-text-field>
                </v-flex>
                <v-flex xs4>
                    <v-text-field
                    label="Đến Km"
                    type = "number"
                    v-model="dialogForm.distanceTo"
                    required
                    :disabled="[2].includes(priceCompute)"
                    ></v-text-field>
                </v-flex>
                <v-flex xs4>
                    <v-text-field
                    label="Giá cước/Km (VND)"
                    type = "number"
                    v-model="dialogForm.price"
                    tabindex="2"
                    required
                    :disabled="[2].includes(priceCompute)"
                    ></v-text-field>
              </v-flex>-->
              <div v-for="(priceStep,index) in priceSteps" :key="index">
                <v-layout row>
                  <v-flex xs1></v-flex>
                  <v-flex xs4>
                    <v-text-field
                      label="Từ Km"
                      type="number"
                      v-model="priceStep.distanceFrom"
                      :disabled="[2].includes(priceCompute)"
                      required
                    ></v-text-field>
                  </v-flex>
                  <v-flex xs4>
                    <v-text-field
                      label="Đến Km"
                      type="number"
                      v-model="priceStep.distanceTo"
                      :disabled="[2].includes(priceCompute)"
                      required
                    ></v-text-field>
                  </v-flex>
                  <v-flex xs3>
                    <v-text-field
                      label="Giá cước/Km (VND)"
                      type="number"
                      v-model="priceStep.price"
                      :disabled="[2].includes(priceCompute)"
                      required
                    ></v-text-field>
                  </v-flex>
                </v-layout>
              </div>
              <v-layout row>
                <v-flex xs1></v-flex>
                <v-btn
                  fab
                  dark
                  small
                  color="green"
                  @click="addPriceStep()"
                  :disabled="[2].includes(priceCompute)"
                >
                  <v-icon>add</v-icon>
                </v-btn>
                <v-btn
                  fab
                  dark
                  small
                  color="red"
                  @click="delPriceStep()"
                  :disabled="[2].includes(priceCompute)"
                >
                  <v-icon>delete</v-icon>
                </v-btn>
              </v-layout>

              <v-flex xs12>
                <v-radio-group xs3 class="pa-0" row v-model="priceCompute">
                  <v-radio :key="2" label="Giới hạn cự ly" :value="2"></v-radio>
                </v-radio-group>
              </v-flex>
              <v-flex xs1></v-flex>
              <v-flex xs3>
                <v-text-field
                  label="Số Km"
                  type="number"
                  v-model="dialogForm.distance"
                  :disabled="[1].includes(priceCompute)"
                  required
                ></v-text-field>
              </v-flex>
              <v-flex xs4>
                <v-text-field
                  label="Giá cước (VND)"
                  type="number"
                  v-model="dialogForm.price"
                  :disabled="[1].includes(priceCompute)"
                  required
                ></v-text-field>
              </v-flex>
              <br />
              <v-flex xs3>
                <v-text-field
                  label="Giá cước phụ trội (VND)"
                  type="number"
                  v-model="dialogForm.priceOver"
                  :disabled="[1].includes(priceCompute)"
                  required
                ></v-text-field>
              </v-flex>
              <v-flex xs1></v-flex>
            </v-layout>
          </v-container>
        </v-card-text>
        <v-divider class="ma-0"></v-divider>

        <v-card-actions>
          <v-spacer></v-spacer>

          <v-btn
            color="green darken-1 white--text"
            small
            @click="create()"
          >Thêm mới</v-btn>
          <v-btn color="red darken-1 white--text" small @click="createDialog = false">Đóng</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- Cập nhật phương thức tính giá  -->
    <v-dialog v-model="updateDialog" max-width="800">
      <v-card>
        <v-card-title class="pa-3">
          <v-layout row class="pa-0">
            <v-flex xs6 >Chỉnh sửa phương thức tính giá</v-flex>
            <v-spacer></v-spacer>
          </v-layout>
        </v-card-title>
        <v-divider class="ma-0"></v-divider>
        <v-card-text>
          <v-container grid-list-xl class="pa-0" fluid>
            <v-layout row wrap>
              <v-flex xs5>
                <v-text-field
                  label="Tên phương thức"
                  v-model="dialogUpdateForm.tenPhuongThuc"
                  tabindex="2"
                  required
                ></v-text-field>
              </v-flex>
              <v-flex xs12>
                <v-radio-group xs3 class="pa-0" row v-model="priceCompute">
                  <v-radio :key="1" label="Giá mở cửa" :value="1"></v-radio>
                </v-radio-group>
              </v-flex>
              <div v-for="(priceStep,index) in updatePriceSteps" :key="index">
                <v-layout row>
                  <v-flex xs1></v-flex>
                  <v-flex xs4>
                    <v-text-field
                      label="Từ Km"
                      type="number"
                      v-model="priceStep.distanceFrom"
                      :disabled="[2].includes(priceCompute)"
                      required
                    ></v-text-field>
                  </v-flex>
                  <v-flex xs4>
                    <v-text-field
                      label="Đến Km"
                      type="number"
                      v-model="priceStep.distanceTo"
                      :disabled="[2].includes(priceCompute)"
                      required
                    ></v-text-field>
                  </v-flex>
                  <v-flex xs3>
                    <v-text-field
                      label="Giá cước/Km (VND)"
                      type="number"
                      v-model="priceStep.price"
                      :disabled="[2].includes(priceCompute)"
                      required
                    ></v-text-field>
                  </v-flex>
                </v-layout>
              </div>
              <v-layout row>
                <v-flex xs1></v-flex>
                <v-btn
                  fab
                  dark
                  small
                  color="green"
                  @click="addUpdatePriceStep()"
                  :disabled="[2].includes(priceCompute)"
                >
                  <v-icon>add</v-icon>
                </v-btn>
                <v-btn
                  fab
                  dark
                  small
                  color="red"
                  @click="delUpdatePriceStep()"
                  :disabled="[2].includes(priceCompute)"
                >
                  <v-icon>delete</v-icon>
                </v-btn>
              </v-layout>

              <v-flex xs12>
                <v-radio-group xs3 class="pa-0" row v-model="priceCompute">
                  <v-radio :key="2" label="Giới hạn cự ly" :value="2"></v-radio>
                </v-radio-group>
              </v-flex>
              <v-flex xs1></v-flex>
              <v-flex xs3>
                <v-text-field
                  label="Số Km"
                  type="number"
                  v-model="dialogUpdateForm.distance"
                  :disabled="[1].includes(priceCompute)"
                  required
                ></v-text-field>
              </v-flex>
              <v-flex xs4>
                <v-text-field
                  label="Giá cước (VND)"
                  type="number"
                  v-model="dialogUpdateForm.price"
                  :disabled="[1].includes(priceCompute)"
                  required
                ></v-text-field>
              </v-flex>
              <br />
              <v-flex xs3>
                <v-text-field
                  label="Giá cước phụ trội (VND)"
                  type="number"
                  v-model="dialogUpdateForm.priceOver"
                  :disabled="[1].includes(priceCompute)"
                  required
                ></v-text-field>
              </v-flex>
              <v-flex xs1></v-flex>
            </v-layout>
          </v-container>
        </v-card-text>
        <v-divider class="ma-0"></v-divider>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
            color="green darken-1 white--text"
            small
            @click="update()"
          >
            Cập nhật
          </v-btn>
          <v-btn color="red darken-1 white--text" small @click="updateDialog = false">Đóng</v-btn>
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
      mappingPriceCompute: {
        1: "Giá mở cửa Vũng Tàu",
        2: "Giới hạn cự ly Sài Gòn"
      },
      priceCompute: 1,

      dialogForm: {},
      dialogUpdateForm: {},
      // Tasks
      taskHeaders: [
        { text: "STT", align: "center", sortable: false },
        { text: "Tên phương thức", align: "left", sortable: false },
        { text: "Loại phương thức ", align: "left", sortable: false },
        { text: "Thao tác", align: "left", sortable: false }
      ],
      prices: [],
      priceSteps: [
        {
          distanceFrom: 0,
          distanceTo: "",
          price: ""
        }
      ],

       updatePriceSteps: [
        {
          distanceFrom: "",
          distanceTo: "",
          price: ""
        }
      ],

      priceByDistanceDTO:{},

      tableLoading: false,
      rowPerPageItems: [10, 20, 50, 100, 500],
      pagination_data: {
        page: 0,
        rowsPerPage: 10
      },
      isWatchingPagination: false,

      // Message snackbar
      snackbar: {
        active: false,
        text: "default message",
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
    async fetchPrices() {
      try {
        let priceRes = await this.$axios.get(
          `/ctv-price`,
          Object.assign({
            page: this.pagination.page,
            size: this.pagination.rowsPerPage
          })
        );

        this.prices = priceRes.data;
        this.pagination.totalItems = priceRes.data.totalElement;
      } catch (err) {
        // TODO: Handle error
        console.log(err);
      }
    },

    async create() {
      try {
        let priceStepRes = await this.$axios.post(`/ctv-price/create-update`, {
          priceByStepDTOS    : this.priceSteps,
          name               : this.dialogForm.tenPhuongThuc,
          type               : this.priceCompute,
          priceByDistanceDTO :{ distance: this.dialogForm.distance, price: this.dialogForm.price, priceOver: this.dialogForm.priceOver}
        });

        this.createDialog = false;
        await this.fetchPrices();
        this.dialogForm = {};
        this.showMessage('green', 'Tạo mới phương thức tính giá cho đối tác thành công');
      } catch(err) {
        // TODO: Error handle
        this.createDialog = false;
        console.log(err);
      }
    },

    edit(pricePartner) {
      // this.dialogForm               = Object.assign({}, pricePartner);
      this.dialogUpdateForm.tenPhuongThuc   = pricePartner.name;
      this.dialogUpdateForm.priceId         = pricePartner.priceId;
      // this.dialogForm.type            = pricePartner.type;
      this.priceCompute               = pricePartner.type;
      if(pricePartner.type == 1 ){
        this.updatePriceSteps                 = pricePartner.priceByStepDTOS;
      } else {
        this.dialogUpdateForm.distance        = pricePartner.priceByDistanceDTO.distance;
        this.dialogUpdateForm.price           = pricePartner.priceByDistanceDTO.price;
        this.dialogUpdateForm.priceOver       = pricePartner.priceByDistanceDTO.priceOver;
        this.dialogUpdateForm.priceDistanceId = pricePartner.priceByDistanceDTO.priceDistanceId;
      }
      // this.dialogFormType             ='edit';
      this.updateDialog               = true;
    },

    async update() {
      try {
        this.tableLoading = true;
        let priceComputeRes = await this.$axios.post(`/ctv-price/create-update`, {
          priceByStepDTOS    : this.updatePriceSteps,
          name               : this.dialogUpdateForm.tenPhuongThuc,
          type               : this.priceCompute,
          priceId            : this.dialogUpdateForm.priceId,
          priceByDistanceDTO :{
                                distance: this.dialogUpdateForm.distance,
                                price: this.dialogUpdateForm.price,
                                priceOver: this.dialogUpdateForm.priceOver,
                                priceDistanceId: this.dialogUpdateForm.priceDistanceId
                              }
        });

        this.updateDialog = false;
        this.dialogUpdateForm = {};
        await this.fetchPrices();
        this.showMessage('green', 'Cập nhật phương thức tính giá cho đối tác thành công');
        this.tableLoading = false;
      } catch(err) {
        // TODO: Error handle
        this.updateDialog = false;
        this.tableLoading = false;
        this.snackbar.active = true;
        this.snackbar.text = `LỖI: ${err.response.data.message}`;
        console.log(err);
      }
    },

    async addPriceStep() {
      this.priceSteps.push({
        distanceFrom: "",
        distanceTo: "",
        price: ""
      });
    },
    async delPriceStep() {
      if (this.priceSteps.length > 1) {
        this.priceSteps.pop();
      }
    },

    async addUpdatePriceStep() {
      this.updatePriceSteps.push({
        distanceFrom: "",
        distanceTo: "",
        price: ""
      });
    },
    async delUpdatePriceStep() {
      if (this.updatePriceSteps.length > 1) {
          let priceStep = this.updatePriceSteps.pop();
      }
      await this.fetchPrices();
    },


    // async pushPriceSteps(){
    //     this.price.push({
    //         priceSteps: this.priceSteps
    //     });
    // },

    allowedStep: m => m % 5 === 0,
    showMessage(color, message) {
      this.snackbar.active = true;
      this.snackbar.color = color;
      this.snackbar.text = message;
    }
  },

  async mounted() {
    await this.fetchPrices();
    this.isWatchingPagination = true;
  },
  watch: {
    pagination: {
      async handler() {
        if (this.isWatchingPagination) await this.fetchPrices();
      },
      deep: true
    }
  }
};
</script>
<style>
</style>
