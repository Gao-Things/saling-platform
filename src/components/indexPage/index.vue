<template>
  <el-container style="height: 100%; border: 1px solid #000000; margin-left: -1px;">
    <el-container style="height: 100%">
      <el-header style="text-align: right; font-size: 12px; width: 100%;height: 100%; border-bottom: rgba(169,169,169,0.35) 2px solid">
          <Header @doCollapse = "doCollapse" @selectedValue="selectedValue"  :icon = "icon"></Header>
      </el-header>
      <el-main style="height: 100%; width: 100%">
          <Main :exchangeValue="exchangeValue"></Main>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import Header from "../header/Header.vue"
import Main from "./Main.vue";
export default {
  name: "Index",
  components: { Header, Main},
  data(){
    return {
      isCollapse:false,
      aside_width:'200px',
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
      console.log("11111111111"+ exchangeValue)
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
.el-main{
  padding: 5px;
}
.el-aside {
  color: #333;
}
</style>