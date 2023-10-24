package com.usyd.capstone.common.DTO;

import lombok.Data;

@Data
public class MessageHistoryDTO {

    private Integer id;

    private String postMessageContent;

    private Integer fromUserId;

    private Integer toUserId;

    private Long postTime;


    // user table
    private String fromUserName;


    private String fromUserAvatar;

    private String toUserName;

    private String toUserAvatar;

}
