import { mapGetters, mapActions } from 'vuex';
export default {
    data() {
        return {
            selectedValue: 'USD', // 存储选择的值
            options: [] // 选项数据
        };
    },
    name: "Header.vue",
    props:{
        icon:String
    },
    computed: {
        ...mapGetters(['isLoggedIn']),
        ...mapGetters(['getRole'])
    },
    mounted() {
        this.loadCurrencyList(); // 在组件挂载后获取选项数据
    },
    methods:{
        ...mapActions(['logout']), // 导入 logout action
        // 登出并跳转到登录页面
        logoutAndRedirect() {
            this.logout(); // 调用登出 action
            this.$router.push('/login'); // 跳转到登录页面
        },
        toLogin(){
          this.$router.push('/login');
        },
        toUser(){
            console.log("aaaaaaaa")
        },
        collapse(){
            this.$emit('doCollapse')
        },
        handleSelectChange(){
            this.$emit('selectedValue', this.selectedValue);
        },
        loadCurrencyList(){

            this.$axios.get(this.$httpurl + '/public/product/currencyList').then(res => res.data).then(res => {

                if (res.code === 200) {
                    console.log(res.data);
                    this.options = res.data;

                } else {
                    alert("failed to get the data")
                }
            })

        },
    }
}