import Vue from 'vue'
import App from './App.vue'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import './assets/global.css';
import axios from "axios";
import store from './store'; // 引入 Vuex store
// 引入
import router from './router'
import VueRouter from 'vue-router'
Vue.prototype.$axios = axios;
Vue.prototype.$httpurl = 'http://localhost:8082'

Vue.config.productionTip = false;
Vue.use(ElementUI);
Vue.use(VueRouter) // router plugin

new Vue({
  render: h => h(App),
  //注册
  router,
  store // 将 Vuex store 挂载到 Vue 实例上
}).$mount('#app')
