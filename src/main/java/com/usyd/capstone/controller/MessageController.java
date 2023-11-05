package com.usyd.capstone.controller;

import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.usyd.capstone.common.DTO.MessageFormat;
import com.usyd.capstone.common.DTO.Result;
import com.usyd.capstone.common.VO.RejectMessage;
import com.usyd.capstone.entity.Message;
import com.usyd.capstone.entity.NormalUser;
import com.usyd.capstone.entity.Offer;
import com.usyd.capstone.entity.Product;
import com.usyd.capstone.rabbitMq.FanoutSender;
import com.usyd.capstone.service.MessageService;
import com.usyd.capstone.service.NormalUserService;
import com.usyd.capstone.service.OfferService;
import com.usyd.capstone.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/normal/message")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private NormalUserService normalUserService;

    @Autowired
    private OfferService offerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private FanoutSender fanoutSender;


    // 获取所有的聊天者信息
    @GetMapping("/getMessageListByUserId")
    public Result getMessageListByUserId(@RequestParam("userId") Integer userId){

        return messageService.getMessageListByUserId(userId);
    }

    // 根据用户id删除所有相关聊天记录
    @GetMapping("/deleteMessageHistoryByUserIdAndRemoteUserId")
    public Result deleteMessageHistoryByUserIdAndRemoteUserId(
            @RequestParam("userId") Integer userId,
            @RequestParam("remoteUserId") Integer remoteUserId){

        return messageService.deleteMessageHistoryByUserIdAndRemoteUserId(userId, remoteUserId);
    }

    @GetMapping("/getMessageListByUserIdAndRemoteUserId")
    public Result getMessageListByUserIdAndRemoteUserId(
            @RequestParam("userId") Integer userId,
            @RequestParam("remoteUserId") Integer remoteUserId){

        return messageService.getMessageListByUserIdAndRemoteUserId(userId, remoteUserId);
    }


    @GetMapping("/getUserInfoById")
    public Result getUserInfoById(
            @RequestParam("userId") Integer userId){

        NormalUser normalUser = normalUserService.findUserInfoById(userId);
        return Result.suc(normalUser);
    }


    /**
     * 保存拒绝offer消息卡片，发送删除消息卡片
     */

    @PostMapping("/postRejectMessage")
    public Result postRejectMessage(
            @RequestBody RejectMessage rejectMessage){
        Offer offer = offerService.getById(rejectMessage.getOfferId());

        // 查出offer的提出者
        Integer toUserId = Math.toIntExact(offer.getBuyerId());

        // 查出product 的imageUrl
        Product product = productService.getById(offer.getProductId());
        // 把图片链接字符串转回list
        String[] items = product.getProductImage().substring(1, product.getProductImage().length() - 1).split(", ");


        // 组合卡片类型的message
        MessageFormat messageFormat = new MessageFormat();

        messageFormat.setMessageType(2);
        messageFormat.setCardImageUrl(items[0]);

        if (rejectMessage.getCardTitle()!=null){
            messageFormat.setCardTitle(rejectMessage.getCardTitle());
        }
        if (rejectMessage.getCardDescription() !=null){
            messageFormat.setCardDescription(rejectMessage.getCardDescription());
        }

        messageFormat.setProductId(Math.toIntExact(product.getId()));

        // 封装成JSON类型字符串存入数据库
        String rejectCardMessage =  JSON.toJSONString(messageFormat);


        Message message = new Message();
        message.setPostMessageContent(rejectCardMessage);
        message.setFromUserId(rejectMessage.getUserId());
        message.setToUserId(toUserId);
        message.setPostTime(System.currentTimeMillis());
        boolean result = messageService.save(message);



        /**
         * 发送消息到rabbitMQ聊天交换机， 发送消息通知买家
         */

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("from", rejectMessage.getUserId());
        jsonObject.put("text", rejectCardMessage);

        Map<Integer, String> rabbitMessageList = new HashMap<>();
        rabbitMessageList.put(toUserId, jsonObject.toString());
        fanoutSender.sendChatMessage(rabbitMessageList);


        if (result){
            return Result.suc();
        }else {
            return Result.fail();
        }


    }
}
