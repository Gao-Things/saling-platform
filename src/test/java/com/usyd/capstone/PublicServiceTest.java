package com.usyd.capstone;

import com.usyd.capstone.common.VO.UserLogin;
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
        normalUser.setEmail("aaa");
        normalUser.setPassword("1234321");
        normalUser.setUserRole(1);
        assertEquals("Login successfully!", publicService.verifyLogin(normalUser).getMsg());
    }

    @Test
    public void registration() {
    }

    @Test
    public void registrationVerification() {
    }
}