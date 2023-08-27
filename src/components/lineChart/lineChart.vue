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
    mounted() {
        this.$nextTick(() => {
            this.drawLineChart();
        })
    },
    methods: {
        drawLineChart() {
            this.chartLine = echarts.init(this.$el,'shine');// 基于准备好的dom，初始化echarts实例
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
                        data : ['1','2','3','4','5','6','7','8','9','10','11','12','13','14']
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
                        data:[120, 132, 101, 134, 90, 230, 210,120, 132, 101, 134, 90, 230, 210]
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