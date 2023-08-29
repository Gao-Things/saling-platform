import {mapGetters} from "vuex";

export default {
    name: "Aside.vue",
    data(){
        return {

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