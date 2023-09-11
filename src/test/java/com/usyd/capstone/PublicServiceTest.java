package com.usyd.capstone.service.impl;

import com.usyd.capstone.CapstoneApplication;
import com.usyd.capstone.common.VO.UserLogin;
import com.usyd.capstone.common.VO.UserRegistration;
import com.usyd.capstone.service.PublicService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CapstoneApplication.class)
public class PublicServiceTest {

    @Autowired
    private PublicService publicService;

    @Test
    public void verifyLogin() {
        UserLogin normalUser = new UserLogin();
        UserLogin adminUser = new UserLogin();
        adminUser.setEmail("aaa");
        adminUser.setPassword("1234321");
        adminUser.setUserRole(2);
        normalUser.setEmail("aaa");
        normalUser.setPassword("1234321");
        normalUser.setUserRole(1);
        // System.err.println(publicService);
        assertEquals("Login successfully!", publicService.verifyLogin(normalUser).getMsg());
        assertEquals("Login successfully!", publicService.verifyLogin(adminUser).getMsg());
    }

    @Test
    public void registration() {
        UserRegistration userRegistration = new UserRegistration();
        userRegistration.setEmail("aaa");
        userRegistration.setFirstname("firstname2");
        userRegistration.setLastname("lastname2");
        userRegistration.setPassword("1234321");
        int userRole = 1;
        assertEquals("This email has been registered!", publicService.registration(userRegistration.getEmail(), userRegistration.getPassword(), userRegistration.getFirstname(), userRegistration.getLastname(), userRole).getMsg());

    }

    @Test
    public void registrationVerification() {

    }

    @Test
    public void testVerifyLogin() {
    }

    @Test
    public void testRegistration() {
    }

    @Test
    public void testRegistrationVerification() {
    }

    @Test
    public void forgetPassword() {
    }

    @Test
    public void forgetPasswordVerification() {
    }

    @Test
    public void pollingResult() {
    }

    @Test
    public void updatePassword() {
    }
}