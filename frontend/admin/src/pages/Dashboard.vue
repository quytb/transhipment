<template>
  <v-layout column>

    <!-- Quản lý ca trực -->
    <v-card class="pa-0 elevation-1">
      <v-card-title class="pa-3">Quản lý ca trực</v-card-title>
      <v-divider class="ma-0"></v-divider>
        
      <v-card-text class="pa-3">
        <v-container 
          grid-list-xl
          text-xs-center
          class="pa-0"
          fluid
        >
          <v-layout row wrap>
            <v-flex xs6>
              <v-text-field
                label="Tên ca"
                tabindex="1"
                required
              ></v-text-field>

              <v-text-field
                label="Mã ca"
                tabindex="1"
                required
              ></v-text-field>
            </v-flex>

            <v-flex xs6>
              <v-select
                :items="timeSelections"
                label="Giờ bắt đầu"
                tabindex="3"
              ></v-select>

              <v-select
                :items="timeSelections"
                label="Giờ kết thúc"
                tabindex="4"
              ></v-select>
            </v-flex>

            <v-flex xs6>
              <v-textarea
                name="input-7-4"
                label="Ghi chú"
              ></v-textarea>
            </v-flex>

            <v-flex xs6>
              <v-layout column class="pa-3">
                <v-layout row wrap justify-space-between>
                  <v-flex xs2 class="px-0 py-3 text-xs-left" style="font-size: 16px; line-height: 28px; color: rgba(0,0,0,.54);">
                    Trạng thái
                  </v-flex>
                  <v-flex xs10>
                    <v-checkbox
                      label="Hoạt động"
                      class="ma-0"
                      required
                    ></v-checkbox>
                  </v-flex>
                </v-layout>

                <v-flex class="text-xs-left pa-0">
                  <v-btn small class="ml-0">Tạo ca</v-btn>
                  <v-btn small>Tìm kiếm</v-btn>
                </v-flex>
              </v-layout>
            </v-flex>

            
          </v-layout>
        </v-container>
      </v-card-text>
    </v-card>

    <!-- Danh sách ca làm việc -->
    <v-card class="mt-4 pa-0 elevation-1">
      <v-card-title class="pa-3">Danh sách ca làm việc</v-card-title>
      <v-divider class="ma-0"></v-divider>

      <v-card-text>
        <v-data-table
          :headers="taskHeaders"
          :items="tasks"
          :rows-per-page-items="rowPerPageItems"
          :pagination.sync="pagination"
          class="elevation-1"
        >
          <template v-slot:items="props">
            <td class="text-xs-center">{{ props.index }}</td>
            <td>{{ props.item.task_name }}</td>
            <td>{{ props.item.task_code }}</td>
            <td>{{ props.item.start_time }}</td>
            <td>{{ props.item.end_time }}</td>
            <td>{{ props.item.status }}</td>
            <td>{{ props.item.note }}</td>
            <td class="text-xs-right">
              <v-btn small outline class="gray pa-0">
                <v-icon>edit</v-icon>
              </v-btn>
            </td>
          </template>
        </v-data-table>
      </v-card-text>
    </v-card>
  </v-layout>
</template>

<script>
export default {
  data() {
    return {
      timeSelections : [
        '00:00 am',
        '00:30 am',
        '01:00 am',
        '01:30 am',
        '02:00 am',
        '02:30 am',
        '03:00 am',
        '03:30 am',
        '04:00 am',
        '04:30 am',
        '05:00 am',
        '05:30 am',
        '06:00 am',
        '06:30 am',
        '07:00 am',
        '07:30 am',
        '08:00 am',
        '08:30 am',
        '09:00 am',
        '09:30 am',
        '10:00 am',
        '10:30 am',
        '11:00 am',
        '11:30 am',
        '12:00 am',
        '00:30 pm',
        '01:00 pm',
      ],

      // Tasks
      taskHeaders: [
        { text: 'STT', align: 'center', sortable: false },
        { text: 'Tên ca', align: 'left', sortable: false },
        { text: 'Mã ca', align: 'left', sortable: false },
        { text: 'Giờ bắt đầu', align: 'left', sortable: false },
        { text: 'Giờ kết thúc', align: 'left', sortable: false },
        { text: 'Trạng thái', align: 'left', sortable: false },
        { text: 'Ghi chú', align: 'left', sortable: false },
        { text: '', align: 'left', sortable: false },
      ],
      tasks : [
        {
          task_name     : 'Ca sáng',
          task_code     : 'HS07LC',
          start_time    : '08:00 am',
          end_time      : '11:00 am',
          status        : 'Đang hoạt động',
          note          : 'Giờ làm việc mùa đông'
        },
        {
          task_name     : 'Ca chiều',
          task_code     : 'HL07PC',
          start_time    : '04:00 pm',
          end_time      : '07:00 pm',
          status        : 'Đang hoạt động',
          note          : 'Giờ làm việc mùa đông'
        },
        {
          task_name     : 'Ca sáng',
          task_code     : 'HS07LR',
          start_time    : '08:00 am',
          end_time      : '11:00 am',
          status        : 'Không hoạt động',
          note          : 'Giờ làm việc mùa đông'
        }
      ],
      rowPerPageItems : [10],
      pagination      : {
        page        : 1,
        rowsPerPage : 10
      }
    }
  }
}
</script>


<style>
</style>
