package com.usyd.capstone.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.usyd.capstone.entity.abstractEntities.NotSuperUser;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Data
@Entity
@TableName("admin_user")
public class AdminUser extends NotSuperUser implements Serializable {

    @OneToMany(mappedBy = "adminUser")
//    mappedBy = "admin_user" 是错误的，应该将其更正为正确的“属性名”而不是表名或者类名
//    即在 AdminUserProduct 类中与 AdminUser 属性相关联的属性名称-->adminUser
/*-----------------截取自AdminUserProduct.java-----------------------*/
//    @ManyToOne
//    @JoinColumn(name = "admin_user_id", nullable = false)
//    private AdminUser adminUser;
//    这里对AdminUser的属性命名为adminUser
/*------------------------------------------------------------------*/
    @TableField(exist = false)
    private Set<AdminUserProduct> adminUserProducts;

}
