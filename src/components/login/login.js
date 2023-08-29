import { mapActions } from 'vuex';

export default {
    data() {
        return {
            loginForm: {
                email: '',
                password: '',
                userRole: 3
            },
            loginRole:""
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
                        if(res.data.role === 1){
                            this.loginRole = "USER"
                        }
                        if (res.data.role === 2){
                            this.loginRole = "ADMIN"
                            this.login({ jwttoken: res.jwttoken, role:this.loginRole }); // 使用映射的 action 方法
                        }
                        if (res.data.role === 3){
                            this.loginRole = "SUPERADMIN"
                            this.login({ jwttoken: res.jwttoken, role:this.loginRole }); // 使用映射的 action 方法
                        }
                        this.$router.push('/admin');
                        console.log('登录');
                    } else {
                        alert("failed to get the data");
                    }
                });
        }
    }
};
