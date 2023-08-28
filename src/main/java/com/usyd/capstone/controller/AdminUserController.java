package com.usyd.capstone.controller;

import com.usyd.capstone.common.DTO.Result;
import com.usyd.capstone.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminUserController {
    @GetMapping("/hello")
    public String hello(){
        return "hello, admin user";
    }

    @Autowired
    private AdminUserService adminUserService;

    @GetMapping("/test")
    public Result test(String token, Long productId, double productPrice, int turnOfRecord)
    {
        return adminUserService.updateProductPrice(token, productId, productPrice, turnOfRecord);
    }

}



