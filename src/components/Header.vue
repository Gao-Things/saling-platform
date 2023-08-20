<template>
    <div class="nav">
        <a href="#">Home</a>
        <a href="#">Home</a>
        <a href="#">Home</a>
        <a href="#">MyProfile</a>
        <a v-on:click="logout" href="#">Logout</a>
    </div>
</template>

<script>
export default {
    name: 'HeaderPage',
    methods:{
        logout(){

            localStorage.clear();
            this.$router.push({ name: 'LoginPage' });
        }
    }
};
</script>

<style>
.nav{
    background-color: #333;
    overflow: hidden;
}
.nav a{
    float: left;
    color: aliceblue;
    padding: 14px 14px;
    text-align: center;
    font-size: 18px;
    margin-right: 4px;
}
.nav a:hover {
    background-color: #555; /* Change background color on hover */
    color: white; /* Change text color on hover */
}
</style>