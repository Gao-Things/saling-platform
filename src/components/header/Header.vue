<template>
  <div style="display: flex; line-height: 60px; background-color: black">
    <div style="margin-top: 8px; cursor: pointer">
      <i :class="icon" style="font-size: 20px" @click = "collapse"></i>
    </div>
      <div style="flex: 1; text-align: center;">
            <img src="../../assets/img_1.png" width="100px">
      </div>
    <div style="flex: 1; text-align: center; font-size: 34px; color: #eeeeee">
      <span>CS COMPANY </span>
    </div>
  <el-dropdown>
    <span>王小虎</span>
    <i class="el-icon-arrow-down" style="margin-left: 5px"></i>
    <el-dropdown-menu slot="dropdown">
      <el-dropdown-item @click.native="toUser">personal central</el-dropdown-item>
      <el-dropdown-item>login out </el-dropdown-item>

    </el-dropdown-menu>
  </el-dropdown>

  </div>
</template>

<script src="./header.js">
</script>

<style scoped>

</style>