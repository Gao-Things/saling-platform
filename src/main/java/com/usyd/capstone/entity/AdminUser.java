package com.usyd.capstone.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.usyd.capstone.entity.abstractEntities.NotSuperUser;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@Entity
@TableName("admin_user")
public class AdminUser extends NotSuperUser implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @TableId(value = "id")
    private Long id;


//    mappedBy = "admin_user" 是错误的，应该将其更正为正确的“属性名”而不是表名或者类名
//    即在 AdminUserProduct 类中与 AdminUser 属性相关联的属性名称-->adminUser
/*-----------------截取自AdminUserProduct.java-----------------------*/
//    @ManyToOne
//    @JoinColumn(name = "admin_user_id", nullable = false)
//    private AdminUser adminUser;
//    这里对AdminUser的属性命名为adminUser
/*------------------------------------------------------------------*/
    @OneToMany(mappedBy = "adminUser")
    @TableField(exist = false)
    @com.github.dreamyoung.mprelation.OneToMany
    @com.github.dreamyoung.mprelation.JoinColumn(name = "id", referencedColumnName = "admin_user_id")
    private Set<AdminUserProduct> adminUserProducts;

}
