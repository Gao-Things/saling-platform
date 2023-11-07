<template>
  <div ref="pieChart" class="echart"></div>
</template>

<script>
import * as echarts from 'echarts';

export default {
  name: 'PieChart',
  props: {
    data: Array,
    title: String,
    chartName: String // 添加新的prop
  },
  mounted() {
    this.initPieChart();
  },
  // 在PieChart.vue组件中
  watch: {
    data(newData) {
      this.initPieChart(); // 当数据更新时重新初始化图表
    }
  },

  methods: {
    initPieChart() {
      const chart = echarts.init(this.$refs.pieChart);
      chart.setOption(this.getPieOption());
    },
    getPieOption() {
      return {
        title: {
          text: this.title,
          left: 'center'
        },
        tooltip: {
          trigger: 'item'
        },
        legend: {
          top: '5%',
          left: 'center'
        },
        series: [
          {
            name: this.chartName,
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '18',
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: this.data
          }
        ]
      };
    }
  }
};
</script>

<style scoped>
.echart {
  width: 100%;
  height: 100%;
}
</style>
