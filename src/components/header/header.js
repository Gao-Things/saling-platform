import { mapGetters, mapActions } from 'vuex';
export default {
    data() {
        return {
            selectedValue: '1', // 存储选择的值
            options: [
                { value: '1', label: 'Gold' },
                { value: '2', label: 'Silver' },
                { value: '0', label: 'All' }
            ],// 选项数据

            searchTerm: '',
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

    },
    methods:{
        ...mapActions(['logout']), // 导入 logout action

        handleSearchChange(value) {
            console.log('User is typing:', value);

            // 你可以在这里执行搜索逻辑，比如 debounce 搜索请求
            this.$emit('selectedValue', value);

        },
        handleSearchClear() {
            console.log('Search input cleared');
            // 清除操作后你可能想要执行的逻辑
        },


        // 登出并跳转到登录页面
        logoutAndRedirect() {
            this.logout(); // 调用登出 action
            this.$router.push('/'); // 跳转到登录页面
        },
        toLogin(){
          this.$router.push('/');
        },
        toUser(){
            console.log("aaaaaaaa")
        },
        collapse(){
            this.$emit('doCollapse')
        },
        handleSelectChange(value){
            console.log('Selected value is:', value);
            this.$emit('selectedValue', this.selectedValue);
        },
    }
}