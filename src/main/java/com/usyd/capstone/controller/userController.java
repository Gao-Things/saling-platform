package com.usyd.capstone.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.usyd.capstone.common.QueryPageParam;
import com.usyd.capstone.common.Result;
import com.usyd.capstone.entity.DTO.RecaptchaResponse;
import com.usyd.capstone.entity.User;
import com.usyd.capstone.entity.VO.Recaptcha;
import com.usyd.capstone.entity.VO.UserLogin;
import com.usyd.capstone.entity.VO.UserRegistration;
import com.usyd.capstone.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/user")
public class userController {

    @Value("${recaptcha.secretKey}")
    private String recaptchaSecretKey;
    @GetMapping("/hello")
    public String hello(){
        int a = 1;
        return "hello";
    }

    @Autowired
    private UserService userService;



    //The token will from the mobile
    @PostMapping("/reCAPTCHA")
    public Result submitForm(@RequestBody Recaptcha recaptcha) {
        // Validate reCAPTCHA response in the backend
        boolean isRecaptchaValid = validateRecaptcha(recaptcha);

        if (isRecaptchaValid) {

            // Perform further processing or save data
            return Result.suc("reCAPTCHA verification successes");
        } else {
            return Result.fail("reCAPTCHA verification failed");
        }
    }

    private boolean validateRecaptcha( Recaptcha recaptcha) {

        // 创建一个JSON对象
        JSONObject eventObject = new JSONObject();
        eventObject.put("token", recaptcha.getToken());
        eventObject.put("siteKey", "6Lf8Y7onAAAAACsaI8NLwElJ1d_Z9pB9CQGUlEO6");
        eventObject.put("expectedAction", recaptcha.getExpectedAction());

        // 将JSON对象添加到包含"event"字段的外部对象
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("event", eventObject);

        // 将JSONObject转换成JSON格式的字符串
        String jsonRequestBody = jsonObject.toJSONString();

        // API URL
        String apiUrl = "https://recaptchaenterprise.googleapis.com/v1/projects/my-project-54012-1692413464721/assessments?key=AIzaSyDM_dL6KmNoXYqXsAR8HFsYAftHpIVk4Mg";

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create the request entity with headers and body
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonRequestBody, headers);

        // Create a RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();

        // Send the POST request
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);

        // Handle the response as needed
        if (response.getStatusCode() == HttpStatus.OK) {


            String json =  response.getBody();

            JSONObject jsonObject1 = JSONObject.parseObject(json);
            JSONObject riskAnalysis = jsonObject1.getJSONObject("riskAnalysis");
            double score = riskAnalysis.getDoubleValue("score");
            System.out.println("Score: " + score);
            return !(score <= 0.2);

        } else {
            System.out.println("Request failed with status: " + response.getStatusCode());
            return false;
        }
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

    @PostMapping("/registration")
    public Result register(@RequestBody UserRegistration userRegistration){
        return userService.registration(userRegistration.getEmail(), userRegistration.getPassword());
    }

    @GetMapping("/registrationVerification")
    public Result registrationVerification(@RequestParam("email") String email, @RequestParam("registrationTimestamp")
            long registrationTimestamp, @RequestParam("passwordToken") String passwordToken){
        return userService.registrationVerification(email, registrationTimestamp, passwordToken);
    }
}
