package com.usyd.capstone.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.usyd.capstone.entity.abstractEntities.NotSuperUser;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@TableName("admin_user")
public class AdminUser extends NotSuperUser implements Serializable {

    private String unnamedAttr;

}
