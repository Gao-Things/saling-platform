export default {
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