package com.usyd.capstone.common.compents;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 各个webSocket端点
 * 不拦截该请求，因为接口测试工具设置请求体困难，因此选择使用路径变量来传递token
 */
@ServerEndpoint(value = "/chat/{token}")
@Component
public class ChatEndpoint {


    /**
     * 用来储存在线用户的容器
     */
    public static Map<Long, Session> onlineUsers = new ConcurrentHashMap<>();

    /**
     * 用来给客户端发送消息
     */
    private Session session;


    /*建立时调用*/
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {

        //将当前session赋值给属性
        this.session = session;
        //从token获取用户数据
        Long normalUserId = JwtToken.getId(token);
        //将当前端点存放到onlineUsers中保存
        onlineUsers.put(normalUserId, session);
        String message = "connection built";
        sendMessage(message);

    }


    /**
     * 接收到客户端发送的数据时调用
     *
     * @param message 客户端发送的数据
     * @param session session对象
     * @return void
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            if (session != null)
            {
                session.getBasicRemote().sendText("Receive: " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //将消息存储在数据库

    }
//
//
    /**
     * 关闭时调用
     */
    @OnClose
    public void onClose(Session session, @PathParam("token") String token) {

        try {
            //从token获取用户数据
            Long normalUserId = JwtToken.getId(token);
            //从在线用户列表中移除
            onlineUsers.remove(normalUserId);
            String message = "disconnected";
            sendMessage(message);
        } catch (Exception e) {

            e.printStackTrace();
        }

    }
    private void sendMessage(String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    /**
     * 发送消息给单个用户
     *
     * @param message
     */
    public static int sendMessage(Long normalUserId, String message) {
        try {
            Session session = onlineUsers.get(normalUserId);
            if (session != null)
            {
                session.getBasicRemote().sendText(message);
                return 1;//发送成功
            }
            else
                return 2;//未链接
        } catch (IOException e) {
            e.printStackTrace();
            return 3;//发送失败
        }
    }
}
