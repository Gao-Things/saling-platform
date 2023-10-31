package com.usyd.capstone.rabbitMq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.usyd.capstone.common.compents.NotificationServer;
import com.usyd.capstone.common.compents.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class FanoutReceiver {
    private static Logger logger = LoggerFactory.getLogger(FanoutReceiver.class);

    @RabbitHandler
    @RabbitListener(queues = "#{notificationQueue.name}")//动态绑定
    public void receiveMessage(String message) {
        logger.info("消息接收者接收到来自【队列一】的消息，消息内容: ");
        Map<Integer, Session> electricSocketMap = NotificationServer.sessionMap;


        // 然后将 JSONObject 转换为 Map
        JSONObject jsonObject = JSON.parseObject(message);
        Map<String, String> stringMap = jsonObject.toJavaObject(new TypeReference<Map<String, String>>(){});

        // 因为 JSON 的键总是字符串，所以需要手动将它们转换为 Integer
        Map<Integer, String> rabbitMessageList = stringMap.entrySet()
                .stream()
                .collect(Collectors.toMap(entry -> Integer.parseInt(entry.getKey()), Map.Entry::getValue));

        /**
         * 判断rabbitMq交换机的key（userID）是否能在当前进程上找到
         */
        for (Integer key : rabbitMessageList.keySet()) {
            if(electricSocketMap.get(key)!=null) {

                String string = rabbitMessageList.get(key);
                NotificationServer.sendMessage(string, key);
                System.out.println("接收到rabbitmq的消息" + string);
            }
        }
    }


    @RabbitHandler
    @RabbitListener(queues = "#{chatQueue.name}")//动态绑定
    public void receiveChatMessage(String message) {
        logger.info("消息接收者接收到来自【聊天队列一】的消息，消息内容: ");
        Map<Integer, Session> electricSocketMap = WebSocketServer.sessionMap2;

        // 然后将 JSONObject 转换为 Map
        JSONObject jsonObject = JSON.parseObject(message);
        Map<String, String> stringMap = jsonObject.toJavaObject(new TypeReference<Map<String, String>>(){});

        // 因为 JSON 的键总是字符串，所以需要手动将它们转换为 Integer
        Map<Integer, String> rabbitMessageList = stringMap.entrySet()
                .stream()
                .collect(Collectors.toMap(entry -> Integer.parseInt(entry.getKey()), Map.Entry::getValue));

        /**
         * 判断rabbitMq交换机的key（userID）是否能在当前进程上找到
         */
        for (Integer key : rabbitMessageList.keySet()) {

            // 如果这个key是0，则为心跳包
            if (key == 0){

            }

            if(electricSocketMap.get(key)!=null) {

                String string = rabbitMessageList.get(key);
                WebSocketServer.sendMessage(string, key);
                System.out.println("接收到rabbitmq的消息" + string);
            }
        }
    }
}

