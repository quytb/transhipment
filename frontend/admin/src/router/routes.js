import DashboardLayout from "@/layout/dashboard/DashboardLayout.vue";
// GeneralViews
import NotFound from "@/pages/NotFoundPage.vue";

// Admin pages
import Dashboard from "@/pages/Dashboard.vue";
import UserProfile from "@/pages/UserProfile.vue";
import Notifications from "@/pages/Notifications.vue";
import Icons from "@/pages/Icons.vue";
import Maps from "@/pages/Maps.vue";
import Typography from "@/pages/Typography.vue";
import TableList from "@/pages/TableList.vue";

// Project pages
import ShiftPage from "@/views/shift/Shift.vue";
import CalendarPage from "@/views/calendar/Calendar.vue";
import TimeKeeping from "@/views/timekeeping/TimeKeeping.vue";
import MonthlyReport from "@/views/report/MonthlyReport.vue";
import Pickup from "@/views/transit/Pickup.vue";
import Dropoff from "@/views/transit/Dropoff.vue";
import Command from "@/views/command/Command.vue";
import Login from "../views/login/Login";
import Auth from "../auth";
import CommandReport from "../views/report/CommandReport";
import AreaTransport from "@/views/area/AreaTransport.vue";
import Collaborator from "@/views/collaborator/Collaborator.vue";
import MonitorTicket from "@/views/monitorticket/MonitorTicket.vue";
import Test from "../views/test/Test";
import BanDo from "../views/map/BanDo";
import PartnerManagement from "@/views/pricecompute/PartnerManagement.vue";
import PriceCompute from "@/views/pricecompute/PriceCompute.vue";
import TestMap from "../views/test/TestMap";
import RolePermission from "@/views/role-permission/RolePermission";
import {Role} from "../role";
import PickupHub from "../views/transit-hub/PickupHub";
import CommandHub from "../views/command/CommandHub";

const routes = [
  {
    path: "/",
    component: DashboardLayout,
    redirect: "/quan-ly-ca",
    children: [
      // Project pages
      {
        path: "quan-ly-ca",
        name: "Quản lý ca",
        component: ShiftPage,
        meta: {permissions: []},
      },
      {
        path: "phan-lich",
        name: "Phân lịch",
        component: CalendarPage,
        meta: {permissions: []},
      },
      {
        path: "cham-cong",
        name: "Chấm công",
        component: TimeKeeping,
        meta: {permissions: []},
      },
      {
        path: "bao-cao-thang",
        name: "Báo cáo chấm công",
        component: MonthlyReport,
        meta: {permissions: []}
      },
      {
        path: "trung-chuyen-don",
        name: "Trung chuyển đón",
        component: Pickup,
        meta: {permissions: []},
        props:true,
      },
      {
        path: "bao-cao-lenh",
        name: "Báo cáo tổng hợp",
        component: CommandReport,
        meta: {permissions: []},
      },
      {
        path: "trung-chuyen-tra",
        name: "Trung chuyển trả",
        component: Dropoff,
        meta: {permissions: []},
      },
      {
        path: "/tc-hub",
        name: "Trung chuyển đón Hub - Hub",
        component: PickupHub,
        meta: {permissions: []},
      },
      {
        path: "/lenh-tc-hub",
        name: "Lệnh Hub - Hub",
        component: CommandHub,
        meta: {permissions: []},
      },
      {
        path: "quan-ly-lenh",
        name: "Quản lý lệnh",
        component: Command,
        meta: {permissions: []},
      },
      {
        path: "/vung-dieu-hanh",
        name: "Vùng điều hành",
        component: AreaTransport,
        meta: {permissions: []},
      },
      {
        path: "/vung-hoat-dong",
        name: "Vùng hoạt động",
        component: Collaborator,
        meta: {permissions: []},
      },
      {
        path: "/role-permission",
        name: "Role Permission",
        component: RolePermission,
        meta: {permissions: [Role.P90000001]},
      },
      {
        path: "/monitor",
        name: "Giám sát vé trung chuyển",
        component: MonitorTicket,
        meta: {permissions: []},
      },

      {
        path: "/partner",
        name: "Quản lý đối tác",
        component: PartnerManagement,
        meta: {permissions: []},
      },
      {
        path: "/price",
        name: "Tính giá cước",
        component: PriceCompute,
        meta: {permissions: []},
      },


      // Default pages
      {
        path: "dashboard",
        name: "dashboard",
        component: Dashboard,
        meta: {permissions: []},
      },
      {
        path: "stats",
        name: "stats",
        component: UserProfile,
        meta: {permissions: []},
      },
      {
        path: "notifications",
        name: "notifications",
        component: Notifications,
        meta: {permissions: []},
      },
      {
        path: "icons",
        name: "icons",
        component: Icons,
        meta: {permissions: []},
      },
      {
        path: "maps",
        name: "maps",
        component: BanDo,
        meta: {permissions: []},
      },
      {
        path: "typography",
        name: "typography",
        component: Typography,
        meta: {permissions: []},
      },
      {
        path: "table-list",
        name: "table-list",
        component: TableList,
        meta: {permissions: []},
      },
      {
        path: "test",
        name: "test",
        component: Test,
        meta: {permissions: []},
      },
      {
        path: "test1",
        name: "test1",
        component: TestMap,
        meta: {permissions: []},
      }
    ]
  },
  {path: "*", component: NotFound}
];

export default routes;
