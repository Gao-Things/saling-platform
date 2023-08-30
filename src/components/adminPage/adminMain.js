
import lineChart from "@/components/chart/lineChart.vue";
import store from '../../store'; // 导入Vuex store
export default {
    name: "adminMain.vue",
    components: {lineChart},
    data() {
        return {
            id:null,
            tableData: [],
            totalItems: 1000, // 总记录数
            pageSize: 5, // 每页显示条数
            currentPage: 1, // 当前页码
            useValue:"",
            dialogVisible: false,
            itemTurnOfRecord: null,
            price: null,
            // chartCategories: ['8/7', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14'],
            // chartValues: [120, 132, 101, 134, 90, 230, 210, 120, 132, 101, 134, 90, 230, 210]
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
        openDialog(id, currentTurnOfRecord) {
            this.dialogVisible = true;
            this.id = id; // Set the id value for later use in savePrice
            this.itemTurnOfRecord = currentTurnOfRecord;
        },
        closeDialog() {
            this.dialogVisible = false;
            this.price = '';
        },
        savePrice() {
            const token = store.getters.getToken;
            // Do modify price with the entered price (this.price)
            // console.log("Saving price for id:", this.id);
            // console.log("Saving price for id:", this.itemTurnOfRecord);
            // console.log("Saving price for id:", token);
            const resettingPriceForm = {
                "token": token,
                "productId": this.id,
                "productPrice": parseFloat(this.price),
                "turnOfRecord": this.itemTurnOfRecord+1
            }

            const config = {
                headers: {
                    'Authorization': `Bearer ${token}` // 添加 Bearer token 请求头
                }
            };
            this.$axios.post(this.$httpurl + '/admin/resettingSingleProductPrice', resettingPriceForm, config)
                .then(res => res.data)
                .then(res => {
                    console.log(res);
                    if (res.code === 200) {

                        this.$message({
                            message: 'Proposal submitted successfully !',
                            type: 'success' // 设置消息类型，可以是success、warning、info、error等
                        })

                    } else {
                        this.$message({
                            message: 'Proposal submitted fail ',
                            type: 'error' // 设置消息类型，可以是success、warning、info、error等
                        })
                    }
                });
            this.closeDialog();
        },
        loadGet(queryParams) {
            this.$axios.get(this.$httpurl + '/public/product/productList', { params: queryParams }).then(res => res.data).then(res => {

                if (res.code === 200) {

                    this.totalItems = res.data.ProductList.total
                    console.log(res.data.ProductList.records)
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

                } else {
                    alert("failed to get the data")
                }
            })
        },

        getRowClassName(row) {
            if (row.row.product.inResettingProcess) {
                return 'red-row'; // Apply "red-row" class to the row
            }
            return 'normal-row'; // Default class
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
