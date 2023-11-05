package com.usyd.capstone.common.compents;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.usyd.capstone.common.DTO.MessageNotificationDTO;
import com.usyd.capstone.entity.Notification;
import com.usyd.capstone.mapper.NotificationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author websocket服务
 */


@ServerEndpoint(value = "/notification/{userId}")
@Component
public class NotificationServer {

    /**
     * 用来解决webSocket中无法注入mapper
     */
    private static ApplicationContext applicationContext;
    private static final Logger log = LoggerFactory.getLogger(NotificationServer.class);
    /**
     * 记录当前在线连接数
     */
    public static final Map<Integer, Session> sessionMap = new ConcurrentHashMap<>();

    public static void setApplicationContext(ApplicationContext applicationContext) {
        NotificationServer.applicationContext = applicationContext;
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") Integer userId) {
        sessionMap.put(userId, session);
        log.info("有新用户加入，userEmail={}, 当前在线人数为：{}", userId, sessionMap.size());
    }
    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session, @PathParam("userId") Integer userId) {
        sessionMap.remove(userId);
    }

    @OnMessage
    public void onMessage(String message, Session session, @PathParam("userId") Integer userId) {

        log.info("服务端收到客户端确认={}的消息:{}", userId, message);
        JSONObject obj = JSONUtil.parseObj(message);
        int status = obj.getInt("ok");
        Long notificationId = obj.getInt("notificationId").longValue();
        if (status == 1){
            // 更新数据库消息列表的状态为已读
            NotificationMapper notificationMapper = applicationContext.getBean(NotificationMapper.class);

            Notification notificationOld =  notificationMapper.selectById(notificationId);

            notificationOld.setUserIsRead(1);

            notificationMapper.updateById(notificationOld);
        }

        if(status == 2){
            // 接收到移动端心跳包消息，发送心跳回复
            MessageNotificationDTO messageNotificationDTO = new MessageNotificationDTO();

            // 消息类型， 998为心跳续约类型
            messageNotificationDTO.setMessageType(998);

            messageNotificationDTO.setNotificationContent("pong");

            String result = com.alibaba.fastjson.JSONObject.toJSONString(messageNotificationDTO);

            // 发送给移动端心跳消息
            sendMessage(result, userId);

        }

    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }
    /**
     * 服务端发送消息给客户端
     */
    public static void sendMessage(String message, Integer userId) {

        try {
            // 如果用户处于在线状态就进行推送
            if (sessionMap.get(userId)!=null){
                log.info("服务器发送消息给客户端", message);
                sessionMap.get(userId).getBasicRemote().sendText(message);
            }
        } catch (Exception e) {
            log.error("服务端发送消息给客户端失败", e);
        }
    }



}

