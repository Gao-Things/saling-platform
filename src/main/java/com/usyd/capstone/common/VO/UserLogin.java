package com.usyd.capstone.common.VO;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class UserLogin {
    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;

    @JsonProperty("userRole")
    private int userRole; //1 = normal user 2 = admin user 3 = super user
}

