<template>
  <div class="admin-Main">
    <el-table :data="tableData"
              @row-click="handleRowClick"
              :cell-style="{padding: '0',textAlign: 'center'}"
              style="font-size: 18px; width: max-content; min-width: 105%;"
              @sort-change="handleSortChange"
              :header-row-style="{height:'80px'}"
              :header-cell-style="{fontSize:'19px',textAlign: 'center', fontweight:700}"
              row-key="id"
    >

      <el-table-column prop="productName" label="Product Name">
      </el-table-column>
      <el-table-column prop="id" label="Image">
        <template slot-scope="scope">
          <img :src="getImageUrl(scope.row.productImage)" alt="Product Image" class="logo">
        </template>

      </el-table-column>
      <el-table-column
          prop="category"
          label="Category"
          :filters="categoryFilters"
          :filter-method="filterCategory"
          filterable
      >

        <template slot-scope="scope">
          <!-- 根据category的值来显示不同的图片 -->
          <img v-if="scope.row.category === 1" src="../../assets/gold.png" alt="Gold" class="logo2">
          <img v-else-if="scope.row.category === 2" src="../../assets/sliver.png" alt="Silver" class="logo2">
        </template>
      </el-table-column>

      <el-table-column
          prop="productStatus"
          label="Status"
          :filters="statusFilters"
          :filter-method="filterStatus">

        <template slot-scope="scope">
          <!-- 使用条件渲染来显示不同的标签 -->
          <el-tag v-if="scope.row.productStatus === 0" type="success">Open</el-tag>
          <el-tag v-else-if="scope.row.productStatus === 1" type="info">Closed</el-tag>
          <el-tag v-else-if="scope.row.productStatus === 2" type="danger">Sold</el-tag>
          <el-tag v-else-if="scope.row.productStatus === 3" type="danger">Cancelled</el-tag>
        </template>
      </el-table-column>


      <el-table-column
          prop="productCreateTime"
          label="Upload time"
          sortable>
        <template v-slot="scope">
          <span>{{ formatDate(scope.row.productCreateTime) }}</span>
        </template>
      </el-table-column>

      <el-table-column
          prop="offerNumber"
          label="Offer Number"
          sortable="custom"
          width="250">
        <template slot-scope="scope">
          <!-- 这里直接展示当前产品的offer数量 -->
          <span>{{ scope.row.offers ? scope.row.offers.length : 0 }}</span>
        </template>
      </el-table-column>

      <el-table-column prop="operation" label="OPERATE">
        <template slot-scope="scope">
          <div class="button-container">
            <el-button class="button-spacing" size="small" type="success" @click.stop="openDialog(scope.row)">EDIT
            </el-button>
            <el-button class="button-spacing" size="small" type="danger" @click.stop="confirmDelete(scope.row)">DELETE</el-button>

          </div>
        </template>
      </el-table-column>

    </el-table>

    <el-dialog :visible.sync="dialogVisible" title="Edit Product" custom-class="dark-dialog">
      <el-form ref="editForm" :model="editingProduct">
        <el-form-item label="Product Name">
          <el-input v-model="editingProduct.productName"></el-input>
        </el-form-item>
        <el-form-item label="Product Description">
          <el-input type="textarea" v-model="editingProduct.productDescription"></el-input>
        </el-form-item>
        <el-form-item label="Category">
          <!-- 假设 category 是一个选择项 -->
          <el-select v-model="editingProduct.category">
            <el-option label="Gold" :value="1"></el-option>
            <el-option label="Silver" :value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Status">
          <!-- 状态也是选择项 -->
          <el-select v-model="editingProduct.productStatus">
            <el-option label="Open" :value="0"></el-option>
            <el-option label="Closed" :value="1"></el-option>
            <el-option label="Sold" :value="2"></el-option>
            <el-option label="Cancelled" :value="3"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Images">
          <div v-if="getImageUrls(editingProduct.productImage).length > 0" class="image-preview-container">
            <div v-for="(image, index) in getImageUrls(editingProduct.productImage)" :key="index" class="image-preview-item">
              <img :src="image.url" class="image-preview">
              <el-button
                  class="delete-image-button"
                  type="danger"
                  icon="el-icon-close"
                  circle
                  @click.stop="removeImage(image.filename)"
              ></el-button>
            </div>
          </div>
          <p v-else class="image-empty">The picture is Empty</p>
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

<script src="./adminMain.js"></script>

<style>
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
</style>

