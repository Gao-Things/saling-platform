package com.usyd.capstone.common.VO;

import lombok.Data;

@Data
public class UserVO {
    private Integer userId;
    private String name;
    private String email;
    private Integer gender;
    private boolean activationStatus;
}
