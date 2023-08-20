<template>
    <div>
        <h1>Sign Up</h1>

        <img class="logo" src="../assets/logo.png" />

        <div class="register">
            <input type="text" v-model="name" placeholder="Enter Name" />
            <input type="text" v-model="email" placeholder="Enter Email" />
            <input type="password" v-model="password" placeholder="Enter password" />
            <button v-on:click="signUp">Sign Up</button>
            <p>
                <router-link to="/login">Login</router-link>
            </p>
        </div>
    </div>
</template>

<script>
import axios from 'axios';
export default {
    name: 'SignUp',
    data() {
        return {
            name: "",
            email: "",
            password: ""
        };
    },
    methods: {
         async signUp() {
            let result =  await axios.post("http://localhost:3000/users", {
                email: this.email,
                name: this.name,
                password: this.password
            });
            console.warn(result);
            if (result.status == 201) {
                localStorage.setItem("user-info", JSON.stringify(result.data));
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
.logo {
    width: 100px
}

.register input {
    height: 30px;
    width: 200px;
    padding-left: 20px;
    display: block;
    margin-bottom: 30px;
    margin-left: auto;
    margin-right: auto;
    border: 1px solid skyblue;
}

.register button {
    width: 230px;
    height: 30px;
    border: 1px solid skyblue;
    background: skyblue;
    color: white;
    cursor: pointer;
}
</style>