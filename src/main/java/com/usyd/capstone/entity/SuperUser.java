package com.usyd.capstone.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.usyd.capstone.entity.abstractEntities.User;

import javax.persistence.Entity;

@Entity
@TableName("super_user")
public class SuperUser extends User {
    private String unnamedAttr;
}
