package com.usyd.capstone.service.base.impl;
import com.github.dreamyoung.mprelation.ServiceImpl;
import com.usyd.capstone.entity.AdminUser;
import com.usyd.capstone.mapper.AdminUserMapper;
import com.usyd.capstone.service.base.AdminUserBaseService;
import org.springframework.stereotype.Service;

@Service
public class AdminUserBaseServiceImpl extends ServiceImpl<AdminUserMapper, AdminUser> implements AdminUserBaseService {

}
