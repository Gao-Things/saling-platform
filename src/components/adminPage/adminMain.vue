<template>
  <div class="admin-Main">
    <el-table :data="tableData"
              :cell-style="{background:'#000000',padding: '0',textAlign: 'center'}"
              :row-class-name="getRowClassName"
              style="font-size: 18px"
              :header-row-style="{height:'80px'}"
              :header-cell-style="{background:'#000000', color: '#ffffff', fontSize:'19px',textAlign: 'center', fontweight:700}"

    >
      <el-table-column prop="id" width="180">
        <img src="@/assets/img_1.png" alt="Logo" class="logo">
      </el-table-column>
      <el-table-column prop="productName" label="Item Name" width="180">
      </el-table-column>
      <el-table-column prop="productPrice" label="price($)" width="180">
      </el-table-column>
      <el-table-column prop="phone" width="180">
        <img src="@/assets/img_2.png" alt="Logo" class="logo">
      </el-table-column>

      <el-table-column prop="productExchangePrice" label="Converted price" width="180">
      </el-table-column>

      <el-table-column prop="formattedTimestamp" label="update time" width="200">
      </el-table-column>

      <el-table-column prop="formattedTimestamp" label="Growth" width="200">
        <lineChart></lineChart>
      </el-table-column>

      <el-table-column prop="operation" label="OPERATE" width="170">
        <template slot-scope="scope">
        <div class="button-container">
          <el-button class="button-spacing" size="small" type="success" @click="openDialog(scope.row.id, scope.row.currentTurnOfRecord)">EDIT</el-button>
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

