package com.usyd.capstone.controller;

import com.usyd.capstone.common.DTO.Result;
import com.usyd.capstone.service.NormalUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/normal")
public class NormalUserController {

    @Autowired
    private NormalUserService normalUserService;

    @GetMapping("/hello")
    public String hello(){
        int a = 1;
        return "hello, normal user";
    }

    @PostMapping("/setPriceThreshold")
    public Result setPriceThreshold() {
        return normalUserService.setPriceThresholdSingle("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI5NDk3MzE5ODhAcXEuY29tIiwianRpIjoiMSIsInJvbGUiOiJST0xFX05PUk1BTCIsImlhdCI6MTY5NDQ1NjM4MiwiZXhwIjoxNjk0NTQyNzgyfQ.W73WkCd6unNAYiq40VazxVIYlIXq4X9QTBq2PztZqyY",
                1L,true, 1.00);
    }
}
