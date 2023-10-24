package com.usyd.capstone.common.DTO;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.persistence.Column;

@Data
public class MessageUserDTO {

    private Integer id;

    private String postMessageContent;

    private Integer fromUserId;

    private Integer toUserId;

    private Long postTime;


    private String name;

    private String email;
    private String password;

    private String avatarUrl;

}
