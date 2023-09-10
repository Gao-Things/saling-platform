<template>
    <div class="background-email">
         <div class="set">Set new password
        </div>
        <div  class="hint">Enter your new password below and check the hint while setting it
        </div>
        <div>
            <input  placeholder="  Input password here" class="password"/>          
            <a href="#" class="forget">Forget password</a>
            <input  placeholder=" Comfirm password" class="comfirm"/>
            <el-button type="success" round style="position: absolute;width: 350px;height: 54px;left: 770px;top:650px;font-size: 18px;">send</el-button>
        </div>
    </div>
</template>


<script src="./emailPsw.js">
</script>

<style>
.background-email{
    background-color: #FDFFF9;
}
.set{
position: absolute;
width: 793px;
height: 38px;
left: 500px;
top: 207px;
font-family: 'Abel';
font-style: normal;
font-weight: 400;
font-size: 50px;
line-height: 38px;
/* identical to box height, or 40% */
text-align: center;
color: #000000;
}
.hint{
    /* Enter your new password below and check the hint while setting it. */
position: absolute;
width: 1039px;
height: 57px;
left: 500px;
top: 348px;

font-family: 'Abel';
font-style: normal;
font-weight: 400;
font-size: 25px;
line-height: 20px;
/* or 50% */
color: #000000;

}
.forget{
    /* Forgot password? */
position: absolute;
width: 200px;
height: 18px;
left: 1010px;
top: 518px;

font-family: 'Shippori Mincho';
font-style: normal;
font-weight: 700;
font-size: 20px;
line-height: 18px;
/* identical to box height, or 90% */
text-decoration-line: underline;

/* green */
color: #437D28;


}
.comfirm{
    /* Rectangle 1047 */

box-sizing: border-box;

position: absolute;
width: 478px;
height: 54px;
left: 700px;
top: 557px;

background: #e3eee2;
border: 1px solid #637A30;
box-shadow: 0px 0px 10px rgba(106, 100, 173, 0.1);
border-radius: 4px;

}
.password{
    background-color: #e3eee2;
    position: absolute;
    width: 478px;
    height: 54px;left: 700px;
    top: 440px;
    background: #e3eee2;
    border: 1px solid #637A30;
    box-shadow: 0px 0px 10px rgba(106, 100, 173, 0.1);
    border-radius: 4px;
}



</style>



