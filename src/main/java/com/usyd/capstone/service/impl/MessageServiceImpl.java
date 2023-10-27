package com.usyd.capstone.service.impl;

import com.usyd.capstone.common.DTO.Result;
import com.usyd.capstone.entity.Message;
import com.usyd.capstone.mapper.MessageMapper;
import com.usyd.capstone.service.MessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yyf
 * @since 2023年10月16日
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Autowired
    private MessageMapper messageMapper;
    @Override
    public Result getMessageListByUserId(Integer userId) {
        return Result.suc(messageMapper.getMessageListByUserId(userId));
    }

    @Override
    public Result getMessageListByUserIdAndRemoteUserId(Integer userId, Integer remoteUserId) {

        return Result.suc(messageMapper.getMessageListByUserIdAndRemoteUserId(userId, remoteUserId));
    }

    @Override
    public Result deleteMessageHistoryByUserIdAndRemoteUserId(Integer userId, Integer remoteUserId) {
        return Result.suc(messageMapper.updateDeleteUserIdByCondition(userId, remoteUserId));
    }
}
