<template>
    <div>    
      <table class="custom-table">
          <tr>
            <!-- /* image */ -->
            <th colspan="2">Item Name</th> 
            <th>Price($)</th>
            <th></th>       
            <th>Converted price</th>
            <th>Growth</th>
          </tr>
          <tr v-for="item in tableData.productList" :key="item.id">
            <td><img src="../assets/CompanyLogo.png"></td>
            <td>{{ item.name }}</td>
            <td>{{ item.price }}</td>
            <td>Data 3</td>
          </tr>
          <!-- More rows... -->
        </table>
    
    </div>
    
    </template>
    
   
    
    <style scoped>
    .button-container {
      display: flex;
    }
    
    .button-spacing {
      margin-right: 5px; /* 调整按钮之间的间距 */
    }
    
    .custom-table {
      width: 100%;
      border-collapse: collapse;
      background-color: black; /* Set the background color to black */
    }
    
    .custom-table th, .custom-table td {
      border: 0px solid white; /* Set the border color to white */
      padding: 10px;
      text-align: center;
      color: white; /* Set the text color to white */
    }
    </style>



    <script>
    import HeaderNav from "./HeaderNav";
    import axios from 'axios';
   export default {
    name: "MainTable",
    data() {
        return {
            tableData: [],
            selectedCurrency: HeaderNav.data().Currency
        }
    },
    components: {
   
      },
    methods: {
        fetchCurrencyData() {
            if(this.selectedCurrency!=='USD'){
                axios.get(`http://localhost:8082/public/product/productList?targetCurrency=${this.selectedCurrency}`).then(res => res.data).then(res => {
                console.log(res)
                this.tableData = res.data;
            })  
            }
            else{
                    axios.get('http://localhost:8082/public/product/productList').then(res => res.data).then(res => {
                    console.log(res)
                    this.tableData = res.data;
            })
        }
    }
    },
    mounted(){
    // console.log(this.selectedCurrency);
    // this.fetchCurrencyData();
    console.log(this.tableData);
}

}
    </script>


  