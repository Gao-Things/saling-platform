package com.usyd.capstone.common.compents;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.usyd.capstone.common.DTO.MessageNotificationDTO;
import com.usyd.capstone.entity.Message;
import com.usyd.capstone.entity.abstractEntities.User;
import com.usyd.capstone.mapper.MessageMapper;
import com.usyd.capstone.mapper.NormalUserMapper;
import com.usyd.capstone.rabbitMq.FanoutSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author websocket服务
 */


@ServerEndpoint(value = "/imserver/{userId}")
@Component
public class WebSocketServer {


    /**
     * 用来解决webSocket中无法注入mapper
     */
    private static ApplicationContext applicationContext;


    public static void setApplicationContext(ApplicationContext applicationContext) {
        WebSocketServer.applicationContext = applicationContext;
    }

    private static final Logger log = LoggerFactory.getLogger(WebSocketServer.class);
    /**
     * 记录当前在线连接数
     */
    public static final Map<Integer, Session> sessionMap2 = new ConcurrentHashMap<>();


    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") Integer userId) {
        sessionMap2.put(userId, session);
        log.info("有新用户加入，userEmail={}, 当前在线人数为：{}", userId, sessionMap2.size());
        JSONObject result = new JSONObject();
        JSONArray array = new JSONArray();
        result.put("users", array);
        for (Object key : sessionMap2.keySet()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", key);
            // {"userEmail", "zhang", "userEmail": "admin"}
            array.add(jsonObject);
        }
//        {"users": [{"userEmail": "zhang"},{ "userEmail": "admin"}]}
//        sendAllMessage(JSONUtil.toJsonStr(result));  // 后台发送消息给所有的客户端
    }
    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session, @PathParam("userId") Integer userId) {
        sessionMap2.remove(userId);

        JSONObject result = new JSONObject();
        JSONArray array = new JSONArray();
        result.put("users", array);
        for (Object key : sessionMap2.keySet()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", key);
            // {"userEmail", "zhang", "userEmail": "admin"}
            array.add(jsonObject);
        }

        log.info("有一连接关闭，移除useruserEmail={}的用户session, 当前在线人数为：{}", userId, sessionMap2.size());
    }
    /**
     * 收到客户端消息后调用的方法
     * 后台收到客户端发送过来的消息
     * onMessage 是一个消息的中转站
     * 接受 浏览器端 socket.send 发送过来的 json数据
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session, @PathParam("userId") Integer userId) {
        log.info("服务端收到用户userId={}的消息:{}", userId, message);
        JSONObject obj = JSONUtil.parseObj(message);
        Integer toUserId = obj.getInt("to"); // to表示发送给哪个用户，比如 admin
        String text = obj.getStr("text"); // 发送的消息文本  hello

        /**
         *
         *  userID == 0为websocket心跳包
         */

        // 注入sender到bean
        FanoutSender fanoutSender = applicationContext.getBean(FanoutSender.class);
        if (Objects.equals(toUserId, userId)){

//            /**
//             *  发送消息通知到rabbitMQ 通知 交换机
//             */
//            Map<Integer, String> rabbitMessageList2 = new HashMap<>();
//
//            // 心跳续约
//            rabbitMessageList2.put(0, "Pong");
//            fanoutSender.sendMessage(rabbitMessageList2);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("from", userId);  // from 是 zhang
            jsonObject.put("text", "pong");  // text 同上面的text

            sendMessage(jsonObject.toString(), toUserId);

        }else{

            // 存入数据库
            MessageMapper messageMapper = applicationContext.getBean(MessageMapper.class);
            Message messageDB = new Message();
            messageDB.setPostMessageContent(text);
            messageDB.setFromUserId(userId);
            messageDB.setToUserId(toUserId);
            messageDB.setPostTime(System.currentTimeMillis());
            messageMapper.insert(messageDB);

            // 服务器端 再把消息组装一下，组装后的消息包含发送人和发送的文本内容
            // {"from": "zhang", "text": "hello"}
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("from", userId);  // from 是 zhang
            jsonObject.put("text", text);  // text 同上面的text


            /**
             * 发送消息到rabbitMQ聊天交换机
             */

            Map<Integer, String> rabbitMessageList = new HashMap<>();
            rabbitMessageList.put(toUserId, jsonObject.toString());
            fanoutSender.sendChatMessage(rabbitMessageList);

//            this.sendMessage(jsonObject.toString(), toUserId);

            log.info("发送给用户userId={}，消息：{}", toUserId, jsonObject);


            /**
             * 发送消息弹窗提示
             */
            // 接受此消息用户的类型
            NormalUserMapper normalUserMapper = applicationContext.getBean(NormalUserMapper.class);
            User localUser = normalUserMapper.selectById(userId);

            MessageNotificationDTO messageNotificationDTO = new MessageNotificationDTO();

            // 消息类型
            messageNotificationDTO.setMessageType(999);
            messageNotificationDTO.setRemoteUser(localUser);

            // 为远程用户加载本地用户的信息
            messageNotificationDTO.setNotificationContent(text);

            String result = com.alibaba.fastjson.JSONObject.toJSONString(messageNotificationDTO);

            /**
             *  发送消息通知到rabbitMQ 通知 交换机
             */
            Map<Integer, String> rabbitMessageList2 = new HashMap<>();
            rabbitMessageList2.put(toUserId, result);
            fanoutSender.sendMessage(rabbitMessageList2);

//            NotificationServer.sendMessage(result, toUserId);
        }




    }
    @OnError
    public void onError(Session sess, Throwable e) {
        Throwable cause = e.getCause();
        /* normal handling... */
        if (cause != null)
            System.out.println("Error-info: cause->" + cause);
        try {
            // Likely EOF (i.e. user killed session)
            // so just Close the input stream as instructed
            sess.close();
        } catch (IOException ex) {
            System.out.println("Handling eof, A cascading IOException was caught: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            System.out.println("Session error handled. (likely unexpected EOF) resulting in closing User Session.");

        }
    }
    /**
     * 服务端发送消息给客户端
     */
    public static void sendMessage(String message, Integer userId) {
        System.out.println("开始调用sendMessage方法");
        Session session = sessionMap2.get(userId);
        if (session == null) {
            log.error("未找到userId={}的session", userId);
            return;
        }

        if (!session.isOpen()) {
            log.error("试图发送消息到一个已关闭的session: userId={}", userId);
            return;
        }

        try {
            log.info("服务端给客户端[{}]发送消息{}", session, message);
            session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            log.error("服务端发送消息给客户端失败", e);
        }
    }


//    /**
//     * 服务端发送消息给所有客户端
//     */
//    private void sendAllMessage(String message) {
//        try {
//            for (Session session : sessionMap.values()) {
//                log.info("服务端给客户端[{}]发送消息{}", session.getId(), message);
//                session.getBasicRemote().sendText(message);
//            }
//        } catch (Exception e) {
//            log.error("服务端发送消息给客户端失败", e);
//        }
//    }
}

