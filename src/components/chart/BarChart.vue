<template>
  <div ref="barChart" class="echart"></div>
</template>

<script>
import * as echarts from 'echarts';

export default {
  name: 'BarChart',
  props: {
    data: Array,
    title: String
  },
  mounted() {
    this.initBarChart();
  },
  watch: {
    data(newData) {
      this.initBarChart(); // 当数据更新时重新初始化图表
    }
  },
  methods: {
    initBarChart() {
      const chart = echarts.init(this.$refs.barChart);
      chart.setOption(this.getBarOption());
    },
    getBarOption() {
      return {
        title: {
          text: this.title,
          left: 'center'
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        xAxis: {
          type: 'category',
          data: this.data.map(item => item.name),
          axisTick: {
            alignWithLabel: true
          }
        },
        yAxis: {
          type: 'value'
        },
        series: [{
          data: this.data.map(item => item.value),
          type: 'bar',
          barWidth: '60%',
          itemStyle: {
            color: '#3398DB'
          }
        }]
      };
    }
  }
};
</script>

<style scoped>
.echart {
  width: 100%;
  height: 350px; /* You can adjust based on your requirement */
}
</style>
