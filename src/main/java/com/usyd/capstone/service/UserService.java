package com.usyd.capstone.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.usyd.capstone.common.Result;
import com.usyd.capstone.entity.User;
import com.usyd.capstone.entity.VO.UserLogin;

import java.util.List;

public interface UserService extends IService<User> {

    List<User> listAll();

    IPage pageC(Page<User> page);

    IPage pageCC(Page<User> page, Wrapper wrapper);

    Result verifyLogin(UserLogin userLogin);

}
