<template>
    <div class="big-container">
        <div class="logo-container">
            <img src="https://capstone-file-store.s3.amazonaws.com/logo2.png" alt="Logo" class="logo" v-if="!isCollapse">
            <h2 style="margin-top: 20px; color: white" v-if="!isCollapse">{{ getRole }}</h2>
        </div>

        <el-menu
                text-color="white"
                active-text-color="#ffd04b"
                class="custom-menu"
                :collapse="isCollapse"
                :collapse-transition="false"
                router>
            <!-- 遍历从父组件获取到的aside item  -->
            <el-menu-item v-for="link in links" :key="link.index" :index="link.index">
                <i :class="link.icon" style="color: #f3f3f3"></i>
                <span slot="title">{{ link.title }}</span>
            </el-menu-item>

        </el-menu>
    </div>
</template>

<script src="./aside.js"></script>

<style scoped>
.custom-menu /deep/ .el-menu-item:hover {
  background-color: #60bb35 !important; /* 将 #yourColorHere 替换为你想要的颜色 */
}

.custom-menu ::v-deep .el-menu-item.is-active {
  background-color: #60bb35 !important; /* 将 #desiredColor 替换为你想要的颜色 */
}
.logo-container {
    display: flex;
    justify-content: center;
    flex-direction: column;
    align-items: center;
    height: 200px; /* 调整 Logo 容器的高度 */
    padding: 10px;
    margin-top: 50px;
}

.big-container {
    display: flex;
    flex-direction: column; /* 添加此行以垂直排列子元素 */
    align-items: center; /* 居中对齐子元素 */
    height: 100vh;

}

.custom-menu {
    padding: 0; /* 移除内边距 */
    border: none; /* 移除边框 */
    height: 100%;
    /*width: 100%;*/

    margin-top: 30px;
    background-color: transparent;
    font-size: 36px
}

.logo {
    padding: 10px;
    width: 110px; /* 调整 Logo 的宽度 */
    height: 110px; /* 调整 Logo 的高度 */
}

</style>