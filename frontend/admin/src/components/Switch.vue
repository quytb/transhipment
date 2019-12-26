<template>
  <div class="bootstrap-switch bootstrap-switch-wrapper bootstrap-switch-animate" :class="switchClass">
    <div class="bootstrap-switch-container" @click="triggerToggle()">
      <span class="bootstrap-switch-handle-on " :class="`bootstrap-switch-${type}`">
        <slot name="on">
            {{onText}}
        </slot>
      </span>
      <span class="bootstrap-switch-label"></span>
      <span class="bootstrap-switch-handle-off bootstrap-switch-default">
        <slot name="off">
            {{offText}}
        </slot>
      </span>
    </div>
  </div>
</template>
<script>
  import swal from 'sweetalert2'
  export default{
    name: 'p-switch',
    props: {
      label: String,
      value: [Array, Boolean],
      disabled: [Boolean, String],
      onText: String,
      offText: String,
      type: {
        type: String,
        default: ''
      },
      change: { type: Function }
    },
    computed: {
      switchClass () {
        let base = 'bootstrap-switch-';
        let state = this.model ? 'on' : 'off';
        return base + state
      },
      model: {
        get () {
          return this.value
        },
        set (value) {
          this.$emit('input', value)
        }
      }
    },
    methods: {
      triggerToggle () {
          let self = this;
        swal({
            title: 'Bạn có chắc chắn không?',
            text: !self.model ? 'Thay đổi trạng thái thành mở sẽ cho phép tạo nhiều lệnh cùng lúc cho từng lái xe cụ thể!'
                : 'Thay đổi trạng thái thành đóng sẽ không cho phép tạo nhiều lệnh cùng lúc cho từng lái xe cụ thể!',
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Thay đổi!',
            cancelButtonText: 'Không',
            confirmButtonClass: 'btn btn-success btn-fill',
            cancelButtonClass: 'btn btn-danger btn-fill',
            buttonsStyling: false
        }).then(function () {
            swal({
                title: 'Đã thay đổi!',
                text: self.model ? 'Đổi trạng thái thành mở!' : 'Đổi trạng thái thành đóng!',
                type: 'success',
                confirmButtonClass: 'btn btn-success btn-fill',
                buttonsStyling: false
            });
            self.model = !self.model;
            self.change();
        }, function (dismiss) {
            // dismiss can be 'overlay', 'cancel', 'close', 'esc', 'timer'
            if (dismiss === 'cancel') {
                swal({
                    title: 'Hủy',
                    text: 'Trạng thái không thay đổi',
                    type: 'error',
                    confirmButtonClass: 'btn btn-info btn-fill',
                    buttonsStyling: false
                })
            }
        });
      }
    }
  }
</script>
<style>
</style>
