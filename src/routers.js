import Vue from 'vue';
import VueRouter from 'vue-router';
import Home from './components/Home.vue';
import SignUp from './components/SignUp.vue';
import Login from './components/Login.vue';

Vue.use(VueRouter);

const routes = [
  { path: '/', component: Home, name: 'HomePage' },
  { path: '/sign-up', component: SignUp, name: 'SignUp' },
  { path: '/login', component: Login, name: 'LoginPage' },
  // ...其他路由
];

const router = new VueRouter({
  mode:'history',
  routes
});

export default router;