<template>
    <el-container style="height: 100%; border: 1px solid #000000;">
        <el-aside :width="aside_width" style="background-color: #008604;margin-left: -1px ">
            <!--            将从vuex中拿出的当前用户的sidebar传给Aside组件-->
            <Aside :isCollapse="isCollapse"></Aside>
        </el-aside>

        <el-container style="height: 100%;">
            <el-header
                    style="text-align: right; font-size: 12px; height: 100%; border-bottom: rgb(255,255,255) 2px solid">
                <Header @doCollapse="doCollapse" @selectedValue="selectedValue" :icon="icon"></Header>
            </el-header>

            <el-main style="height: 100%;  margin-left: -1px">
                <adminMain :exchangeValue="exchangeValue"></adminMain>
            </el-main>
        </el-container>
    </el-container>
</template>

<script src="./admin.js">
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