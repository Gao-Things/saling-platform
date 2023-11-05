<template>
  <div class="container">
    <div class="content">
      <img src="@/assets/logoUse.png" alt="Website Logo" class="logo">
      <h1 class="welcome-title">Please note ！</h1>
      <p class="welcome-description">You are resetting your password.</p>
      <el-alert
          v-if="verificationSuccess"
          type="success"
          :closable="false"
          show-icon
          title="Email verification successful !"
      ></el-alert>
      <el-alert
          v-if="passwordModified"
          type="success"
          :closable="false"
          show-icon
          title="Modify password successful"
          description="Please log in with your new password."
      ></el-alert>
      <div v-if="verificationSuccess && !passwordModified" class="reset-password-form" style="margin-top: 30px">
        <el-input
            v-model="newPassword"
            type="password"
            placeholder="Enter new password"
            class="input-with-margin"
        ></el-input>
        <el-input
            v-model="confirmPassword"
            type="password"
            placeholder="Confirm new password"
            class="input-with-margin"
        ></el-input>
        <el-button type="primary" @click="submitNewPassword">Submit</el-button>
      </div>
      <el-alert
          v-if="!verificationSuccess && errorMessage"
          type="error"
          :closable="false"
          show-icon
          title="Email verification failed !"
          :description="errorMessage"
      ></el-alert>
    </div>
  </div>
</template>



<script>

export default {
  data() {
    return {
      verificationSuccess: false,
      errorMessage:"",
      successMessage:"",
      newPassword: "",
      confirmPassword: "",
      userEmail:"",
      passwordModified: false
    };
  },
  created() {
    this.verifyEmail();
  },
  methods: {
    submitNewPassword() {
      // 验证密码长度
      if (this.newPassword.length <= 6) {
        this.$message.error('Password must be longer than 6 characters.');
        return;
      }
      // 验证密码是否包含至少一个字母和一个数字
      if (!this.newPassword.match(/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{7,}$/)) {
        this.$message.error('Password must contain at least one letter and one number.');
        return;
      }
      // 验证两次输入的密码是否一致
      if (this.newPassword !== this.confirmPassword) {
        this.$message.error('Passwords do not match.');
        return;
      }
      // 这里添加提交新密码的逻辑
      console.log('New password is set:', this.newPassword);

      // 调用API来更新密码
      const updatePasswordBody = {
        email: this.userEmail,
        password: this.newPassword,
        password2: this.confirmPassword
      }

      this.$axios.post(this.$httpurl + '/public/updatePassword', updatePasswordBody)
          .then(res => res.data)
          .then(res => {
            console.log(res);
            if (res.code === 200) {
              this.passwordModified = true; // 控制提示信息的显示
              this.$message({
                message: 'Update password successfully !',
                type: 'success' // 设置消息类型，可以是success、warning、info、error等
              })

            } else {
              this.$message({
                message: 'Update password fail ',
                type: 'error' // 设置消息类型，可以是success、warning、info、error等
              })
            }
          });

    },
    goToLogin() {
      this.$router.push("/");
    },
    async verifyEmail() {
      const urlParams = new URLSearchParams(window.location.search);
      const email = urlParams.get("email");
      this.userEmail = email
      const resettingPasswordTimestamp = urlParams.get("resettingPasswordTimestamp");

      try {
        const response = await this.$axios.get(this.$httpurl + '/public/forgetPasswordVerification', {
          params: {
            email: email,
            resettingPasswordTimestamp: resettingPasswordTimestamp,
          }
        });

        if (response.data.code === 200) {
          console.log(response.data);
          this.verificationSuccess = true;
        } else {
          this.verificationSuccess = false;
          this.errorMessage = response.data.msg;  // Changed from "response.msg" to "response.data.msg"
          this.$message.error(this.errorMessage);
        }
      } catch (error) {
        console.error("An error occurred:", error);
      }
    }
  }
};
</script>

<style scoped>
.reset-password-form {
  display: flex;        /* 启用Flexbox */
  flex-direction: column; /* 子元素垂直排列 */
  align-items: center;  /* 子元素在交叉轴上居中对齐（在这里是水平居中） */
  margin-top: 30px;     /* 与其他元素的距离 */
}

.input-with-margin {
  width: 100%; /* 如果你希望输入框宽度充满容器 */
  margin-bottom: 15px; /* 为输入框添加间距 */
}
.particles {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  z-index: -1;
}
.container {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #008604;
  justify-content: center;
  align-items: center;
  font-family: 'Arial', sans-serif;  /* 使用Arial字体，你也可以选择其他的字体 */
}

.content {
  background-color: rgba(255, 255, 255, 0.95);  /* 更加明亮的背景 */
  box-shadow: 0px 0px 12px rgba(0, 0, 0, 0.1);  /* 添加阴影 */
  margin-top: 40px;
  padding: 30px 20px;  /* 增加垂直间距 */
  border-radius: 10px;
  width: 80%;
  max-width: 400px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.logo {
  width: 200px;
  max-width: 100%;
  height: auto;
  margin-bottom: 40px;
}

.login-button {
  margin-top: 20px;  /* 添加按钮的间距 */
}

.welcome-title {
  font-size: 1.8em;  /* 调整字体大小 */
  margin-bottom: 15px;  /* 增加间距 */
  color: #333;  /* 字体颜色 */
}

.welcome-description {
  font-size: 1em;
  margin-bottom: 25px;  /* 增加间距 */
  color: #666;  /* 字体颜色 */
  text-align: center;  /* 文本居中 */
}
</style>

