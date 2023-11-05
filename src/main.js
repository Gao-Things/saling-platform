import Vue from 'vue'
import App from './App.vue'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import './assets/global.css';
import axios from "axios";
import store from './store'; // 引入 Vuex store
import locale from 'element-ui/lib/locale/lang/en';
// 引入
import router from './router'
import VueRouter from 'vue-router'
import { Loading } from 'element-ui';
Vue.prototype.$axios = axios;

// Vue.prototype.$httpurl = 'http://localhost:8082'
Vue.prototype.$httpurl = 'http://capstone-loadbalancer-2005125113.us-east-1.elb.amazonaws.com'
Vue.prototype.$imageurl = 'https://capstone-file-store.s3.amazonaws.com/'

Vue.config.productionTip = false;

Vue.use(ElementUI,  { locale });
Vue.use(VueRouter) // router plugin
Vue.use(Loading.directive);

Vue.prototype.$loading = Loading.service;

new Vue({
  render: h => h(App),
  //注册
  router,
  store // 将 Vuex store 挂载到 Vue 实例上
}).$mount('#app')
