package com.usyd.capstone.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.usyd.capstone.common.Result;
import com.usyd.capstone.common.utils.TokenUtils;
import com.usyd.capstone.entity.User;
import com.usyd.capstone.entity.VO.UserLogin;
import com.usyd.capstone.mapper.UserMapper;
import com.usyd.capstone.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private UserMapper userMapper;


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
            return Result.fail();
        }

        // Verify user
        User dbUser = userMapper.selectOne(new QueryWrapper<User>().eq("email", userLogin.getEmail()));
        if (dbUser == null || !dbUser.getPassword().equals(userLogin.getPassword())) {
            return Result.fail("Wrong email or password");
        }

        String token =  TokenUtils.generateToken(dbUser.getPassword());

        return Result.suc(token);
    }


}
