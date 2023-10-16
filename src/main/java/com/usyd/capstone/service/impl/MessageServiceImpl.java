package com.usyd.capstone.service.impl;

import com.usyd.capstone.entity.Message;
import com.usyd.capstone.mapper.MessageMapper;
import com.usyd.capstone.service.MessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
