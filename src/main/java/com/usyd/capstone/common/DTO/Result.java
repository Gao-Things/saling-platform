package com.usyd.capstone.common.DTO;

import lombok.Data;

@Data
public class Result {
    private int code; // status code
    private String msg; // return message
    private Long total;  // total number
    private Object data; // data
    private String JWTToken; //

    public static Result fail(){
        return new Result(400, "fail", 0L, null, "");
    }
    public static Result fail(Object data){
        return new Result(400, "fail", 0L, data, "");
    }
    public static Result suc(){
        return new Result(200, "successful", 0L, null, "");
    }
    public static Result suc(Object data){
        return new Result(200, "successful", 0L, data, "");
    }
    public static Result suc(Object data, Long total){
        return new Result(200, "successful", total, data, "");
    }

    public Result(int code, String msg, Long total, Object data, String JWTToken){
        this.code = code;
        this.msg = msg;
        this.total = total;
        this.data = data;
        this.JWTToken = JWTToken;
    }
}
