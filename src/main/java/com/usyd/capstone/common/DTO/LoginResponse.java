package com.usyd.capstone.common.DTO;

import lombok.Data;

@Data
public class LoginResponse {
    private Long id;
    private int role; //1 = normal user 2 = admin user 3 = super user
//    private String email;
}
