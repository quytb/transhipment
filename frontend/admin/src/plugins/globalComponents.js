import { FormGroupInput, Card, Button } from "../components/index";
import DropDown from '../components/Dropdown.vue'
/**
 * You can register global components here and use them as a plugin in your main Vue instance
 */

const GlobalComponents = {
  install(Vue) {
    Vue.component("fg-input", FormGroupInput);
    Vue.component(DropDown.name, DropDown);
    Vue.component("card", Card);
    Vue.component("p-button", Button);
  }
};

export default GlobalComponents;
