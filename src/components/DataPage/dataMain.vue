<template>
  <div class="dashboard" style="margin-top: 30px" >
    <el-row type="flex" justify="center" class="header-row">
      <el-col :span="24">
        <h1 style="color: #008604"> Welcome To Metal Trading Management System</h1>
      </el-col>
    </el-row>
    <el-row gutter="20" class="echart-row" type="flex" justify="center">
      <el-col :span="11" class="echart-container" >
        <PieChart :data="genderData" title="User Gender Distribution"/>
      </el-col>
      <el-col :span="11" class="echart-container">
        <PieChart :data="genderData" title="User Gender Distribution"/>
      </el-col>
    </el-row>
    <el-row gutter="20" class="echart-row" type="flex" justify="center">
      <el-col :span="11" class="echart-container">
        <PieChart :data="genderData" title="User Gender Distribution"/>
      </el-col>
      <el-col :span="11" class="echart-container">
        <BarChart :data="barChartData" title="某某数据统计"/>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import 'element-ui/lib/theme-chalk/index.css';
import { Row, Col } from 'element-ui';
import PieChart from '../chart/PieChart.vue';
import BarChart from '../chart/BarChart.vue';
import store from "@/store";

export default {
  name: 'Dashboard',
  components: {
    'el-row': Row,
    'el-col': Col,
    PieChart,
    BarChart
  },
  data() {
    return {
      // 初始化饼图数据
      genderData: [],
      barChartData: []
    };
  },

  mounted() {
    this.fetchGenderData();
  },
  methods: {
    fetchGenderData() {
      // 调用API获取数据的代码
      this.getGenderStatistic();

      // 假设这是从API获取的数据
      this.barChartData = [
        { name: '产品A', value: 20 },
        { name: '产品B', value: 50 },
        { name: '产品C', value: 30 },
        // ...其他数据
      ];


    },

    getGenderStatistic() {
      // 格式化数据以适应ECharts的数据格式
      // this.genderData = [
      //   { value: 100, name: 'Male' },
      //   { value: 80, name: 'Female' },
      //   { value: 20, name: 'Unknown' }
      // ];

      // 开启全局 Loading
      let loadingInstance = this.$loading({
        lock: true,
        text: 'Loading...',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.4)'
      });

      this.$axios.get(this.$httpurl + '/public/admin/genderStatistic')
          .then(res => res.data)
          .then(res => {

            if (res.code === 200) {
              // 关闭 Loading
              loadingInstance.close();
              this.genderData = res.data

            } else {
              alert("failed to get the data")
            }
          })
    }
  }
};
</script>

<style scoped>
.header-row {
  margin-bottom: 20px;
  text-align: center;
}
.echart-row {
  margin-bottom: 20px; /* Add space between the rows */
}
.echart-container {
  height: 400px; /* Adjust the height as needed */
}
.echart {
  width: 100%;
  height: 100%;
}
h1 {
  color: #333;
  font-size: 2em;
}
</style>
