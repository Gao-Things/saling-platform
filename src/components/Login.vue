<template>
    <div>
        <h1>Login Page</h1>

        <img class="logo" src="../assets/logo.png" />

        <div class="login">
            <input type="text" v-model="email" placeholder="Enter Email" />
            <input type="password" v-model="password" placeholder="Enter password" />
            <button v-on:click="login">Login</button>
            <p>
                <router-link to="/sign-up">Sign up</router-link>
            </p>
        </div>
    </div>
</template>

<script>
import axios from 'axios';
export default {
    name: 'LoginPage',
    data() {
        return {
            email: "",
            password: ""
        };
    },
    methods:{
       async login(){
            let result= await axios.get(
                `http://localhost:3000/users`, {
            params: {
            email: this.email,
            password: this.password
          }
        }
        )
            console.warn(result);
            if (result.status == 200 && result.data.length==1) {
                localStorage.setItem("user-info", JSON.stringify(result.data[0]));
                this.$router.push({ name: 'HomePage' });
            }
        }
    },
    mounted(){
        let user=localStorage.getItem('user-info');
        if(user){
            this.$router.push({ name: 'HomePage' });
        }
    }
};
</script>

<style>
.login input {
    height: 30px;
    width: 200px;
    padding-left: 20px;
    display: block;
    margin-bottom: 30px;
    margin-left: auto;
    margin-right: auto;
    border: 1px solid skyblue;
}

.login button {
    width: 230px;
    height: 30px;
    border: 1px solid skyblue;
    background: skyblue;
    color: white;
    cursor: pointer;
}
</style>