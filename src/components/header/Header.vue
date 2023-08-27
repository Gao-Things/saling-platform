<template>
    <div class="header-container">
        <div class="header-item">
            <i :class="icon" style="font-size: 20px" @click="collapse"></i>
        </div>
        <div class="header-item">
            <img src="../../assets/img_1.png" width="100px">
        </div>
        <div class="header-item" style="text-align: center">
            <span class="title">CS COMPANY</span>
        </div>
        <el-select class="header-item custom-select" v-model="selectedValue" @change="handleSelectChange">

            <el-option label="AUD" value="personal"></el-option>
            <el-option label="USD" value="logout"></el-option>
        </el-select>

        <div class="header-item" style="padding: 10px">
            <img src="../../assets/img_2.png" width="100px">
        </div>
        <el-dropdown class="header-item" style="margin-left: 10%">
            <span style="color: #eeeeee">ADMIN USER</span>
            <i class="el-icon-arrow-down" style="margin-left: 5px"></i>
            <el-dropdown-menu slot="dropdown">
                <el-dropdown-item @click.native="toUser">personal central</el-dropdown-item>
                <el-dropdown-item>login out</el-dropdown-item>
            </el-dropdown-menu>
        </el-dropdown>
    </div>
</template>

<script src="./header.js">
</script>

<style scoped>
.header-container {
    display: flex; /* 使用 Flex 布局 */
    align-items: center; /* 垂直居中对齐 */
    background-color: black;
    flex-direction: row;
    color: white;
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
    font-size: 35px;

}

.custom-select {
    width: 200px; /* 设置下拉框宽度 */
    font-size: 16px; /* 设置字体大小 */
    margin-left: 15%;
}


/* 可根据需要调整下拉菜单的样式 */
.el-dropdown-menu {
    background-color: black;
}
.el-dropdown-item {
    color: white;
}
</style>
