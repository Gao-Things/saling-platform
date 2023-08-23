<template>
  <div>
    <el-table :data="tableData"
              :header-cell-style="{background:'#cae6ff', color: '#727070'}"
              border
    >
      <el-table-column prop="id" label="ID" width="180">
      </el-table-column>
      <el-table-column prop="name" label="NAME" width="180">
      </el-table-column>
      <el-table-column prop="no" label="ACCOUNT" width="180">
      </el-table-column>
      <el-table-column prop="phone" label="PHONE" width="200">
      </el-table-column>
      <el-table-column prop="sex" label="SEX" width="80">
        <template slot-scope="scope">
          <el-tag
              :type="scope.row.sex === 1 ? 'success' : 'primary'"
              disable-transitions>{{ scope.row.sex === 1 ? 'male' : 'female' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="roleId" label="ROLE" width="180">
        <template slot-scope="scope">
          <el-tag
              :type="scope.row.roleId === 0 ? 'danger' : (scope.row.roleId === 1 ? 'success' : 'primary') "
              disable-transitions>{{ scope.row.roleId === 0 ? 'super administer' : (scope.row.roleId === 1 ? 'administer' : 'user') }}
          </el-tag>
        </template>
      </el-table-column>


      <el-table-column prop="operation" label="OPERATE" width="170">
        <div class="button-container">
          <el-button class="button-spacing" size="small" type="success">EDIT</el-button>
          <el-button class="button-spacing" size="small" type="danger">DELETE</el-button>
        </div>
      </el-table-column>

    </el-table>
    <el-pagination
        @size-ch
        background
        layout="prev, pager, next"
        :total="1000">
    </el-pagination>
  </div>

</template>

<script src="./main.js"></script>

<style scoped>
.button-container {
  display: flex;
}

.button-spacing {
  margin-right: 5px; /* 调整按钮之间的间距 */
}
</style>