import VueRouter from 'vue-router'
import index from '../components/indexPage/index.vue'
import login from "../components/login/login.vue";
import adminPage from "../components/adminPage/admin.vue"


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

export default router