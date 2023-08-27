export default {
    data() {
        return {
            selectedValue: 'USD', // 存储选择的值
        };
    },
    name: "Header.vue",
    props:{
        icon:String
    },
    methods:{
        toUser(){
            console.log("aaaaaaaa")
        },
        collapse(){
            this.$emit('doCollapse')
        }
    }
}