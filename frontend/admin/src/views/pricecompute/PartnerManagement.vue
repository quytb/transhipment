<template>
  <v-layout column>
      <!-- Danh sách đối tác -->
    <v-card class="mt-3 pa-0 elevation-1">
      <v-card-title class="pa-3">Khai báo đối tác
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
          :items="partners"
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
            <td>{{ props.item.partnerName }}</td>
            <td>{{ props.item.discountRange }}</td>
            <td>{{ props.item.priceName }}</td>
          
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


    <!-- Create new partner -->
    <v-dialog
      v-model="createDialog"
      max-width="600"
    >
      <v-card>
        <v-card-title class="pa-3">
          <v-layout row class="pa-0" align-center>
            <v-flex xs6>{{ dialogFormType == 'create' ? 'Thêm mới đối tác' : 'Chỉnh sửa thông tin đối tác' }}</v-flex>
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
            <v-layout align-center>
              <v-flex  fill-height>
                <v-text-field
                  label="Tên đối tác"
                  v-model="dialogForm.partnerName"                  
                  required
                ></v-text-field>
                <v-text-field
                  label="Mức chiết khấu"
                  v-model="dialogForm.discountRange"
                  type = "number"                 
                  required
                ></v-text-field>
                <v-select
                  v-model="dialogForm.type"
                  :items="prices"
                  item-value="priceId"
                  item-text="name"
                  label="Loại phương thức giá"  
                ></v-select>
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
            Tạo mới
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
        0 : 'Không hoạt động',
        1 : 'Đang hoạt động'
      },
   
      dialogForm : {},
      dialogFormType : 'create',


      // Tasks
      taskHeaders: [
        { text: 'STT', align: 'center', sortable: false },
        { text: 'Tên đối tác', align: 'left', sortable: false },
        { text: 'Mức chiết khấu (%)', align: 'left', sortable: false },
        { text: 'Loại phương thức giá áp dụng', align: 'left', sortable: false },       
        { text: 'Thao tác', align: 'left', sortable: false, }        
      ],
      partners : [],
      prices : [],
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
      }
    }
  },
  computed : {
    pagination : {
      get() { return this.pagination_data; },
      set(value) { this.pagination_data = value }
    }
  },
 
  methods : {
    async fetchPartners(){
      try {
        let partnerRes = await this.$axios.post(`ctv-price/partner/list`, Object.assign({          
          "page": this.pagination.page,
          "size": this.pagination.rowsPerPage
        }));
        
        this.partners = partnerRes.data.content;
        this.pagination.totalItems = partnerRes.data.totalElement;
      } catch (err) {
        // TODO: Handle error
        console.log(err);
      }
    },
    allowedStep: m => m % 5 === 0,
    async create() {
      try {
        let partnerRes = await this.$axios.post(`/ctv-price/partner/create-update`, {          
          partnerName     : this.dialogForm.partnerName,
          discountRange   : this.dialogForm.discountRange,
          priceId         : this.dialogForm.type
        });

        this.createDialog = false;        
        await this.fetchPartners();
        this.showMessage('green', 'Tạo đối tác thành công');
      } catch(err) {
        // TODO: Error handle
        this.createDialog = false;
        console.log(err);
      }
    },

    edit(partner) {
      this.dialogForm               = Object.assign({}, partner);
      this.dialogForm.id            = partner.id;
      this.dialogForm.partnerId     = partner.partnerId;
      this.dialogForm.partnerName   = partner.partnerName;
      this.dialogForm.discountRange = partner.discountRange;
      this.dialogForm.type          = partner.priceId;
      this.dialogFormType           ='edit';
      this.createDialog             = true;
    },

    async update() {
      try {
        this.tableLoading = true;
        let shiftRes = await this.$axios.post(`/ctv-price/partner/create-update`, {
          id              : this.dialogForm.id,
          partnerId       : this.dialogForm.partnerId,
          partnerName     : this.dialogForm.partnerName,
          discountRange   : this.dialogForm.discountRange,
          priceId         : this.dialogForm.type,
        });

        this.createDialog = false;
        await this.fetchPartners();
        this.showMessage('green', 'Cập nhật đối tác thành công');
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

    
    
    showMessage(color, message) {
      this.snackbar.active  = true;
      this.snackbar.color   = color;
      this.snackbar.text    = message;
    }
  },

  async mounted() {
    let [priceRes] = await Promise.all([
      this.$axios.get(`/ctv-price`)
    ]);

    this.prices = priceRes.data;
    await this.fetchPartners();
    this.isWatchingPagination = true;
  },
  watch : {
    pagination : {
      async handler () {
        if(this.isWatchingPagination)
          await this.fetchPartners()
      },
      deep: true
    }
  }
}
</script>
<style>
</style>
