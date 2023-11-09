<template>
  <el-container style="height: 100%; border: 1px solid #000000;">
    <el-aside :width="aside_width" style="background-color: #008604;margin-left: -1px ">
      <!--            将从vuex中拿出的当前用户的sidebar传给Aside组件-->
      <Aside :isCollapse="isCollapse"></Aside>
    </el-aside>

    <el-container style="height: 100%;">
<!--      <el-header-->
<!--          style="text-align: right; font-size: 12px; height: 100%; border-bottom: rgb(255,255,255) 2px solid">-->
<!--        <Header @doCollapse="doCollapse" @selectedValue="selectedValue" :icon="icon"></Header>-->
<!--      </el-header>-->
      <el-main style="height: 100%;  margin-left: -1px">
        <data-main :exchangeValue="exchangeValue"></data-main>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>

import Aside from "../aside/Aside.vue"
import Header from "../header/Header.vue"
import dataMain from "./dataMain.vue"
export default {
  components: { Aside, Header, dataMain},
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
</script>

<style>
.el-header {
  /*background-color: #B3C0D1;*/
  color: #333;
  line-height: 60px;
}

.el-main {
  padding: 5px;
}

.el-aside {
  color: #333;
}

.el-main.no-padding-main {
  padding: 0; /* Remove padding */
}
</style>
