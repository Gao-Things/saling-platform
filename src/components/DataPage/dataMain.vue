<template>
  <div class="dashboard" style="margin-top: 30px" >
    <el-row type="flex" justify="center" class="header-row">
      <el-col :span="24">
        <h1 style="color: #008604"> Welcome To Metal Trading Management System</h1>
      </el-col>
    </el-row>
    <el-row gutter="20" class="echart-row" type="flex" justify="center">
      <el-col :span="11" class="echart-container" >
        <PieChart :data="genderData" title="User Gender Distribution" chartName="gender type"/>
      </el-col>
      <el-col :span="11" class="echart-container">
        <PieChart :data="goldData" title="Types of gold products" chartName="gold type"/>
      </el-col>
    </el-row>
    <el-row gutter="20" class="echart-row" type="flex" justify="center">
      <el-col :span="11" class="echart-container">
        <PieChart :data="sliverData" title="Types of silver products" chartName="sliver type"/>
      </el-col>
      <el-col :span="11" class="echart-container">
        <BarChart :data="barChartData" title="Top search product"/>
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
      goldData: [],
      sliverData: [],
      barChartData: []
    };
  },

  mounted() {
    this.getGenderStatistic();
    this.getProductStatistic(1);
    this.getProductStatistic(2);
    this.getHotProductStatistic();
  },
  methods: {
    fetchGenderData() {
      // 调用API获取数据的代码
      // 假设这是从API获取的数据
      // this.barChartData = [
      //   { name: '产品A', value: 20 },
      //   { name: '产品B', value: 50 },
      //   { name: '产品C', value: 30 },
      //   // ...其他数据
      // ];


    },
    transformGenderData(data) {
      const genderMap = {
        '1': 'Male',
        '2': 'Female',
        '0': 'Unknown'
      };

      return data.map(item => ({
        ...item,
        name: genderMap[item.name] || item.name
      }));
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
              this.genderData =  this.transformGenderData(res.data)

            } else {
              alert("failed to get the data")
            }
          })
    },

    getProductStatistic(category) {
      const token = store.getters.getToken;
      const config = {
        headers: {
          'Authorization': `Bearer ${token}` // 添加 Bearer token 请求头
        }
      };
      const requestParameter = {
          "category": category
      }

      this.$axios.get(this.$httpurl + '/public/admin/productStatistic', { params: requestParameter }, config )
          .then(res => res.data)
          .then(res => {

            if (res.code === 200) {

              if (category ===1){
                /**
                 * 金制品统计数据
                 */
                this.goldData = res.data;
              }else {
                this.sliverData = res.data;
              }


            } else {
              alert("failed to get the data")
            }
          })
    },

    getHotProductStatistic(){

      this.barChartData = [
        { name: '产品A', value: 20 },
        { name: '产品B', value: 50 },
        { name: '产品C', value: 30 },
        // ...其他数据
      ];

      this.$axios.get(this.$httpurl + '/public/admin/getHotProductStatistic')
          .then(res => res.data)
          .then(res => {

            if (res.code === 200) {

              this.barChartData =  res.data

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
