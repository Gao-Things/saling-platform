package com.usyd.capstone.rabbitMq;

import com.alibaba.fastjson.JSON;
import com.usyd.capstone.common.config.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class FanoutSender {
    private static Logger logger = LoggerFactory.getLogger(FanoutSender.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;


    public void sendMessage(Map<Integer, String> rabbitMessageList) {
        logger.info("【消息发送者】发送消息到fanout交换机，");
        System.out.println("【消息发送者】发送消息到fanout交换机，");
        // 使用 Fastjson 的 JSON 类来转换
        String jsonStr = JSON.toJSONString(rabbitMessageList);
        rabbitTemplate.convertAndSend(Constants.FANOUT_EXCHANGE_NAME, "", jsonStr);
    }


    public void sendChatMessage(Map<Integer, String> rabbitMessageList) {
        logger.info("【消息发送者】发送消息到聊天室交换机，");
        System.out.println("【消息发送者】发送消息到聊天室交换机，");
        // 使用 Fastjson 的 JSON 类来转换
        String jsonStr = JSON.toJSONString(rabbitMessageList);
        rabbitTemplate.convertAndSend(Constants.CHAT_EXCHANGE_NAME, "", jsonStr);
    }
}

