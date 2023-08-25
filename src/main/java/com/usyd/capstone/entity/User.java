package com.usyd.capstone.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.context.annotation.Role;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@TableName("user")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String no;
    private String name;
    private String password;
    private int gender;

    private String phone;
    @TableField("registration_timestamp")
    private long registrationTimestamp;

    @TableField("resetting_password_timestamp")
    private long resettingPasswordTimestamp;

    private String email;

    @TableField("activation_status")
    private boolean activationStatus;

    @TableField("forget_password_verity")
    private boolean forgetPasswordVerity;
}
