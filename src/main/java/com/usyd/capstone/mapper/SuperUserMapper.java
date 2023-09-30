package com.usyd.capstone.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.usyd.capstone.entity.SuperUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SuperUserMapper extends BaseMapper<SuperUser> {
}
