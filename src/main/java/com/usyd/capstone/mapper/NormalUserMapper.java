package com.usyd.capstone.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.usyd.capstone.entity.NormalUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NormalUserMapper extends BaseMapper<NormalUser> {

//    List<User> listAll();
//
//    IPage pageC(Page<User> page);
//
//    IPage pageCC(Page<User> page, Wrapper ew);

    NormalUser findByEmailAndRegistrationTimestampAndPassword(String email, long registrationTimestamp, String password);
}
