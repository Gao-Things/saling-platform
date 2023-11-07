package com.usyd.capstone.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.usyd.capstone.common.DTO.LoginResponse;
import com.usyd.capstone.common.Enums.ROLE;
import com.usyd.capstone.common.Enums.SYSTEM_SECURITY_KEY;
import com.usyd.capstone.common.compents.JwtToken;
import com.usyd.capstone.common.DTO.Result;
import com.usyd.capstone.common.compents.SendEmail;
import com.usyd.capstone.entity.AdminUser;
import com.usyd.capstone.entity.NormalUser;
import com.usyd.capstone.common.VO.EmailAddress;
import com.usyd.capstone.common.VO.UpdatePasswordParameter;
import com.usyd.capstone.common.VO.UserLogin;
import com.usyd.capstone.entity.UserSetting;
import com.usyd.capstone.entity.abstractEntities.NotSuperUser;
import com.usyd.capstone.entity.abstractEntities.User;
import com.usyd.capstone.mapper.AdminUserMapper;
import com.usyd.capstone.mapper.NormalUserMapper;
import com.usyd.capstone.mapper.SuperUserMapper;
import com.usyd.capstone.mapper.UserSettingMapper;
import com.usyd.capstone.service.PublicService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;


@Service
public class PublicServiceImpl extends ServiceImpl<NormalUserMapper, NormalUser> implements PublicService {
    @Resource
    private NormalUserMapper normalUserMapper;

    @Resource
    private AdminUserMapper adminUserMapper;

    @Resource
    private SuperUserMapper superUserMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserSettingMapper userSettingMapper;

    @Autowired
    private SendEmail sentEmail;


//    @Override
//    public List<User> listAll() {
//        return userMapper.listAll();
//    }
//
//    @Override
//    public IPage pageC(Page<User> page) {
//        return userMapper.pageC(page);
//    }
//
//    @Override
//    public IPage pageCC(Page<User> page, Wrapper wrapper) {
//        return userMapper.pageCC(page, wrapper);
//    }

    @Override
    public Result verifyLogin(UserLogin userLogin) {
        if (StringUtils.isEmpty(userLogin.getPassword()) || StringUtils.isEmpty(userLogin.getEmail())){
            return Result.fail("Empty Email or password");
        }
        User user;
        BaseMapper mapper;
        ROLE role;
        if(userLogin.getUserRole() == 1)
        {
            mapper = normalUserMapper;
            role = ROLE.ROLE_NORMAL;
        }
        else if (userLogin.getUserRole() == 2)
        {
            mapper = adminUserMapper;
            role = ROLE.ROLE_ADMIN;
        }
        else
        {
            mapper = superUserMapper;
            role = ROLE.ROLE_SUPER;
        }
        // Verify user
        user = (User) mapper.selectOne(new QueryWrapper<NormalUser>().eq("email", userLogin.getEmail()));

        if (user == null) {

            return Result.fail("Wrong email or password");
        }

        /**
         * 不好验证，暂时绕行
         */
        if (userLogin.getUserRole() == 3){
            System.out.println(user.getPassword());
            System.out.println(userLogin.getPassword());
            if (!userLogin.getPassword().equals(user.getPassword())){
                return Result.fail("wrong email or password");
            }
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setId(user.getId());
            loginResponse.setRole(userLogin.getUserRole());
            String token = JwtToken.generateToken(user.getId(), userLogin.getEmail(), role);
            return new Result(200, "Login successfully!", 0L, loginResponse, token);
        }

        String machUse = userLogin.getEmail() + userLogin.getPassword() + SYSTEM_SECURITY_KEY.PASSWORD_SECRET_KEY.getValue();

        if (!passwordEncoder.matches(machUse, user.getPassword())){
            return Result.fail("Wrong email or password");
        }

        if (!user.isActivationStatus()){
            return Result.fail("your account has not been activation");
        }

        String token = JwtToken.generateToken(user.getId(), userLogin.getEmail(), role);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setId(user.getId());
        loginResponse.setRole(userLogin.getUserRole());
//        loginResponse.setEmail(userLogin.getEmail());
        return new Result(200, "Login successfully!", 0L, loginResponse, token);
    }

