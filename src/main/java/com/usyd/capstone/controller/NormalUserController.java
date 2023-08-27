package com.usyd.capstone.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/normal")
public class NormalUserController {

    @GetMapping("/hello")
    public String hello(){
        int a = 1;
        return "hello, normal user";
    }
}
