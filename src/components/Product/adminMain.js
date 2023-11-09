
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
            itemTurnOfRecord: null,
            price: null,
            categoryFilters: [
                { text: 'Gold', value: '1' },
                { text: 'Silver', value: '2' }
            ],
            statusFilters: [
                { text: 'Open', value: 0 },
                { text: 'Closed', value: 1 },
                { text: 'Sold', value: 2 },
                { text: 'Cancelled', value: 3 }
            ],
            dialogVisible: false,
            editingProduct: {}, // 存储当前正在编辑的产品信息
            // chartCategories: ['8/7', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14'],
            // chartValues: [120, 132, 101, 134, 90, 230, 210, 120, 132, 101, 134, 90, 230, 210]
        }
    },
    props:{
        exchangeValue: {
            type: String,
            default: ""
        }
    },
    watch: {
        exchangeValue(newValue) {
            // 搜索的value
            this.exchangeValue = newValue
            this.loadGet({ pageNum: this.currentPage, pageSize: this.pageSize, searchValue: this.exchangeValue});
            console.log("真督"+ this.exchangeValue)

        }
    },
    methods: {
        handleRowClick(row, event, column) {
            // 检查是否点击的是操作列，如果不是，则进行跳转
            if (column.property !== 'operation') {
                this.$router.push({ name: 'offer', params: { productId: row.id }});
            }
        },
        confirmDelete(row) {
            this.$confirm('Are you sure you want to delete this item?', 'Warning', {
                confirmButtonText: 'OK',
                cancelButtonText: 'Cancel',
                type: 'warning'
            }).then(() => {
                this.deleteProduct(row.id);
            }).catch(() => {
                console.log('Delete action canceled');
            });
        },
        deleteProduct(productId) {
            // 在这里执行删除操作，通常是发送一个请求到后端API
            console.log('Product deleted:', productId);

            const queryParams = {
                productID: productId
            }
            this.$axios.get(this.$httpurl + '/public/admin/adminDeleteProduct', { params: queryParams })
                .then(res => res.data)
                .then(res => {

                if (res.code === 200) {
                    this.loadGet({ pageNum: this.currentPage, pageSize: this.pageSize, searchValue: this.exchangeValue});
                    this.$message.success("Delete product successful");

                } else {
                    alert("failed to get the data")
                }
            })
        },
        openDialog(row) {
            this.editingProduct = { ...row }; // 复制行数据到编辑对象
            this.dialogVisible = true; // 显示对话框
        },
        saveProduct() {
            // 处理保存逻辑
            // 可以调用API更新产品信息，然后刷新表格数据
            this.$confirm('Are you sure you want to save these changes?', 'Confirmation', {
                confirmButtonText: 'Yes',
                cancelButtonText: 'No',
                type: 'warning'
            }).then(() => {
                // 用户点击了确认按钮，调用实际的保存逻辑
                this.actualSaveProduct();
            }).catch(() => {
                // 用户点击了取消按钮或关闭了弹窗
                console.log('Save cancelled');
            });

        },

        prepareDataForBackend(frontendData) {
            // 创建一个新的对象来匹配后端期望的参数格式
            const backendData = {
                productId: frontendData.id,
                category: frontendData.category, // 或者 frontendData.category，根据后端的实际要求
                itemTitle: frontendData.productName,
                itemDescription: frontendData.productDescription,
                itemWeight: frontendData.productWeight,
                productStatus: frontendData.productStatus,
                // 假设后端需要单个图片URL作为字符串，而不是数组
                imageUrl: frontendData.productImage,
                userId: frontendData.ownerId,
                hiddenPrice: frontendData.priceStatus, // 这里假设priceStatus是hiddenPrice，根据实际调整
                itemPurity: frontendData.purity,
                itemPrice: frontendData.productPrice,
            };

            // 返回处理后的数据
            return backendData;
        },

        actualSaveProduct() {
            let loadingInstance = this.$loading({
                lock: true,
                text: 'Loading...',
                spinner: 'el-icon-loading',
                background: 'rgba(0, 0, 0, 0.4)'
            });

            // 实际的保存逻辑
            console.log('Saving product:', this.editingProduct);
            // 您需要在这里实现实际的保存逻辑，比如发送请求到后端API
            const token = store.getters.getToken;
            const config = {
                headers: {
                    'Authorization': `Bearer ${token}` // 添加 Bearer token 请求头
                }
            };

            this.$axios.post(this.$httpurl + '/public/admin/adminUploadProduct', this.prepareDataForBackend(this.editingProduct), config)
                .then(res => res.data)
                .then(res => {
                    console.log(res);
                    if (res.code === 200) {
                        // 关闭 Loading
                        loadingInstance.close();
                        this.loadGet({ pageNum: this.currentPage, pageSize: this.pageSize, searchValue: this.exchangeValue});
                        this.$message({
                            message: 'Product modify successfully !',
                            type: 'success' // 设置消息类型，可以是success、warning、info、error等
                        })

                    } else {
                        this.$message({
                            message: 'Product modify fail ',
                            type: 'error' // 设置消息类型，可以是success、warning、info、error等
                        })
                    }
                });



            this.dialogVisible = false; // 保存后关闭对话框
        },
        closeDialog() {
            this.dialogVisible = false; // 关闭对话框
        },
        formatDate(timestamp) {
            const date = new Date(timestamp);
            return `${date.getFullYear()}-${date.getMonth() + 1}-${date.getDate()} ${date.getHours()}:${date.getMinutes()}:${date.getSeconds()}`;
        },
        handleSortChange({ prop, order }) {
            if (prop === 'offerNumber') {
                console.log("调用了吗")
                this.sortOffersByNumber(order);
            }
        },
        sortOffersByNumber(order) {
            this.tableData.sort((a, b) => {
                const lengthA = a.offers ? a.offers.length : 0;
                const lengthB = b.offers ? b.offers.length : 0;
                if (order === 'ascending') {
                    return lengthA - lengthB;
                } else if (order === 'descending') {
                    return lengthB - lengthA;
                }
                return 0;
            });
        },
        filterStatus(value, row) {
            // value 是筛选器选项的值，row.productStatus 是行数据中的状态值
            return row.productStatus == value;
        },
        filterCategory(value, row) {
            // 确保这个方法返回一个布尔值
            console.log(row.category)
            console.log(value)
            return row.category == value;
        },

        loadGet(queryParams) {
            const token = store.getters.getToken;
            const config = {
                headers: {
                    'Authorization': `Bearer ${token}` // 添加 Bearer token 请求头
                }
            };
            // 开启全局 Loading
            let loadingInstance = this.$loading({
                lock: true,
                text: 'Loading...',
                spinner: 'el-icon-loading',
                background: 'rgba(0, 0, 0, 0.4)'
            });

            this.$axios.get(this.$httpurl + '/public/admin/productListAdmin', { params: queryParams }, config)
                .then(res => res.data)
                .then(res => {

                if (res.code === 200) {
                    // 关闭 Loading
                    loadingInstance.close();
                    this.tableData = res.data.records
                    this.totalItems = res.data.total
                    console.log("wdfffffffffff");
                    console.log(this.tableData);


                } else {
                    alert("failed to get the data")
                }
            })
        },

        filterHandler(value, row, column) {
            // 筛选逻辑
            // value: 选定的筛选项的值
            // row: 当前行的数据
            // column: 当前列的定义
            const property = column['property'];
            return row[property] == value;
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
            this.loadGet({ pageNum: this.currentPage, pageSize: this.pageSize, searchValue: this.exchangeValue});
        },
        handleCurrentChange(newPage) {
            this.currentPage = newPage;
            // 重新获取数据
            this.loadGet({ pageNum: this.currentPage, pageSize: this.pageSize, searchValue: this.exchangeValue});
        },

        getImageUrl(imageString) {
            // 假设imageString是形如"[image1.jpg, image2.jpg]"的字符串
            // 首先去除字符串开头的'['和结尾的']'，然后分割字符串得到数组
            const trimmedString = imageString.substring(1, imageString.length - 1);
            const imagesArray = trimmedString.split(', ');
            // 获取数组中的第一个元素，即第一张图片的文件名

            const imageUrl = this.$imageurl + imagesArray[0].trim();

            console.log(imageUrl)
            return imageUrl;
        },

        getImageUrls(imageString) {
            if (!imageString) return [];
            const trimmedString = imageString.substring(1, imageString.length - 1);
            return trimmedString.split(', ').map(filename => ({
                url: this.$imageurl + filename.trim(),
                filename: filename.trim()
            }));
        },
        removeImage(filename) {
            let imagesArray = this.getImageUrls(this.editingProduct.productImage);
            imagesArray = imagesArray.filter(image => image.filename !== filename);
            // 转换回字符串格式，如："[image1.jpg, image2.jpg]"
            this.editingProduct.productImage = '[' + imagesArray.map(image => image.filename).join(', ') + ']';
        }


    },
    beforeMount() {
        this.loadGet({ pageNum: this.currentPage, pageSize: this.pageSize, searchValue: "" });
    },


}
