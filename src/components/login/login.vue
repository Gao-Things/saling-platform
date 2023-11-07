<template>
    <div class="login-page">
        <img src="https://capstone-file-store.s3.amazonaws.com/logo1.png" alt="Logo" class="logo">
        <el-form class="login-form" ref="loginForm" :model="loginForm" label-width="80px">
            <el-form-item label="Email">
                <el-input v-model="loginForm.email" placeholder="enter email"></el-input>
            </el-form-item>
            <el-form-item label="password">
                <el-input type="password" v-model="loginForm.password" placeholder="enter password"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="success" @click="loginPress">Login</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script src="./login.js">
</script>

<style scoped>
.login-page {
  background-color: #f0f2f5; /* 更亮的背景可以带来更好的对比 */
  height: 100vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.logo {
  width: 160px; /* 可以根据实际Logo大小调整 */
  height: 140px; /* 可以根据实际Logo大小调整 */
  margin-bottom: 30px;
}

.login-form {
  width: 340px; /* 略微增加宽度以适应内容 */
  background-color: #ffffff; /* 白色背景可以增加对比度 */
  padding: 30px; /* 增加填充以提供更多空间感 */
  border-radius: 10px; /* 增加圆角 */
  box-shadow: 0 4px 10px rgba(0,0,0,0.1); /* 添加阴影效果 */
  border: 1px solid #ebedf0; /* 添加边框 */
}

.el-form-item {
  margin-bottom: 24px; /* 增加表单项的间距 */
}

.el-input {
  border-radius: 4px; /* 输入框增加圆角 */
}

.el-button {
  width: 100%; /* 使按钮宽度与输入框一致 */
  border-radius: 4px; /* 按钮增加圆角 */
}

.el-button:hover {
  background-color: #006400; /* 鼠标悬停时按钮颜色变深 */
  border-color: #006400;
}
</style>

