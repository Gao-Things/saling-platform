import * as echarts from 'echarts';

export default {
    name: 'Dashboard',
    mounted() {
        this.initPieChart('pie-chart1');
        this.initPieChart('pie-chart2');
        this.initPieChart('pie-chart3');
        this.initBarChart('bar-chart');
    },
    methods: {
        initPieChart(elementId) {
            const pieChart = echarts.init(document.getElementById(elementId));
            const option = {
                // ECharts 饼图的配置选项
            };
            pieChart.setOption(option);
        },
        initBarChart(elementId) {
            const barChart = echarts.init(document.getElementById(elementId));
            const option = {
                // ECharts 柱状图的配置选项
            };
            barChart.setOption(option);
        }
    }
}