    @Override
    //如果用户不存在->存一个新用户+发邮件+返回“已保存”
    //如果存在->如果已激活->报错“已激活”
    //如果存在->如果未激活->更新该用户+发邮件+返回“已更新”
    //admin用默认密码或者空密码注册，之后可以修改密码
    public Result registration(String email, String password, String firstname, String lastname, int userRole){
        long registrationTimeStamp = System.currentTimeMillis();
        String passwordToken = passwordEncoder.encode(email + password + SYSTEM_SECURITY_KEY.PASSWORD_SECRET_KEY.getValue());
        String name = firstname +' '+ lastname;
        NotSuperUser notSuperUserOld;
        BaseMapper mapper;
        String text;
        if(userRole == 1)
        {
            mapper = normalUserMapper;
            text = "your";
        }
        else
        {
            mapper = adminUserMapper;
            text = name + "'s";
        }
        notSuperUserOld = (NotSuperUser) mapper.selectOne(new QueryWrapper<NotSuperUser>().eq("email", email));

        if(notSuperUserOld == null)
        {
            NotSuperUser notSuperUserNew;
            if(userRole == 1)
            {
                notSuperUserNew = new NormalUser();
            }
            else
            {
                notSuperUserNew = new AdminUser();
            }
            notSuperUserNew.setEmail(email);
            notSuperUserNew.setName(name);
            notSuperUserNew.setRegistrationTimestamp(registrationTimeStamp);
            notSuperUserNew.setPassword(passwordToken);
            notSuperUserNew.setActivationStatus(false);

            sentEmail.sentRegistrationEmail(email, registrationTimeStamp, passwordToken, userRole);

            // 可以直接调用mybatisplus的insert方法
            mapper.insert(notSuperUserNew);


            /**
             * 插入用户设置默认值
             */
            UserSetting userSetting = new UserSetting();
            userSetting.setUserId(Math.toIntExact(notSuperUserNew.getId()));
            userSetting.setChooseTone("elegant");
            userSetting.setMessageToneOpen(1);
            userSetting.setNotificationOpen(0);
            userSetting.setMessageReceived(1);
            userSettingMapper.insert(userSetting);



            return new Result(200, "Registration successful! The verification link will be " +
                    "sent to " + text + " E-mail box.", 0L, null, null);
        }
        else
        {
            if(notSuperUserOld.isActivationStatus())
            {
                return new Result(409, "This email has been registered!", 0L, null, null);
            }
            else {
                notSuperUserOld.setRegistrationTimestamp(registrationTimeStamp);
                notSuperUserOld.setPassword(passwordToken);
                sentEmail.sentRegistrationEmail(email, registrationTimeStamp, passwordToken, userRole);
                mapper.updateById(notSuperUserOld);
                return new Result(200, "Registration successful! The verification link will be " +
                        "sent to " + text + " E-mail box.", 0L, null, null);
            }
        }
    }



    @Override
    public Result registrationVerification(String email, long registrationTimestamp, String password, int userRole) {
        NotSuperUser notSuperUser;
        BaseMapper mapper;
        if(userRole == 1)
        {
            mapper = normalUserMapper;
        }
        else
        {
            mapper = adminUserMapper;
        }
        notSuperUser = (NotSuperUser)mapper.selectOne(new QueryWrapper<NotSuperUser>().eq("email", email)
                                                                                        .eq("password", password)
                                                                                        .eq("registration_timestamp", registrationTimestamp));
        if(notSuperUser == null)
        {
            return new Result(404, "The registration verification link is wrong!", 0L, null, null);
        }
        else
        {
            if(notSuperUser.isActivationStatus())
            {
                return new Result(400, "This is an active account!", 0L, null, null);
            }
            else if(System.currentTimeMillis() - registrationTimestamp > 86400000L)
            {
                return new Result(410, "The registration verification link is out of date!", 0L, null, null);
            }
            else
            {
                notSuperUser.setActivationStatus(true);
                mapper.updateById(notSuperUser);
                return new Result(200, "Your account has been activated!", 0L, null, null);
            }
        }
    }

