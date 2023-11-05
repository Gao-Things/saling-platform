<template>
    <div class="Main">
        <el-table :data="tableData"
                  :cell-style="{background:'#000000',padding: '0', color: '#ffffff',textAlign: 'center'}"
                  style="font-size: 18px; width: max-content; min-width: 105%;"
                  :header-row-style="{height:'80px'}"
                  :row-style ="{height:'130px'}"
                  :header-cell-style="{background:'#000000', color: '#ffffff', fontSize:'19px',textAlign: 'center', fontweight:700}"

        >
            <el-table-column prop="id">
                <img src="@/assets/img_1.png" alt="Logo" class="logo">
            </el-table-column>
            <el-table-column prop="product.productName" label="Item Name" >
            </el-table-column>
            <el-table-column prop="product.productPrice" label="price($)" >
            </el-table-column>
            <el-table-column prop="product.phone">
                <img src="@/assets/img_2.png" alt="Logo" class="logo">
            </el-table-column>

            <el-table-column prop="product.productExchangePrice" label="Converted price" >
            </el-table-column>

            <el-table-column prop="product.formattedTimestamp" label="update time" >
              <span>2023-8-26 06:33:33</span>
            </el-table-column>

            <el-table-column prop="product.formattedTimestamp" label="Growth" >
                <img src="@/assets/arrow.png" alt="Logo" class="arrow">
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

<script src="./main.js">
import lineChart from "@/components/chart/lineChart.vue";

export default {
    components: {lineChart}
}
</script>

<style>
.logo {
    width: 80px;
    height: 80px;
}
.arrow{
    width: 20px;
    height: 20px;
}
.button-container {
    display: flex;
}

.button-spacing {
    margin-right: 5px; /* 调整按钮之间的间距 */
}


.el-table td.el-table__cell {
    border: 0;
}

.echarts-container {
    width: 100%;
}
</style>

