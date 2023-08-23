import VueRouter from 'vue-router'
import index from '../components/indexPage'


const router = new VueRouter({
    mode: 'history',    // Routing mode, this mode will not display the pound sign # in the address
    routes: [
        {
            path: '/',
            name: 'index',
            component: index
        }
    ]
})

export default router