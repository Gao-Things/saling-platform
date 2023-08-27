<template>
    <div class= "flex-container" style="background-color: black;">  
      <div style="margin-top: 8px; cursor: pointer">
        <img src="../assets/CompanyLogo.png" style="width: 128px; height: 128px;">
      </div>
      <div class="CompanyName">CS Company
      </div>
      <div class="vertical-line">
      </div>
      <div>
      <select  class="custom-select"  v-model="Currency" >
        <!-- @change="handleCurrencyChange" -->
        <option value="" disabled selected>Select Currency</option>
        <option value="EUR">EUR</option>
        <option value="CNY">CNY</option>
        <option value="AUD">AUD</option>
      </select>
      <!-- <p>You selected: {{ selectedOption }}</p> -->
      </div>
      <div>
      <button class="custom-button" @click="handleButtonClick">
        <!-- @click="handleButtonClick" -->
        <img src="../assets/exchange.png" alt="Button Image" class="button-icon" />
         Convert
      </button>  
    </div>
    
      <button class="profile" >
       <img src="../assets/Profile.png">
       </button>
      
       <!-- <div class="horizontal-line"></div> -->
  
    </div>
  </template>

<script>
export default {
    name: "HeaderNav",
    data() {
        return {
            Currency: 'USD' // Initial selected option
        };
      },
    
    methods:{

        handleOptionChange() {
            // Do something when the option changes
        },
        handleButtonClick() {
            // Emit a custom event with the selected currency as payload
            this.$emit('currency-selected', this.Currency);
          }
        }
    }

</script>

<style>
.CompanyName{
  font-family: 'DM Sans';
  font-style: normal;
  font-weight: 700;
  font-size: 66px;
  line-height: 80px;
  display: flex;
  align-items: center;
  letter-spacing: -1px;
  color: #FFFFFF;
  width: 466px;
  height: 80px;
  left: 320px;
  top: 33px;
  position: absolute;
  }
  
  .vertical-line {
    position: absolute;
  width: 2px;
  height: 98px;
  left: 930px;
  top: 28px;
  background-color:#FFFFFF; /* Set the color of the line */
  }
  
  
  .custom-select {
    position: absolute;
    cursor: pointer;
    height: 40px;
    left: 1100px;
    top: -10px;
    background-color:#000000;
    margin-top: 50px;
    color:#FFFFFF;
  }
  
  .custom-select option[disabled]:first-child {
    color: white; /* Set placeholder color */
  }
  
  .custom-button {
  
  position: absolute;
  width: 95px;
  height: 70px;
  left: 1225px;
  top: 40px;
  font-family: 'Inter';
  font-style: normal;
  font-weight: 400;
  font-size: 16px;
  line-height: 19px;
  background-color:#000000;
  color: #FFFFFF;
  
  }
  
  .button-icon{
    width: 50px; /* Adjust the width to your desired size */
    height: auto; /* Maintain aspect ratio */
  
  }
  
  .profile{
  position: absolute;
  width: 90px;
  height: 80px;
  right:40px;
  top: 32px;
  background-color:#000000;
  }
  
  .horizontal-line {
  position: absolute;
  width: 1933px;
  height: 2px;
  left: 0px;
  top: 142px;
  
  border: 1px solid #FFFFFF;
  
  }
  
  .flex-container {
    display: flex;
    line-height: 60px；
    
  }
  
  </style>