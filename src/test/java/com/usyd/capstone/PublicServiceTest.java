package com.usyd.capstone;

import com.baomidou.mybatisplus.core.assist.ISqlRunner;
import com.usyd.capstone.common.VO.EmailAddress;
import com.usyd.capstone.common.VO.UpdatePasswordParameter;
import com.usyd.capstone.common.VO.UserLogin;
import com.usyd.capstone.common.VO.UserRegistration;
import com.usyd.capstone.entity.NormalUser;
import com.usyd.capstone.service.PublicService;
import org.aspectj.lang.annotation.Before;
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
    @Test
    public void verifyLogin() {
        // Case: Empty fields
        UserLogin emptyFieldsUser = new UserLogin();
        assertEquals("Empty Email or password", publicService.verifyLogin(emptyFieldsUser).getData());
        //  Case: Non-existent user
        UserLogin nonExistentUser = new UserLogin();
        nonExistentUser.setEmail("nonexistent@example.com");
        nonExistentUser.setPassword("password123");
        assertEquals("Wrong email or password", publicService.verifyLogin(nonExistentUser).getData());
        // Case: Valid normal user
        UserLogin normalUser = new UserLogin();
        normalUser.setEmail("aaa");
        normalUser.setPassword("1234321");
        normalUser.setUserRole(1);
        assertEquals("Login successfully!", publicService.verifyLogin(normalUser).getMsg());
        // Case: Valid admin user
        UserLogin adminUser = new UserLogin();
        adminUser.setEmail("aaa");
        adminUser.setPassword("1234321");
        adminUser.setUserRole(2);
        assertEquals("Login successfully!", publicService.verifyLogin(adminUser).getMsg());
    }

    @Test
    public void registration() {

        // Case: Already registered email
        assertEquals("This email has been registered!", publicService.registration("aaa", "password123", "Test", "User", 1).getMsg());

        // Case: New email registration
        assertEquals("Registration successful! The verification link will be sent to your E-mail box.", publicService.registration("newuser@example.com", "password123", "Test", "User", 1).getMsg());


    }

    @Test
    public void registrationVerification() {
        assertEquals("The registration verification link is wrong!", publicService.registrationVerification("aaa", 123, "1234321", 1).getMsg());
    }



    @Test
    public void forgetPassword() {
        // Case: Invalid email
        EmailAddress nonExistentEmail = new EmailAddress();
        nonExistentEmail.setEmailAddress("nonexistent@example.com");
        assertEquals("your email address is wrong", publicService.forgetPassword(nonExistentEmail).getData());

        // Case: Valid but inactive email
//        EmailAddress inactiveEmail = new EmailAddress();
//        inactiveEmail.setEmailAddress("inactive@example.com");
//        assertEquals("your account is not active", publicService.forgetPassword(inactiveEmail).getData());

        // Case: Valid and active email
        EmailAddress existingEmail = new EmailAddress();
        existingEmail.setEmailAddress("aaa");
        assertEquals("The verification link has been sent to your email box", publicService.forgetPassword(existingEmail).getData());
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