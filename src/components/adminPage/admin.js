import Aside from "../aside/Aside.vue"
import Header from "../header/Header.vue"
import adminMain from "../Product/adminMain.vue"
export default {
    name: "Index",
    components: {Aside, Header, adminMain},
    data(){
        return {
            isCollapse:false,
            aside_width:'230px',
            icon:'el-icon-s-fold',
            exchangeValue:""
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
        },
        selectedValue(exchangeValue){
            this.exchangeValue = exchangeValue
        }
    }
};