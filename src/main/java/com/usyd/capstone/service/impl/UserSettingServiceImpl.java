package com.usyd.capstone.service.impl;

import com.usyd.capstone.entity.UserSetting;
import com.usyd.capstone.mapper.UserSettingMapper;
import com.usyd.capstone.service.UserSettingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yyf
 * @since 2023年10月28日
 */
@Service
public class UserSettingServiceImpl extends ServiceImpl<UserSettingMapper, UserSetting> implements UserSettingService {

}
