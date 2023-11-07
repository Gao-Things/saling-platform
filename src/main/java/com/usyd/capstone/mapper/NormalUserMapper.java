package com.usyd.capstone.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.usyd.capstone.common.DTO.StatisticsData;
import com.usyd.capstone.entity.NormalUser;
import com.usyd.capstone.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NormalUserMapper extends BaseMapper<NormalUser> {

//    List<User> listAll();
//
//    IPage pageC(Page<User> page);
//
//    IPage pageCC(Page<User> page, Wrapper ew);

    NormalUser findByEmailAndRegistrationTimestampAndPassword(String email, long registrationTimestamp, String password);

    NormalUser getUserByUserID(@Param("userId") Long userId);

    Page<NormalUser> getUserListAdmin(Page<Product> page, String searchValue);

    @Select("SELECT gender AS name, COUNT(*) AS value FROM normal_user GROUP BY gender")
    List<StatisticsData> selectGenderStatistics();
}
