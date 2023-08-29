import Aside from "../aside/Aside.vue"
import Header from "../header/Header.vue"
import settingMain from "./settingMain.vue"
export default {
    components: {Aside, Header, settingMain},
    data(){
        return {
            isCollapse:false,
            aside_width:'230px',
            icon:'el-icon-s-fold',
        }
    },
    methods:{
        doCollapse(){
            console.log(11111111111111)
            this.isCollapse = !this.isCollapse
            if(!this.isCollapse){
                this.aside_width = '200px'
                this.icon = 'el-icon-s-fold'
            }else {
                this.aside_width = '64px'
                this.icon = 'el-icon-s-unfold'
            }
        }
    }
};