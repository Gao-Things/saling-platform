package com.usyd.capstone.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * <p>
 * 
 * </p>
 *
 * @author yyf
 * @since 2023年08月24日
 */
@Getter
@Setter
@Accessors(chain = true)
@Entity
@TableName("role")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @TableField("role_Name")
    private String roleName;

    @TableField("role_Permission_Use")
    private String rolePermissionUse;


}