    @Override
    public Result forgetPassword(EmailAddress emailAddress) {

        NormalUser normalUser = normalUserMapper.selectOne(new QueryWrapper<NormalUser>().eq("email", emailAddress.getEmailAddress()));
        if (normalUser == null){
            return Result.fail("your email address is wrong");
        }
       normalUser.setResettingPasswordTimestamp(System.currentTimeMillis());

        if (!normalUser.isActivationStatus()){
            return Result.fail("your account is not active");
        }

       // send verify email
        sentEmail.sentForgetEmail( emailAddress.getEmailAddress(), normalUser.getResettingPasswordTimestamp());
        normalUserMapper.updateById(normalUser);
        return Result.suc("The verification link has been sent to your email box");
    }

    @Override
    public Result forgetPasswordVerification(String email, long resettingPasswordTimestamp) {

        NormalUser normalUser = normalUserMapper.selectOne(new QueryWrapper<NormalUser>().eq("email", email));
        if (normalUser == null){
            return Result.fail("invalid verification link");
        }
        if(normalUser.getResettingPasswordTimestamp() != resettingPasswordTimestamp)
        {
            return Result.fail("invalid verification link");
        }
        if (System.currentTimeMillis() - resettingPasswordTimestamp > 86400000L){
            return Result.fail("The resetting password verification link is out of date!");
        }
        normalUser.setResettingPasswordTimestamp(System.currentTimeMillis());
        normalUser.setForgetPasswordVerity(true);
        normalUserMapper.updateById(normalUser);
        return Result.suc("The resetting password verification has been verified successfully!" +
                "You will have 30 minutes to set a new password");
    }

    @Override
    public Result pollingResult(String email) {

        NormalUser normalUser = normalUserMapper.selectOne(new QueryWrapper<NormalUser>().eq("email", email));

        if (normalUser == null){
            return Result.fail("error, the user doesn't exit");
        }

        if (normalUser.isForgetPasswordVerity()){
           return Result.suc("Email verity successful");
        }else {
            return Result.fail("Email still not verity");
        }
    }

    //TODO 检测两个密码是否一致的功能移到前端（？）
    @Override
    public Result updatePassword(UpdatePasswordParameter updatePasswordParameter) {

        String email = updatePasswordParameter.getEmail();
        String password = updatePasswordParameter.getPassword();
        String passwordTwo = updatePasswordParameter.getPassword2();

        NormalUser normalUser = normalUserMapper.selectOne(new QueryWrapper<NormalUser>().eq("email", email));
        if (normalUser == null){
            return Result.fail("error, can`t find this user");
        }
        if (!normalUser.isForgetPasswordVerity()){
            return Result.fail("your resetting password request hasn't been verity by email");
        }
        if (System.currentTimeMillis() - normalUser.getResettingPasswordTimestamp() > 1800000L)
        {
            normalUser.setForgetPasswordVerity(false);
            normalUserMapper.updateById(normalUser);
            return Result.fail("resetting password permission has been out of date.");
        }
        if (!password.equals(passwordTwo)){
            return Result.fail("your two password is not same ");
        }
        // encode password
        String passwordToken = passwordEncoder.encode(email + password + SYSTEM_SECURITY_KEY.PASSWORD_SECRET_KEY.getValue());
        normalUser.setPassword(passwordToken);
        normalUser.setForgetPasswordVerity(false);

        normalUserMapper.updateById(normalUser);

        return Result.suc("user password has been update");
    }


}
