package com.usyd.capstone.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.usyd.capstone.common.DTO.Result;
import com.usyd.capstone.entity.NormalUser;
import com.usyd.capstone.entity.Offer;
import com.usyd.capstone.service.MessageService;
import com.usyd.capstone.service.NormalUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/normal/message")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private NormalUserService normalUserService;

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
}
