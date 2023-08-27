<template>
  <el-menu background-color="black"
           text-color="#fff"
           active-text-color="#ffd04b"
           default-active="/Home"
           style="height: 100vh; background-color: black"
           :collapse="isCollapse"
           :collapse-transition="false"
            router>
    <el-menu-item index="/Home">
      <i class="el-icon-s-home"></i>
      <span slot="title">Main</span>
    </el-menu-item>

    <el-menu-item index="/fun">
      <i class="el-icon-s-home"></i>
      <span slot="title">fun</span>
    </el-menu-item>

    <el-menu-item index="/customer">
      <i class="el-icon-s-home"></i>
      <span slot="title">customer</span>
    </el-menu-item>
  </el-menu>
</template>

<script src="./aside.js"></script>

<style scoped>

</style>