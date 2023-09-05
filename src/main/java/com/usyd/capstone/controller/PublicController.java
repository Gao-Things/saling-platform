package com.usyd.capstone.controller;

import com.usyd.capstone.common.DTO.Result;
import com.usyd.capstone.common.VO.*;
import com.usyd.capstone.common.utils.ValidateRecaptcha;
import com.usyd.capstone.service.AdminUserService;
import com.usyd.capstone.service.PublicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {
    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }

    @Autowired
    private PublicService publicService;

    @Autowired
    private ValidateRecaptcha validateRecaptcha;

    @PostMapping("/login")
    public Result userLogin(@RequestBody UserLogin userLogin){
        System.out.println(userLogin);
        return publicService.verifyLogin(userLogin);

    }
    //The token will from the mobile
    @PostMapping("/reCAPTCHA")
    public Result submitForm(@RequestBody Recaptcha recaptcha) {
        // Validate reCAPTCHA response in the backend
        boolean isRecaptchaValid = validateRecaptcha.validate(recaptcha);

        if (isRecaptchaValid) {

            // Perform further processing or save data
            return Result.suc("reCAPTCHA verification successes");
        } else {
            return Result.fail("reCAPTCHA verification failed");
        }
    }

    @PostMapping("/forgetPassword")
    public Result forgetPassword(@RequestBody EmailAddress emailAddress){

        return publicService.forgetPassword(emailAddress);

    }

    @GetMapping("/forgetPasswordVerification")
    public Result forgetPasswordVerification(@RequestParam("email") String email, @RequestParam("resettingPasswordTimestamp")
    long resettingPasswordTimestamp){
        return publicService.forgetPasswordVerification(email, resettingPasswordTimestamp);
    }

    @GetMapping("/pollingResult")
    public Result pollingResult(@RequestParam("email") String email){

        return publicService.pollingResult(email);
    }


    // TODO 需要完善加个token
    @PostMapping("/updatePassword")
    public Result updatePassword(@RequestBody UpdatePasswordParameter updatePasswordParameter){

        return publicService.updatePassword(updatePasswordParameter);

    }


//    @GetMapping("/List")
//    public List<User> list(){
//        return userService.listAll();
//    }

//    @PostMapping("/save")
//    public Boolean save(@RequestBody User user){
//        return userService.save(user);
//    }
    // 模糊查询
//    @PostMapping("/ListP")
//    public Result listP(@RequestBody User user){
//        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        if(StringUtils.isNotBlank(user.getName())){
//            lambdaQueryWrapper.like(User::getName, user.getName());
//        }
//
//        return  Result.suc(userService.list(lambdaQueryWrapper));
//    }



//    @PostMapping("/lispage")
//    public List<User> lispage(@RequestBody QueryPageParam query){
//
//        HashMap param = query.getParam();
//
//        Page<User> page = new Page<>();
//        page.setCurrent(query.getPageNum());
//        page.setSize(query.getPageSize());
//        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        lambdaQueryWrapper.like(User::getName, param.get("name"));
//        IPage result = userService.page(page, lambdaQueryWrapper);
//
//        System.out.println(result.getTotal());
//        return result.getRecords();
//    }


//    @PostMapping("/lispageC")
//    public List<User> lispageC(@RequestBody QueryPageParam query){
//
//        HashMap param = query.getParam();
//
//        Page<User> page = new Page<>();
//        page.setCurrent(query.getPageNum());
//        page.setSize(query.getPageSize());
////
////        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
////        lambdaQueryWrapper.like(User::getName, param.get("name"));
//        IPage result = userService.pageC(page);
//
//        System.out.println(result.getTotal());
//        return result.getRecords();
//    }


//    @PostMapping("/lispageCC")
//    public Result lispageCC(@RequestBody QueryPageParam query){
//
//        HashMap param = query.getParam();
//
//        Page<User> page = new Page<>();
//        page.setCurrent(query.getPageNum());
//        page.setSize(query.getPageSize());
//
//        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        lambdaQueryWrapper.like(User::getName, param.get("name"));
//        // 传入wrapper筛选条件
//        IPage result = userService.pageCC(page, lambdaQueryWrapper);
//
//        System.out.println(result.getTotal());
//        return Result.suc(result.getRecords(), result.getTotal());
//    }

    @PostMapping("/registration")
    public Result register(@RequestBody UserRegistration userRegistration){
        return publicService.registration(userRegistration.getEmail(), userRegistration.getPassword(), userRegistration.getFirstname(), userRegistration.getLastname(), 1);
    }

    @GetMapping("/registrationVerification")
    public Result registrationVerification(@RequestParam("email") String email, @RequestParam("registrationTimestamp")
            long registrationTimestamp, @RequestParam("passwordToken") String passwordToken, @RequestParam("userRole") int userRole){
        return publicService.registrationVerification(email, registrationTimestamp, passwordToken, userRole);
    }
}
