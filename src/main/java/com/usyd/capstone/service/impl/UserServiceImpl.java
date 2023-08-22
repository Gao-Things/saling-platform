package com.usyd.capstone.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.usyd.capstone.common.Enums.PublicKey;
import com.usyd.capstone.common.Result;
import com.usyd.capstone.common.SendEmail;
import com.usyd.capstone.common.utils.TokenUtils;
import com.usyd.capstone.entity.User;
import com.usyd.capstone.entity.VO.UserLogin;
import com.usyd.capstone.mapper.UserMapper;
import com.usyd.capstone.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SendEmail sentEmail;


    @Override
    public List<User> listAll() {
        return userMapper.listAll();
    }

    @Override
    public IPage pageC(Page<User> page) {
        return userMapper.pageC(page);
    }

    @Override
    public IPage pageCC(Page<User> page, Wrapper wrapper) {
        return userMapper.pageCC(page, wrapper);
    }

    @Override
    public Result verifyLogin(UserLogin userLogin) {
        if (StringUtils.isEmpty(userLogin.getPassword()) || StringUtils.isEmpty(userLogin.getEmail())){
            return Result.fail("Empty Email or password");
        }

        // Verify user
        User dbUser = userMapper.selectOne(new QueryWrapper<User>().eq("email", userLogin.getEmail()));
        if (dbUser == null) {
            return Result.fail("Wrong email or password");
        }

        String machUse = userLogin.getEmail() + userLogin.getPassword() + PublicKey.firstKey.getValue();

        if (!passwordEncoder.matches(machUse, dbUser.getPassword())){
            return Result.fail("Wrong email or password");
        }

        if (!dbUser.isActivationStatus()){
            return Result.fail("your account has not been activation");
        }

        String token =  TokenUtils.generateToken(dbUser.getPassword());

        return Result.suc(token);
    }

    @Override
    //如果用户不存在->存一个新用户+发邮件+返回“已保存”
    //如果存在->如果已激活->报错“已激活”
    //如果存在->如果未激活->更新该用户+发邮件+返回“已更新”
//    @Async("taskExecutor")
    public Result registration(String email, String password, String firstname, String lastname){
        long registrationTimeStamp = System.currentTimeMillis();
        String passwordToken = passwordEncoder.encode(email + password + PublicKey.firstKey.getValue());
        User userOld = userMapper.findByEmail(email);
        if(userOld == null)
        {
            User userNew = new User();
            userNew.setEmail(email);
            userNew.setName(firstname +' '+ lastname);
            userNew.setRegistrationTimestamp(registrationTimeStamp);
            userNew.setPassword(passwordToken);
            userNew.setActivationStatus(false);
            sentEmail.sentRegistrationEmail(email, registrationTimeStamp, passwordToken);
//            userMapper.saveANewUser(userNew);

            // 可以直接调用mybatisplus的insert方法
            userMapper.insert(userNew);

            return Result.customize(200, "Registration successful! The verification link will be " +
                    "sent to your E-mail box.", 0L, null);
        }
        else
        {
            if(userOld.isActivationStatus())
            {
                return Result.customize(409, "This email has been registered!", 0L, null);
            }
            else {
                userOld.setRegistrationTimestamp(registrationTimeStamp);
                userOld.setPassword(passwordToken);
                sentEmail.sentRegistrationEmail(email, registrationTimeStamp, passwordToken);
                userMapper.updateAnOledInactivatedUser(userOld);
                return Result.customize(200, "Registration successful! The verification link will be " +
                        "sent to your E-mail box.", 0L, null);
            }
        }
    }



    @Override
    public Result registrationVerification(String email, long registrationTimestamp, String password) {
        User user = userMapper.findByEmailAndRegistrationTimestampAndPassword(email, registrationTimestamp, password);
        if(user == null)
        {
            return Result.customize(404, "The registration verification link is wrong!", 0L, null);
        }
        else
        {
            if(user.isActivationStatus())
            {
                return Result.customize(400, "This is an active account!", 0L, null);
            }
            else if(System.currentTimeMillis() - registrationTimestamp > 86400000L)
            {
                return Result.customize(410, "The registration verification link is out of date!", 0L, null);
            }
            else
            {
                userMapper.activeAnAccount(user);
                return Result.customize(200, "Your account has been activated!", 0L, null);
            }
        }
    }


}
