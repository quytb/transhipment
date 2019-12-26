import Vue from "vue";
import App from "./App";
import router from "./router/index";
import Vuetify from "vuetify";
import axios from "axios";
import ElementUI from 'element-ui';

// CSS
import "vuetify/dist/vuetify.min.css";

import {Collapse, CollapseItem} from 'element-ui'
import VueTabs from 'vue-nav-tabs'

Vue.use(VueTabs);
Vue.use(Collapse);
Vue.use(CollapseItem);

// Configs
Vue.use(Vuetify);
Vue.use(require('vue-cookies'));
Vue.use(ElementUI);

// Cookie
import VueCookie from 'vue-cookies'
Vue.use(VueCookie);

import { Icon } from "leaflet"
delete Icon.Default.prototype._getIconUrl;

Icon.Default.mergeOptions({
  iconRetinaUrl: require("leaflet/dist/images/marker-icon-2x.png"),
  iconUrl: require("leaflet/dist/images/marker-icon.png"),
  shadowUrl: require("leaflet/dist/images/marker-shadow.png")
});
require('./bootstrap');
Vue.use({
  install(Vue) {
    Auth.setupVue(Vue, $cookies.get(Auth.COOKIE_ACCESS_TOKEN));
    Auth.refreshTokenInterceptor(Vue);
  }
});

import PaperDashboard from "./plugins/paperDashboard";
import "vue-notifyjs/themes/default.css";
import './assets/sass/paper-dashboard.scss'
import './assets/sass/element_variables.scss'
import './assets/sass/demo.scss'
import Auth from "./auth";
import GlobalComponents from "./plugins/globalComponents";

Vue.use(PaperDashboard);
Vue.use(GlobalComponents);

/* eslint-disable no-new */
new Vue({
  router,
  render: h => h(App)
}).$mount("#app");
