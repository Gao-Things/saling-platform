package com.usyd.capstone.entity.abstractEntities;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@MappedSuperclass
public class NotSuperUser extends User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @TableId(value = "id")
    private Long id;

    @TableField("registration_timestamp")
    private long registrationTimestamp;

    @TableField("resetting_password_timestamp")
    private long resettingPasswordTimestamp;

    @TableField("forget_password_verity")
    private boolean forgetPasswordVerity;
}
