package com.usyd.capstone;

import com.usyd.capstone.common.VO.EmailAddress;
import com.usyd.capstone.common.VO.UpdatePasswordParameter;
import com.usyd.capstone.common.VO.UserLogin;
import com.usyd.capstone.common.VO.UserRegistration;
import com.usyd.capstone.service.PublicService;
import org.junit.Test;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CapstoneApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PublicServiceTest {

    @Autowired
    private PublicService publicService;
    Result result = new Result();

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
        assertEquals("The registration verification link is wrong!", publicService.registrationVerification("aaa", 123, "1234321", 1).getMsg());
    }

//    @Test
//    public void testVerifyLogin() {
//
//
//        assertEquals("Login successfully!", publicService.testverifyLogin("aaa", "1234321", 1).getMsg());
//
//
//    }

    @Test
    public void testRegistration() {
    }

    @Test
    public void testRegistrationVerification() {

    }

    @Test
    public void forgetPassword() {
        EmailAddress emailAddress = new EmailAddress();
        assertEquals("your email address is wrong", publicService.forgetPassword(emailAddress).getData());

    }

    @Test
    public void forgetPasswordVerification() {
        assertEquals("invalid verification link", publicService.forgetPasswordVerification("aaa", 123).getData());
    }

    @Test
    public void pollingResult() {
        assertEquals("Email still not verity", publicService.pollingResult("aaa").getData());
    }

    @Test
    public void updatePassword() {
        UpdatePasswordParameter updatePasswordParameter = new UpdatePasswordParameter();
        updatePasswordParameter.setEmail("aaa");
        updatePasswordParameter.setPassword("1234321");
        updatePasswordParameter.setPassword2("1234321");
        assertEquals("your resetting password request hasn't been verity by email", publicService.updatePassword(updatePasswordParameter).getData());
    }
}