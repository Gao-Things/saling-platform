package com.usyd.capstone.common.DTO;

import lombok.Data;

@Data
public class RecaptchaResponse {
    private boolean success;
    private String challenge_ts;
    private String hostname;
    // ... 其他属性和 getter/setter 方法 ...
}