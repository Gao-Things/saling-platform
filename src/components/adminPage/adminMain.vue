<template>
    <div class="admin-Main">
        <el-table :data="tableData"
                  :cell-style ="{background:'#000000', color: '#ffffff',textAlign: 'center'}"
                  style="font-size: 18px"
                  :header-row-style="{height:'80px'}"
                  :header-cell-style="{background:'#000000', color: '#ffffff', fontSize:'19px',textAlign: 'center', fontweight:700}"

        >
            <el-table-column prop="id"  width="180">
                <img src="@/assets/img_1.png" alt="Logo" class="logo">
            </el-table-column>
            <el-table-column prop="productName" label="Item Name" width="180">
            </el-table-column>
            <el-table-column prop="productPrice" label="price($)" width="180">
            </el-table-column>
            <el-table-column prop="phone"  width="180">
                <img src="@/assets/img_2.png" alt="Logo" class="logo">
            </el-table-column>

            <el-table-column prop="productPrice" label="Converted price($)" width="180">
            </el-table-column>

            <el-table-column prop="formattedTimestamp" label="update time" width="200">
            </el-table-column>

            <el-table-column prop="formattedTimestamp" label="Growth" width="200">
                <lineChart></lineChart>
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

.admin-Main{
    background-color: black;
}
.el-table td.el-table__cell {
    border: 0;
}

.echarts-container {
    width: 100%;
}
</style>

