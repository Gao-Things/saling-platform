export default {
    data() {
        return {
            loginForm: {
                email: '',
                password: ''
            }
        };
    },
    methods: {
        login() {
            // 在这里处理登录逻辑
            this.$router.push('/admin');
            console.log('登录');
        }
    }
};