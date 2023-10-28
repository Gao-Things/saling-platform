package com.usyd.capstone;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.assist.ISqlRunner;
import com.usyd.capstone.common.DTO.Result;
import com.usyd.capstone.common.VO.EmailAddress;
import com.usyd.capstone.common.VO.UpdatePasswordParameter;
import com.usyd.capstone.common.VO.UserLogin;
import com.usyd.capstone.common.VO.UserRegistration;
import com.usyd.capstone.entity.NormalUser;
import com.usyd.capstone.service.PublicService;
import org.aspectj.lang.annotation.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CapstoneApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PublicServiceTest {

    @Autowired
    private PublicService publicService;
    @Test
    public void verifyLogin() {
        UserLogin normalUser = new UserLogin();
        normalUser.setEmail("joses@yahoo.com");
        normalUser.setPassword("1234321");
        Result result = publicService.verifyLogin(normalUser);
        if(result.getCode()==200){
            System.out.println("success");
        }else {
            System.out.println("Wrong email or password");
        }
    }

    @Test
    public void registration() {

        // Case: Already registered email
        //assertEquals("This email has been registered!", publicService.registration("aaa", "password123", "Test", "User", 1).getMsg());

        // Case: New email registration
        assertEquals("Registration successful! The verification link will be sent to your E-mail box.", publicService.registration("newuser@example.com", "password123", "Test", "User", 1).getMsg());

        Result registration = publicService.registration("aaa", "password123", "Test", "User", 1);
        if(registration.getCode() == 400){
            System.out.println("This email has been registered!");
        }



    }

    @Test
    public void registrationVerification() {

        Date now = DateUtil.date();

        // 获取时间戳（毫秒值）
        long timestamp = now.getTime();
        assertEquals("The registration verification link is wrong!",

                publicService.registrationVerification("aaa",timestamp , "1234321", 1).getMsg());
    }



    @Test
    public void forgetPassword() {

        // Case: Valid and active email
        EmailAddress existingEmail = new EmailAddress();
        existingEmail.setEmailAddress("123123");
        assertEquals("The verification link has been sent to your email box",
                publicService.forgetPassword(existingEmail).getData());
    }

    @Test
    public void forgetPasswordVerification() {
        // assertEquals("invalid verification link", publicService.forgetPasswordVerification("aaa", 123).getData());
        // Case: Invalid email
        assertEquals("invalid verification link", publicService.forgetPasswordVerification("nonexistent@example.com", 1234567890L).getData());

        // Case: Valid email but incorrect timestamp
//        assertEquals("invalid verification link", publicService.forgetPasswordVerification("aaa", 1234567890L).getData());
//
//        // Case: Valid email and timestamp but expired link
//        // Assuming 86400001L milliseconds have passed since the email was sent
//        assertEquals("The resetting password verification link is out of date!", publicService.forgetPasswordVerification("aaa", System.currentTimeMillis() - 86400001L).getData());
    }

    @Test
    public void pollingResult() {
        // assertEquals("Email still not verity", publicService.pollingResult("aaa").getData());
        // Case: Invalid email
        // Case: Non-existent email
        assertEquals("error, the user doesn't exit",
                publicService.pollingResult("nonexistent@example.com").getData());

        // Case: Email not yet verified
//        assertEquals("Email still not verity",
//                publicService.pollingResult("nonexistent@example.com").getData());
//
//        // Case: Email verified
//        assertEquals("Email verity successful",
//                publicService.pollingResult("verified@example.com").getData());

    }

    @Test
    public void updatePassword() {
//        UpdatePasswordParameter updatePasswordParameter = new UpdatePasswordParameter();
//        updatePasswordParameter.setEmail("aaa");
//        updatePasswordParameter.setPassword("1234321");
//        updatePasswordParameter.setPassword2("1234321");
//        assertEquals("your resetting password request hasn't been verity by email", publicService.updatePassword(updatePasswordParameter).getData());
//
        //UpdatePasswordParameter invalidEmailParams = new UpdatePasswordParameter();
        UpdatePasswordParameter params = new UpdatePasswordParameter();
        params.setEmail("nonexistent@example.com");
        params.setPassword("1234321");
        params.setPassword2("1234321");
        assertEquals("error, can`t find this user", publicService.updatePassword(params).getData());
//
//        // Case: Valid email but passwords don't match
//        params.setEmail("valid@example.com");
//        params.setPassword("password1");
//        params.setPassword2("password2");
//        assertEquals("your two password is not same", publicService.updatePassword(params).getData());

        // Case: Valid email, matching passwords but request hasn't been verified

//        params.setPassword("newPassword");
//        params.setPassword2("newPassword");
//        assertEquals("your resetting password request hasn't been verity by email", publicService.updatePassword(params).getData());


    }
}