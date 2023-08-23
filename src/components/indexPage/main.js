export default {
    name: "Main.vue",
    data() {
        return {
            tableData: []
        }
    },
    methods: {
        loadGet() {
            this.$axios.get(this.$httpurl + '/user/List').then(res => res.data).then(res => {
                console.log(res)
                this.tableData = res;
            })
        },
        loadPost() {
            this.$axios.post(this.$httpurl + '/user/ListP', {}).then(res => res.data).then(res => {
                console.log(res)
                if (res.code === 200) {
                    this.tableData = res.data;
                } else {
                    alert("failed to get the data")
                }
            })
        },
    },
    beforeMount() {
        this.loadPost();
    }
}
