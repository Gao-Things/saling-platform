package com.usyd.capstone.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.usyd.capstone.common.QueryPageParam;
import com.usyd.capstone.common.Result;
import com.usyd.capstone.entity.DTO.RecaptchaResponse;
import com.usyd.capstone.entity.User;
import com.usyd.capstone.entity.VO.UserLogin;
import com.usyd.capstone.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/user")
public class userController {

    @Value("${recaptcha.secretKey}")
    private String recaptchaSecretKey;
    @GetMapping
    public String hello(){
        return "hello";
    }

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result userLogin(@RequestBody UserLogin userLogin){
        System.out.println(userLogin);
        return userService.verifyLogin(userLogin);

    }
    //The token will from the mobile
    @PostMapping("/reCAPTCHA")
    public Result submitForm(@RequestBody String recaptchaResponse) {
        // Validate reCAPTCHA response in the backend
        boolean isRecaptchaValid = validateRecaptcha(recaptchaResponse);

        if (isRecaptchaValid) {
            // Perform further processing or save data
            return Result.suc("reCAPTCHA verification successes");
        } else {
            return Result.fail("reCAPTCHA verification failed");
        }
    }

    private boolean validateRecaptcha(String recaptchaResponse) {
        String url = "https://www.google.com/recaptcha/api/siteverify";
        String params = "?secret=" + recaptchaSecretKey + "&response=" + recaptchaResponse;

        RestTemplate restTemplate = new RestTemplate();
        RecaptchaResponse response = restTemplate.getForObject(url + params, RecaptchaResponse.class);

        assert response != null;
        return response.isSuccess();
    }



    @GetMapping("/List")
    public List<User> list(){
        return userService.listAll();
    }

    @PostMapping("/save")
    public Boolean save(@RequestBody User user){
        return userService.save(user);
    }
    // 模糊查询
    @PostMapping("/ListP")
    public Result listP(@RequestBody User user){
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(user.getName())){
            lambdaQueryWrapper.like(User::getName, user.getName());
        }

        return  Result.suc(userService.list(lambdaQueryWrapper));
    }



    @PostMapping("/lispage")
    public List<User> lispage(@RequestBody QueryPageParam query){

        HashMap param = query.getParam();

        Page<User> page = new Page<>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(User::getName, param.get("name"));
        IPage result = userService.page(page, lambdaQueryWrapper);

        System.out.println(result.getTotal());
        return result.getRecords();
    }


    @PostMapping("/lispageC")
    public List<User> lispageC(@RequestBody QueryPageParam query){

        HashMap param = query.getParam();

        Page<User> page = new Page<>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());
//
//        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        lambdaQueryWrapper.like(User::getName, param.get("name"));
        IPage result = userService.pageC(page);

        System.out.println(result.getTotal());
        return result.getRecords();
    }


    @PostMapping("/lispageCC")
    public Result lispageCC(@RequestBody QueryPageParam query){

        HashMap param = query.getParam();

        Page<User> page = new Page<>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(User::getName, param.get("name"));
        // 传入wrapper筛选条件
        IPage result = userService.pageCC(page, lambdaQueryWrapper);

        System.out.println(result.getTotal());
        return Result.suc(result.getRecords(), result.getTotal());
    }



}
