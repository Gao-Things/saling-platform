<template>
  <div>
    <div class="h1-container">
      <h1 style="color: #008604; line-height: 2; ">"Please ensure the uploaded logo is a transparent PNG image with an aspect ratio ideally of 1:1."</h1>
    </div>

    <el-row type="flex" direction="column" class="logo-upload-container">
      <el-col :span="24">
        <div class="logo-container">

          <div class="image-upload-container">
            <h3>startup logo</h3>
            <el-image
                style="width: 200px; height: 200px; margin-top: 20px"
                :src="websiteLogo"
                fit="contain"
            ></el-image>

            <input
                ref="fileInput1"
                type="file"
                accept="image/*"
                style="display: none;"
                @change="handleFileChange "
            />
            <!-- 使用 custom-button 类设置通用样式 -->
            <el-button size="small" type="success" style="margin-top: 20px" class="custom-button select-file-button"
                       @click="chooseFile('fileInput1')">Update Image
            </el-button>
          </div>



        </div>
      </el-col>
      <el-col :span="24">
        <div class="logo-container">
          <div class="image-upload-container">
          <h3>internal logo</h3>
          <el-image
              style="width: 200px; height: 200px; margin-top: 20px;"
              :src="appLogo"

              fit="contain"
          ></el-image>


          <input
              ref="fileInput2"
              type="file"
              accept="image/*"
              style="display: none;"
              @change="handleFileChange2"
          />
          <!-- 使用 custom-button 类设置通用样式 -->
          <el-button size="small" type="success" class="custom-button select-file-button"
                     style="margin-top: 20px"
                     @click="chooseFile('fileInput2')">Update
            Image
          </el-button>

          </div>

        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  data() {
    return {
      websiteLogo: 'https://capstone-file-store.s3.amazonaws.com/logo1.png',
      appLogo: 'https://capstone-file-store.s3.amazonaws.com/logo2.png',
    };
  },
  methods: {
    chooseFile(refName) {
      this.$refs[refName].click();
    },
    // 处理文件更改的方法
    handleFileChange(event) {
      const selectedFile = event.target.files[0];
      if (!selectedFile) {
        return;
      }
      this.uploadFile(selectedFile, 1);
    },
    handleFileChange2(event) {
      const selectedFile = event.target.files[0];
      if (!selectedFile) {
        return;
      }
      this.uploadFile(selectedFile, 2);
    },
    // 上传文件的方法
    uploadFile(file, logoType) {
      // 开启全局 Loading
      let loadingInstance = this.$loading({
        lock: true,
        text: 'Loading...',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.4)'
      });

      const formData = new FormData();
      formData.append('file', file);

      if (logoType == 1) {
        formData.append("uploadType", 1)

      } else {
        formData.append("uploadType", 2)
      }

      this.$axios.post(this.$httpurl + '/public/admin/adminUploadLogo', formData)
          .then(res => res.data)
          .then(res => {
            if (res.code === 200) {
              console.log("upload successful");
              if (logoType == 1) {
                this.websiteLogo = this.$imageurl + res.data
              } else {
                this.appLogo = this.$imageurl + res.data
              }
              // 关闭 Loading
              loadingInstance.close();
              window.location.reload();

            } else {
              // 关闭 Loading
              loadingInstance.close();
              this.$message.error(res.data)
            }
          });
    }
  },
};
</script>

<style scoped>
.logo-upload-container {
  width: 70%;
  margin: auto;
}

.logo-container {
  text-align: center;
  margin-top: 50px;
}

.upload-icon .el-upload__input {
  display: none;
}

.image-upload-container {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.h1-container {
  display: block; /* 确保 h1 是块级元素 */
  margin-top: 20px;
  margin-left: 20px;
  width: 60%; /* 确保 h1 不会影响其他元素的布局 */
}

.h1-container h1 {
  background-color: #f0f0f0;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px; /* 确保在 h1 和 el-row 之间有足够空间 */
}
</style>
