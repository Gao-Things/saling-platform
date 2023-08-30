<template>
  <!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
    <div id="chartLine" class="line-wrap"></div>
</template>

<script>
import * as echarts from 'echarts';
require('echarts/theme/shine');//引入主题

export default {

    data() {
        return {
            chartLine: null
        }
    },
    props: {
      categories: Array, // x轴数据
      values: Array,      // y轴数据
      color: String
    },
    watch: {
      $props: {
        deep: true,
        immediate: true,
        handler(newProps, oldProps) {
          // Compare the newProps with oldProps to determine if the data has changed
          if (!this.isEqual(newProps, oldProps)) {
            // Update your chart data or trigger re-rendering logic here
            this.updateChart(); // Call the function to update the chart
          }
        }
      }
    },
    mounted() {
        this.$nextTick(() => {
            this.drawLineChart();
        })
    },
    methods: {
        drawLineChart() {
            this.chartLine = echarts.init(this.$el,'shine');// 基于准备好的dom，初始化echarts实例

          // Convert timestamp to formatted date string for x-axis labels
            const formattedCategories = this.categories.map(timestamp => {
              const date = new Date(timestamp * 1000);
              return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')}`;
            });
            let option = {
                tooltip : {
                    trigger: 'axis'
                },
                calculable : true,
                xAxis : [
                    {
                        type : 'category',
                        axisLine: {show:false},//不显示坐标轴
                        boundaryGap : false,
                        axisTick: {
                            show: false
                        },
                        splitLine: {
                            show: false,
                        },
                        show: false,
                        axisLabel:{ interval:0 },
                        data : formattedCategories
                    }
                ],
                yAxis : [
                    {
                        type : 'value',
                        axisTick: {
                            show: false
                        },
                        axisLine: {
                            show: false,
                        },
                        minorTick: {
                            show: false,
                        },
                        splitLine: {
                            show: false,
                        },
                        show: false,
                        name: '（人）'
                    }
                ],
                series : [
                    {
                        type:'line',
                        data: this.values,
                      // 修改折线颜色
                      itemStyle: {
                        color: this.color  // 在这里设置你想要的颜色
                      }
                    },

                ]
            };
            // 使用刚指定的配置项和数据显示图表
            this.chartLine.setOption(option);
        }
    }
}
</script>

<style>
.line-wrap{
  width:100%;
  height:200px;
}
</style>