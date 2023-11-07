<template>
    <div class="header-container">
        <div class="header-item" v-if="isLoggedIn">
            <i :class="icon" style="font-size: 20px" @click="collapse"></i>
        </div>
        <div class="header-item">
            <img src="../../assets/img_1.png" width="100px">
        </div>
        <div class="header-item" style="text-align: center">
            <span class="title">Metal Trading</span>
        </div>
      <el-input
          class="header-item custom-search-input"
          style="width: 400px; margin-left: 30px"
          placeholder="Enter search term"
          v-model="searchTerm"
          @input="handleSearchChange"
          @clear="handleSearchClear"
          clearable>
      </el-input>

      <div class="header-item" style="padding: 10px; margin-top: 20px">
            <img src="../../assets/search.png" width="30px">
        </div>

        <template v-if="isLoggedIn"> <!-- 根据登录状态进行条件渲染 -->
            <el-dropdown class="header-item" style="margin-left: 15%">
                <span >{{getRole}}</span>
                <i class="el-icon-arrow-down" style="margin-left: 5px"></i>
                <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item @click.native="toUser">personal central</el-dropdown-item>
                    <el-dropdown-item @click.native="logoutAndRedirect">logout</el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>
        </template>
        <template v-else>
            <div class="header-item" style="margin-left: 25%">
                <img src="../../assets/personal.png" width="100px" @click="toLogin">
            </div>
        </template>
    </div>
</template>

<script src="./header.js">
</script>

<style scoped>
.header-container {
    display: flex; /* 使用 Flex 布局 */
    align-items: center; /* 垂直居中对齐 */
    flex-direction: row;
}

.header-item {
    align-items: center;
    padding: 10px; /* 添加一些内边距 */
    font-size: 16px; /* 设置字体大小 */
}

.header-item img {
    max-width: 100px; /* 限制图片宽度 */
}

.header-item .title {
    font-size: 30px;

}

.custom-select {
    width: 200px; /* 设置下拉框宽度 */
    font-size: 16px; /* 设置字体大小 */
    margin-left: 15%;
}


/* 可根据需要调整下拉菜单的样式 */
.el-dropdown-menu {
    //background-color: black;
}
.el-dropdown-item {
    color: white;
}
</style>
