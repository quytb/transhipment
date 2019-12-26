import {Role} from "./role";

export default [
  {
    name: 'Điều hành',
    icon: 'ti-package',
    permissions: [Role.P10000001],
    children: [
      {
        name: 'Trung chuyển đón',
        path: '/trung-chuyen-don',
        icon: 'ti-control-forward',
        permissions: [Role.P10000001],
      },
      {
        name: 'Trung chuyển trả',
        path: '/trung-chuyen-tra',
        icon: 'ti-control-backward',
        permissions: [Role.P10000001],
      },
      {
        name: 'Quản lý lệnh',
        path: '/quan-ly-lenh',
        icon: 'ti-panel',
        permissions: [Role.P10000001],
      },
      {
        name: 'Bản đồ',
        path: '/maps',
        icon: 'ti-map',
        permissions: [Role.P10000001],
      }
    ]
  },
  {
    name: 'Quản lý',
    icon: 'ti-package',
    permissions: [Role.P20000001],
    children: [
      {
        name: 'Quản lý ca',
        path: '/quan-ly-ca',
        icon: 'ti-time',
        permissions: [Role.P20000001],
      },
      {
        name: 'Điều độ tháng',
        path: '/phan-lich',
        icon: 'ti-calendar',
        permissions: [Role.P20000001],
      },
      {
        name: 'Điều độ ngày',
        path: '/cham-cong',
        icon: 'ti-check-box',
        permissions: [Role.P20000001],
      },
      {
        name: 'Vùng điều hành',
        path: '/vung-dieu-hanh',
        icon: 'ti-view-grid',
        permissions: [Role.P20000001],
      },
      {
        name: 'Vùng hoạt động',
        path: '/vung-hoat-dong',
        icon: 'ti-view-grid',
        permissions: [Role.P20000001],
      },
      {
        name: 'Quản lý đối tác',
        path: '/partner',
        icon: 'ti-user',
        permissions: [Role.P20000001],
      },
      {
        name: 'Tính giá cước',
        path: '/price',
        icon: 'ti-money',
        permissions: [Role.P20000001],
      }
    ]
  },
  {
    name: 'Báo cáo',
    icon: 'ti-package',
    permissions: [Role.P30000001],
    children: [
      {
        name: 'Báo cáo chấm công',
        path: '/bao-cao-thang',
        icon: 'ti-clipboard',
        permissions: [Role.P30000001],
      },
      {
        name: 'Báo cáo tổng hợp',
        path: '/bao-cao-lenh',
        icon: 'ti-bar-chart',
        permissions: [Role.P30000001],
      },
      {
        name: 'Giám sát vé',
        path: '/monitor',
        icon: 'ti-desktop',
        permissions: [Role.P30000001],
      }
    ]
  }
  ,
  {
    name: 'Role',
    icon: 'ti-package',
    permissions: [Role.P90000001],
    children: [
      {
        name: 'Role Permission',
        path: '/role-permission',
        icon: 'ti-view-grid',
        permissions: [Role.P90000001],
      }
    ]
  },
  {
    name: 'ĐH Hub-Hub',
    icon: 'ti-package',
    permissions: [Role.P40000001],
    children: [
      {
        name: 'Trung chuyển đón Hub',
        path: '/tc-hub',
        icon: 'ti-plus',
        permissions: [Role.P40000001],
      },
      {
        name: 'Lệnh hub',
        path: '/lenh-tc-hub',
        icon: 'ti-plus',
        permissions: [Role.P40000001],
      }
    ]
  }
]
