import VueRouter from 'vue-router'
import index from '../components/indexPage/index.vue'
import login from "../components/login/login.vue";
import adminPage from "../components/adminPage/admin.vue"
import store from '../store'; // 导入你的 Vuex store

const router = new VueRouter({
    mode: 'history',    // Routing mode, this mode will not display the pound sign # in the address
    routes: [
        {
            path: '/',
            name: 'index',
            component: index
        },
        {
            path: '/login',
            name: 'login',
            component: login
        },
        {
            path: '/admin',
            name: 'admin',
            component: adminPage
        }
    ]
})

router.beforeEach((to, from, next) => {
    if (to.path === '/admin') {
        const isLoggedIn = store.getters.isLoggedIn; // 从 Vuex store 获取登录状态
        if (!isLoggedIn) {
            next('/login'); // 跳转到登录页
        } else {
            next(); // 继续访问 /admin
        }
    } else {
        next(); // 其他页面不需要验证
    }
});

export default router