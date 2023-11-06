<template>
  <div class="admin-Main">

    <el-table :data="tableData"
              :cell-style="{padding: '0',textAlign: 'center'}"
              style="font-size: 18px; width: max-content; min-width: 105%;"
              @sort-change="handleSortChange"
              :header-row-style="{height:'80px'}"
              :header-cell-style="{fontSize:'19px',textAlign: 'center', fontweight:700}"
              row-key="id"
    >


      <el-table-column prop="id" label="User Avatar">
        <template slot-scope="scope">
          <img :src="getAvatarUrl(scope.row.avatarUrl)" @error="setDefaultAvatar" alt="Buyer Avatar" class="avatar">
        </template>

      </el-table-column>

      <el-table-column prop="name" label="User Name">
      </el-table-column>

      <el-table-column prop="email" label="Email">
      </el-table-column>

      <el-table-column
          prop="gender"
          label="Gender"
          :filters="genderFilters"
          :filter-method="genderStatus">
        <template slot-scope="scope">
          <!-- 根据category的值来显示不同的图片 -->
          <img v-if="scope.row.gender === 1" src="../../assets/male.png" alt="Gold" class="logo2">
          <img v-else-if="scope.row.gender === 0" src="../../assets/unknow.png" alt="Silver" class="logo2">
          <img v-else-if="scope.row.gender === 2" src="../../assets/female.png" alt="Silver" class="logo2">
        </template>
      </el-table-column>


      <el-table-column
          prop="activationStatus"
          label="Account status"
          :filters="statusFilters"
          :filter-method="filterStatus">

        <template slot-scope="scope">
          <!-- 使用条件渲染来显示不同的标签 -->
          <el-tag v-if="scope.row.activationStatus === true" type="success">Active</el-tag>
          <el-tag v-else-if="scope.row.activationStatus === false" type="danger">inactivated</el-tag>
        </template>
      </el-table-column>


      <el-table-column
          prop="registrationTimestamp"
          label="Registration time"
          sortable>
        <template v-slot="scope">
          <span>{{ formatDate(scope.row.registrationTimestamp) }}</span>
        </template>
      </el-table-column>


      <el-table-column prop="operation" label="OPERATE">
        <template slot-scope="scope">
          <div class="button-container">
            <el-button class="button-spacing" size="small" type="success" @click.stop="openDialog(scope.row)">EDIT
            </el-button>
            <el-button class="button-spacing" size="small" type="warning" @click.stop="confirmDelete(scope.row)">Reset Password</el-button>

          </div>
        </template>
      </el-table-column>

    </el-table>

    <el-dialog :visible.sync="dialogVisible" title="Edit User Information" custom-class="dark-dialog">
      <el-form ref="editForm" :model="editingProduct">
        <el-form-item label="User name">
          <el-input v-model="editingProduct.name"></el-input>
        </el-form-item>

        <el-form-item label="User Email">
          <el-input type="textarea" v-model="editingProduct.email"></el-input>
        </el-form-item>

        <el-form-item label="User Gender">
          <el-select v-model="editingProduct.gender">
            <el-option label="Male" :value=1></el-option>
            <el-option label="Female" :value=2></el-option>
            <el-option label="Unknown" :value=0></el-option>
          </el-select>
        </el-form-item>


        <el-form-item label="Acount Status">
          <!-- 状态也是选择项 -->
          <el-select v-model="editingProduct.activationStatus">
            <el-option label="Active" :value=true></el-option>
            <el-option label="Inactivated" :value=false></el-option>
          </el-select>
        </el-form-item>

        <!-- ...其他表单项... -->
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeDialog">Cancel</el-button>
        <el-button type="primary" @click="saveProduct">Save</el-button>
      </span>
    </el-dialog>

    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        background
        layout="sizes, prev, pager, next"
        :total="totalItems"
        :page-sizes="[3, 5, 10, 20]"
        :page-size="pageSize"
        :current-page="currentPage"
    >
    </el-pagination>
  </div>

</template>

<script src="./userMain.js">
</script>

<style>
.product-details {
  /* 样式定义 */
  display: flex;
  align-items: center;
  margin-bottom: 20px; /* 表格和详情之间的间距 */
}

.product-image {
  /* 样式定义 */
  width: 100px; /* 图片宽度 */
  height: 100px; /* 图片高度 */
  margin-right: 20px; /* 图片和标题之间的间距 */
}

.product-details h2 {
  /* 样式定义 */
  margin: 0;
  font-size: 24px;
}

.product-details p {
  /* 样式定义 */
  margin: 0;
  font-size: 18px;
}
/* ...其他样式... */
.image-preview-container {
  display: flex;
  flex-wrap: wrap;
}
.image-preview-item {
  margin-right: 10px;
  margin-bottom: 10px;
  position: relative;
  overflow: hidden; /* 确保超出容器的部分（如删除按钮）被隐藏 */
}
.image-preview {
  width: 100px;
  height: 100px;
  object-fit: cover;
  border: 1px solid #ddd;
  transition: filter 0.3s; /* 可选的，给图片添加过渡效果 */
}
.delete-image-button {
  display: none;
  position: absolute;
  top: 0;
  right: 0;
  padding: 4px; /* 减少内边距以减小按钮大小 */
  min-width: auto;
  font-size: 12px; /* 如果需要，减小图标的字体大小 */
  line-height: 1; /* 调整行高以确保图标垂直居中 */
}
/* 可选：当按钮是mini尺寸时进一步调整样式 */
.el-button--mini {
  min-height: 20px; /* 调整按钮的最小高度 */
  min-width: 20px; /* 调整按钮的最小宽度 */
}
.image-preview-item:hover .delete-image-button {
  display: block; /* 当鼠标悬停时显示删除按钮 */
}
.image-preview-item:hover .image-preview {
  filter: brightness(0.7); /* 可选的，当鼠标悬停时给图片添加效果 */
}
.logo {
  width: 80px;
  height: 80px;
}

.logo2 {
  width: 40px;
  height: 40px;
}

.centered-content {
  display: flex;
  justify-content: center; /* 水平居中对齐 */
  align-items: center; /* 垂直居中对齐 */
  height: 100%; /* 使用 100% 高度以实现垂直居中 */
}

.button-container {
  display: flex;
}

.button-spacing {
  margin-right: 5px; /* 调整按钮之间的间距 */
}

.admin-Main {

}

.el-table td.el-table__cell {
  border: 0;
}

.echarts-container {
  width: 100%;
}

/* Set title text color to white */
.dark-dialog .el-dialog__header {
  color: #fff;
}

.dialog-footer {
  text-align: right;
}

.red-row {
  color: #ff0000;
}

.normal-row {
  color: #eeeeee;
}

/* 头像样式 */
.avatar {
  width: 70px; /* 设置头像的大小，根据需要调整 */
  height: 70px; /* 设置头像的大小，根据需要调整 */
  margin-top: 10px;
  margin-bottom: 10px;
  border-radius: 50%; /* 圆形效果 */
  object-fit: cover; /* 确保图片覆盖整个内容区域 */
}
</style>

