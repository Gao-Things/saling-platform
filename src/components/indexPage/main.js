
import lineChart from "@/components/chart/lineChart.vue";
export default {
    name: "Main.vue",
    components: {lineChart},
    data() {
        return {
            tableData: [],
            totalItems: 1000, // 总记录数
            pageSize: 5, // 每页显示条数
            currentPage: 1, // 当前页码
            useValue:"",
        }
    },
    props:{
        exchangeValue:String
    },
    watch: {
        exchangeValue(newValue) {
            // 在属性值变化时执行的逻辑
            this.exchangeValue = newValue
            console.log( this.exchangeValue)
            this.loadGet({ pageNum: this.currentPage, pageSize: this.pageSize, targetCurrency:  this.exchangeValue});
        }
    },
    methods: {
        loadGet(queryParams) {
            this.$axios.get(this.$httpurl + '/public/product/productList', { params: queryParams }).then(res => res.data).then(res => {

                if (res.code === 200) {
                    console.log(res.data)
                    this.totalItems = res.data.ProductList.total
                    // 将数据加载到组件的数据属性中
                    this.tableData = res.data.ProductList.records.map(item => {
                        const isoDateString = item.productUpdateTime; // 假设时间戳字段名为 timestamp
                        const isoDate = new Date(isoDateString);
                        const year = isoDate.getFullYear();
                        const month = String(isoDate.getMonth() + 1).padStart(2, "0");
                        const day = String(isoDate.getDate()).padStart(2, "0");
                        const hours = String(isoDate.getHours()).padStart(2, "0");
                        const minutes = String(isoDate.getMinutes()).padStart(2, "0");
                        const seconds = String(isoDate.getSeconds()).padStart(2, "0");

                        const formattedDate = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;

                        return {
                            ...item,
                            formattedTimestamp: formattedDate // 将格式化后的时间添加到数据项中
                        };
                    });

                    console.log(this.tableData);
                } else {
                    alert("failed to get the data")
                }
            })
        },

        handleSizeChange(newSize) {
            this.pageSize = newSize;
            // 重新获取数据
            this.loadGet({ pageNum: this.currentPage, pageSize: this.pageSize, targetCurrency:this.exchangeValue});
        },
        handleCurrentChange(newPage) {
            this.currentPage = newPage;
            // 重新获取数据
            this.loadGet({ pageNum: this.currentPage, pageSize: this.pageSize, targetCurrency:this.exchangeValue});
        },

    },
    beforeMount() {
        this.loadGet({ pageNum: this.currentPage, pageSize: this.pageSize, targetCurrency:this.exchangeValue});
    },


}
