package com.usyd.capstone.service;

import com.usyd.capstone.common.DTO.Result;
import com.usyd.capstone.entity.Message;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yyf
 * @since 2023年10月16日
 */
public interface MessageService extends IService<Message> {

    Result getMessageListByUserId(Integer userId);

    Result getMessageListByUserIdAndRemoteUserId(Integer userId, Integer remoteUserId);

    Result deleteMessageHistoryByUserIdAndRemoteUserId(Integer userId, Integer remoteUserId);
}
