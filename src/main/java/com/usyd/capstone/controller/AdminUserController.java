package com.usyd.capstone.controller;

import com.usyd.capstone.common.DTO.Result;
import com.usyd.capstone.common.VO.AdminResetingPrice;
import com.usyd.capstone.common.VO.UserLogin;
import com.usyd.capstone.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminUserController {
    @GetMapping("/hello")
    public String hello(){
        return "hello, admin user";
    }

    @Autowired
    private AdminUserService adminUserService;

    @PostMapping("/resettingSingleProductPrice")
    public Result test(@RequestBody AdminResetingPrice adminResetingPrice)
    {
        return adminUserService.updateProductPrice(adminResetingPrice.getToken(),
                adminResetingPrice.getProductId(), adminResetingPrice.getProductPrice(), adminResetingPrice.getTurnOfRecord());
    }

}



