import { mapActions } from 'vuex';

export default {
    data() {
        return {
            loginForm: {
                email: '',
                password: '',
                userRole: 2
            }
        };
    },
    methods: {
        ...mapActions(['login']),
        loginPress() {
            let loginForm = this.loginForm;
            this.$axios.post(this.$httpurl + '/public/login', loginForm)
                .then(res => res.data)
                .then(res => {
                    console.log(res);
                    if (res.code === 200) {
                        this.login({ jwttoken: res.jwttoken, role: res.data.role }); // 使用映射的 action 方法
                        this.$router.push('/admin');
                        console.log('登录');
                    } else {
                        alert("failed to get the data");
                    }
                });
        }
    }
};
