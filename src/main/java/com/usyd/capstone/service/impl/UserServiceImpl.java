package com.usyd.capstone.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
        if (dbUser == null || !dbUser.getPassword().equals(userLogin.getPassword())) {
            return Result.fail("Wrong email or password");
        }

        String token =  TokenUtils.generateToken(dbUser.getPassword());

        return Result.suc(token);
    }

    @Override
    //如果用户不存在->存一个新用户+发邮件+返回“已保存”
    //如果存在->如果已激活->报错“已激活”
    //如果存在->如果未激活->更新该用户+发邮件+返回“已更新”
    @Async("taskExecutor")
    public Result registration(String email, String password){
        long registrationTimeStamp = System.currentTimeMillis();
        String passwordToken = passwordEncoder.encode(email + password + PublicKey.firstKey.getValue());
        User userOld = userMapper.findByEmail(email);
        if(userOld == null)
        {
            User userNew = new User();
            userNew.setEmail(email);
            userNew.setRegistrationTimestamp(registrationTimeStamp);
            userNew.setPassword(passwordToken);
            sentEmail.sentRegistrationEmail(email, registrationTimeStamp, passwordToken);
            userMapper.saveANewUser(userNew);
        }
        else
        {
            if(userOld.isActivationStatus())
            {
                //reject
            }
            else {
                userOld.setRegistrationTimestamp(registrationTimeStamp);
                userOld.setPassword(passwordToken);
                sentEmail.sentRegistrationEmail(email, registrationTimeStamp, passwordToken);
                userMapper.updateAnOledInactivatedUser(userOld);
            }
        }

        return null;
    }



    @Override
    public Result verifyRegistration(String email, long registrationTimeStamp, String passwordToken) {
        return null;
    }


}
