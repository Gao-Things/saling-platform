package com.usyd.capstone.entity.VO;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class UserLogin {
    // 变量名要小写
    private String email;

    private String password;
}

