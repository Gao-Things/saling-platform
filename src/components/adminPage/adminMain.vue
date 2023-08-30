<template>
  <div class="admin-Main">
    <el-table :data="tableData"
              :cell-style="{background:'#000000',padding: '0',textAlign: 'center'}"
              :row-class-name="getRowClassName"
              style="font-size: 18px; width: max-content; min-width: 105%;"
              :header-row-style="{height:'80px'}"
              :header-cell-style="{background:'#000000', color: '#ffffff', fontSize:'19px',textAlign: 'center', fontweight:700}"

    >
      <el-table-column prop="product.id">
        <img src="@/assets/img_1.png" alt="Logo" class="logo">
      </el-table-column>
      <el-table-column prop="product.productName" label="Item Name" >
      </el-table-column>
      <el-table-column prop="product.productPrice" label="price($)" >
      </el-table-column>
      <el-table-column prop="product.phone" width="180">
        <img src="@/assets/img_2.png" alt="Logo" class="logo">
      </el-table-column>

      <el-table-column prop="product.productExchangePrice" label="Converted price" >
      </el-table-column>

      <el-table-column prop="product.formattedTimestamp" label="update time" >
        <span>2023-8-26 06:33:33</span>
      </el-table-column>

      <el-table-column prop="product.formattedTimestamp" label="Growth" width="250" >
        <template slot-scope="scope">
          <div class="centered-content">
        <lineChart :categories="scope.row.priceUpdateTime" :values="scope.row.priceUpdateRecord" :color="'red'" :key="scope.row.product.id"></lineChart>
          </div>
        </template>
      </el-table-column>

      <el-table-column prop="operation" label="OPERATE">
        <template slot-scope="scope">
        <div class="button-container">
          <el-button class="button-spacing" size="small" type="success" @click="openDialog(scope.row.product.id, scope.row.product.currentTurnOfRecord)">EDIT</el-button>
          <el-dialog :visible.sync="dialogVisible" title="Enter Price" custom-class="dark-dialog">
              <el-input v-model="price" placeholder="Enter the price" type="number" inputmode="numeric"></el-input>
              <span slot="footer" class="dialog-footer">
              <el-button @click="closeDialog">Cancel</el-button>
              <el-button type="primary" @click="savePrice">Save</el-button>
            </span>
          </el-dialog>
          <el-button class="button-spacing" size="small" type="danger">DELETE</el-button>

        </div>
        </template>
      </el-table-column>

    </el-table>
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        background
        layout="prev, pager, next"
        :total="totalItems"
        :page-size="pageSize"
        :current-page="currentPage"
    >
    </el-pagination>
  </div>

</template>

<script src="./adminMain.js"></script>

<style>
.logo {
  width: 80px;
  height: 80px;
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
  background-color: black;
}

.el-table td.el-table__cell {
  border: 0;
}

.echarts-container {
  width: 100%;
}

.dark-dialog {
  background-color: #333; /* Set the background color to dark gray */
  color: #000000; /* Set the text color to white */
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

