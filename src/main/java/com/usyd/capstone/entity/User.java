package com.usyd.capstone.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@TableName("user")
public class User {
    @TableId(type = IdType.AUTO) // 使用自增主键
    private Long id;
    private String no;
    private String name;
    private String password;
    private int sex;
    @TableField("role_id")
    private int roleId;
    private String phone;
    @TableField("registration_timestamp")
    private long registrationTimestamp;

    private String email;

    @TableField("activation_status")
    private boolean activationStatus;
}
