    import {mapGetters} from "vuex";

    export default {
        name: "Aside.vue",
        data(){
            return {
                activeIndex: '/admin', // 默认活动索引
            }
        },
        watch: {
            $route(newRoute) {
                // 当路由变化时，更新 activeIndex
                console.log("scxsacsacsavasvasv")
                console.log(newRoute)
                this.activeIndex = newRoute.path;
            }
        },
        props:{
            isCollapse:Boolean
        },
        computed: {
            ...mapGetters(['getRole']),
            links() {
                return this.$store.getters.sidebarLinks;
            }
        },
    }