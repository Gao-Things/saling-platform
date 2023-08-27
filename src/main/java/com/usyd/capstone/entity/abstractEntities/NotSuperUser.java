package com.usyd.capstone.entity.abstractEntities;

import com.baomidou.mybatisplus.annotation.TableField;
import com.usyd.capstone.entity.abstractEntities.User;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class NotSuperUser extends User {
    @TableField("registration_timestamp")
    private long registrationTimestamp;

    @TableField("resetting_password_timestamp")
    private long resettingPasswordTimestamp;

    @TableField("forget_password_verity")
    private boolean forgetPasswordVerity;
}
