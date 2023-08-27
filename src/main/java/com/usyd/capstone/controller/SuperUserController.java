package com.usyd.capstone.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/super")
public class SuperUserController {

    @GetMapping("/hello")
    public String hello(){
        int a = 1;
        return "hello, super user";
    }
}
