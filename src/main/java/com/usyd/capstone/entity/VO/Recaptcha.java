package com.usyd.capstone.entity.VO;

import lombok.Data;

@Data
public class Recaptcha {
    private String token;
    private String expectedAction;
}