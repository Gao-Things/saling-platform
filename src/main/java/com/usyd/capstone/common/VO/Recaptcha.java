package com.usyd.capstone.common.VO;

import lombok.Data;

@Data
public class Recaptcha {
    private String token;
    private String expectedAction;
}