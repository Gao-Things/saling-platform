package com.usyd.capstone.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.usyd.capstone.entity.AdminUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminUserMapper extends BaseMapper<AdminUser> {
}
